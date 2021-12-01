package sk.stuba.fei.uim.mobv_project.data.view_models.settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sk.stuba.fei.uim.mobv_project.data.exceptions.ApiException
import sk.stuba.fei.uim.mobv_project.data.exceptions.TransactionFailedException
import sk.stuba.fei.uim.mobv_project.data.exceptions.ValidationException
import sk.stuba.fei.uim.mobv_project.data.repositories.AccountRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.BalanceRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.ContactRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.PaymentRepository
import sk.stuba.fei.uim.mobv_project.data.utils.Validation
import sk.stuba.fei.uim.mobv_project.data.view_models.event.Event
import sk.stuba.fei.uim.mobv_project.utils.CipherUtils
import sk.stuba.fei.uim.mobv_project.utils.SecurityContext
import java.lang.Exception
import javax.crypto.BadPaddingException


class SettingsViewModel(
    private val accountRepo: AccountRepository,
    private val balanceRepo: BalanceRepository,
    private val contactRepo: ContactRepository,
    private val paymentRepo: PaymentRepository,
) : ViewModel() {
    // "asset_code": "DOGECOIN", "asset_issuer": "GB46LWKQW65CZ7AMEY7JD4Z4ADX2CA4XYQBAJK6VTIUP6MWQUKCRQDJF"
    // "asset_code": "USD", "asset_issuer": "GA234B7P5ME6FTFLZ77PBYMRUJ3L33I6UPNYZ3XAUY4NNT7QWIAV7GL5"

    val assetCode = MutableLiveData<String>()
    val assetIssuer = MutableLiveData<String>()
    val pin = MutableLiveData<String>()

    private val _eventTransactionSuccessful = MutableLiveData<Event<String>>()
    val eventTransactionSuccessful: LiveData<Event<String>>
        get() = _eventTransactionSuccessful

    private val _eventLoadingStarted = MutableLiveData<Event<Boolean>>()
    val eventLoadingStarted: LiveData<Event<Boolean>>
        get() = _eventLoadingStarted

    private val _eventApiValidationFailed = MutableLiveData<Event<String>>()
    val eventApiValidationFailed: LiveData<Event<String>>
        get() = _eventApiValidationFailed

    private val _eventInvalidPin = MutableLiveData<Event<Boolean>>()
    val eventInvalidPin: LiveData<Event<Boolean>>
        get() = _eventInvalidPin

    fun clearDatabase() {
        viewModelScope.launch {
            accountRepo.clearAccounts()
            balanceRepo.clearBalances()
            contactRepo.clearContacts()
            paymentRepo.clearPayments()
        }
    }

    fun changeTrust() {
        _eventLoadingStarted.value = Event(true)

        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                if (isFormValid()) {
                    try {
                        if (SecurityContext.account == null) {
                            Log.e("account", "SecurityContext.account is null")
                        } else {
                            val account = SecurityContext.account!!
                            val privateKey = account.privateKey!!
                            val decryptedPrivateKey = CipherUtils.decrypt(
                                privateKey,
                                pin.value!!,
                                account.salt!!,
                                account.iv!!
                            )
                            balanceRepo.addAndSyncTrustedAsset(
                                account.accountId,
                                decryptedPrivateKey,
                                assetCode.value!!,
                                assetIssuer.value!!)

                            _eventTransactionSuccessful.postValue(
                                Event("Asset has been successfully added to your balances.")
                            )

                        }
                    } catch (e: ValidationException) {
                        _eventApiValidationFailed.postValue(Event(e.message.toString()))
                    }
                    catch (e: TransactionFailedException) {
                        _eventApiValidationFailed.postValue(Event(e.message.toString()))
                    }
                    catch (e: ApiException) {
                        _eventApiValidationFailed.postValue(Event(e.message.toString()))
                    }
                    catch (e: BadPaddingException) {
                        Log.e("BadPadding exception", e.toString())
                        _eventInvalidPin.postValue(Event(true))
                    }
                } else {
                    _eventApiValidationFailed.postValue(Event("Invalid input data."))
                }
            }
        }
    }

    private fun validatePin(): Boolean {
        val pinValid = Validation.validatePin(pin.value)
        _eventInvalidPin.postValue(Event(!pinValid))

        return pinValid
    }


    private fun isFormValid(): Boolean {
        var isFormValid = true
        isFormValid = isFormValid
                && !assetCode.value.isNullOrEmpty()
                && !assetIssuer.value.isNullOrEmpty()
                && validatePin()

        return isFormValid
    }


}