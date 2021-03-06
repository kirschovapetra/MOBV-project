package sk.stuba.fei.uim.mobv_project.data.view_models.transaction

import android.util.Log
import androidx.lifecycle.*
import androidx.navigation.NavDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sk.stuba.fei.uim.mobv_project.data.entities.Contact
import sk.stuba.fei.uim.mobv_project.data.exceptions.ApiException
import sk.stuba.fei.uim.mobv_project.data.exceptions.TransactionFailedException
import sk.stuba.fei.uim.mobv_project.data.exceptions.ValidationException
import sk.stuba.fei.uim.mobv_project.data.repositories.BalanceRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.ContactRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.PaymentRepository
import sk.stuba.fei.uim.mobv_project.data.view_models.event.Event
import sk.stuba.fei.uim.mobv_project.ui.transaction.CreateNewTransactionFragmentDirections.actionCreateNewTransactionFragmentToMyBalanceFragment
import sk.stuba.fei.uim.mobv_project.ui.transaction.CreateNewTransactionFragmentDirections.actionCreateNewTransactionFragmentToSaveRecipientFragment
import sk.stuba.fei.uim.mobv_project.data.utils.Validation
import sk.stuba.fei.uim.mobv_project.utils.CipherUtils
import sk.stuba.fei.uim.mobv_project.utils.SecurityContext
import javax.crypto.BadPaddingException

class CreateNewTransactionViewModel(
    private val contactRepository: ContactRepository,
    private val paymentRepository: PaymentRepository,
    private val balanceRepository: BalanceRepository
) :
    ViewModel() {
    val accountId = MutableLiveData<String>()
    val amount = MutableLiveData<String>()
    val pin = MutableLiveData<String>()

    private val _eventPaymentSuccessful = MutableLiveData<Event<NavDirections?>>()
    val eventPaymentSuccessful: LiveData<Event<NavDirections?>>
        get() = _eventPaymentSuccessful

    val allContacts: LiveData<List<Contact>>
        get() = contactRepository.getAllContacts()
    private var selectedContact: Contact? = null

    private val _eventLoadingStarted = MutableLiveData<Event<Boolean>>()
    val eventLoadingStarted: LiveData<Event<Boolean>>
        get() = _eventLoadingStarted

    private val _eventApiValidationFailed = MutableLiveData<Event<String>>()
    val eventApiValidationFailed: LiveData<Event<String>>
        get() = _eventApiValidationFailed

    private val _eventInvalidPin = MutableLiveData<Event<Boolean>>()
    val eventInvalidPin: LiveData<Event<Boolean>>
        get() = _eventInvalidPin

    fun setSelectedContact(contact: Contact?) {
        selectedContact = contact
        accountId.value = contact?.contactId.orEmpty()
    }

    private fun validatePin(): Boolean {
        val pinValid = Validation.isPinValid(pin.value)
        _eventInvalidPin.postValue(Event(!pinValid))

        return pinValid
    }

    fun sendPayment() {
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
                            amount.value?.let {
                                paymentRepository.sendAndSyncPayment(
                                    account.accountId,
                                    decryptedPrivateKey,
                                    accountId.value!!,
                                    amount = it
                                )
                            }
                            balanceRepository.syncBalances(account.accountId)

                            if (isNewRecipient()) {
                                _eventPaymentSuccessful.postValue(
                                    Event(
                                        actionCreateNewTransactionFragmentToSaveRecipientFragment(
                                            accountId.value.orEmpty()
                                        )
                                    )
                                )
                            } else {
                                _eventPaymentSuccessful.postValue(
                                    Event(
                                        actionCreateNewTransactionFragmentToMyBalanceFragment()
                                    )
                                )
                            }
                        }
                    } catch (exception: ValidationException) {
                        _eventApiValidationFailed.postValue(Event(exception.message.toString()))
                    } catch (exception: TransactionFailedException) {
                        _eventApiValidationFailed.postValue(Event(exception.message.toString()))
                    } catch (exception: BadPaddingException) {
                        _eventInvalidPin.postValue(Event(true))
                    } catch (exeption: ApiException) {
                        Log.w("ApiException", exeption.toString())
                        _eventApiValidationFailed.postValue(Event(exeption.message.toString()))
                    } catch (exception: Exception) {
                        Log.e("CreateNewTransaction", exception.message.toString())
                        _eventApiValidationFailed.postValue(Event(exception.message.toString()))
                    }
                } else {
                    _eventPaymentSuccessful.postValue(Event(null))
                }
            }
        }
    }

    private fun isFormValid(): Boolean {
        var isFormValid = true
        isFormValid = isFormValid && validatePin()

        return isFormValid
    }

    private fun isNewRecipient(): Boolean {
        return contactRepository.getDeadContactById(accountId.value.orEmpty()) == null
    }
}