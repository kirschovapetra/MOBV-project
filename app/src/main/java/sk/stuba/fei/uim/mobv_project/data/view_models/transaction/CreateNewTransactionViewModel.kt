package sk.stuba.fei.uim.mobv_project.data.view_models.transaction

import android.util.Log
import androidx.lifecycle.*
import androidx.navigation.NavDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sk.stuba.fei.uim.mobv_project.data.entities.Contact
import sk.stuba.fei.uim.mobv_project.data.exceptions.ApiException
import sk.stuba.fei.uim.mobv_project.data.exceptions.ValidationException
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
    private val paymentRepository: PaymentRepository
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
        val pinValid = Validation.validatePin(pin.value)
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
                    } catch (exeption: ValidationException) {
                        _eventApiValidationFailed.postValue(Event(exeption.message.toString()))
                    } catch (exeption: BadPaddingException) {
                        Log.e("BadPadding exception", exeption.toString())
                        _eventInvalidPin.postValue(Event(true))
                    } catch (exeption: ApiException) {
                        Log.e("ApiException exception", exeption.toString())
                        _eventApiValidationFailed.postValue(Event(exeption.message.toString()))
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