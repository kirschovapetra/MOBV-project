package sk.stuba.fei.uim.mobv_project.data.view_models.transaction

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.navigation.NavDirections
import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sk.stuba.fei.uim.mobv_project.api.StellarApi
import sk.stuba.fei.uim.mobv_project.data.entities.Contact
import sk.stuba.fei.uim.mobv_project.data.repositories.ContactRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.PaymentRepository
import sk.stuba.fei.uim.mobv_project.data.view_models.event.Event
import sk.stuba.fei.uim.mobv_project.ui.transaction.CreateNewTransactionFragmentDirections
import sk.stuba.fei.uim.mobv_project.ui.transaction.CreateNewTransactionFragmentDirections.actionCreateNewTransactionFragmentToMyBalanceFragment
import sk.stuba.fei.uim.mobv_project.ui.transaction.CreateNewTransactionFragmentDirections.actionCreateNewTransactionFragmentToSaveRecipientFragment
import sk.stuba.fei.uim.mobv_project.data.utils.Validation
import sk.stuba.fei.uim.mobv_project.utils.CipherUtils
import sk.stuba.fei.uim.mobv_project.utils.SecurityContext
import kotlin.coroutines.coroutineContext

class CreateNewTransactionViewModel(
    private val contactRepository: ContactRepository,
    private val paymentRepository: PaymentRepository
) :
    ViewModel() {
    val accountId = MutableLiveData<String>()
    val amount = MutableLiveData<Float>()
    val pin = MutableLiveData<String>()

    private val _eventPaymentSuccessful = MutableLiveData<Event<NavDirections?>>()
    val eventPaymentSuccessful: LiveData<Event<NavDirections?>>
        get() = _eventPaymentSuccessful

    private val _eventEnoughMoneyHappyFace = MutableLiveData<Event<Boolean>>()
    val eventEnoughMoneyHappyFace: LiveData<Event<Boolean>>
        get() = _eventEnoughMoneyHappyFace

    private val _eventAccountValid = MutableLiveData<Event<Boolean>>()
    val eventAccountValid: LiveData<Event<Boolean>>
        get() = _eventAccountValid

    val allContacts: LiveData<List<Contact>>
        get() = contactRepository.getAllContacts()
    private var selectedContact: Contact? = null

    fun setSelectedContact(contact: Contact) {
        selectedContact = contact
        accountId.value = contact.contactId
    }

    private val _eventInvalidPin = MutableLiveData<Event<Boolean>>()
    val eventInvalidPin: LiveData<Event<Boolean>>
        get() = _eventInvalidPin

    private fun validatePin(): Boolean {
        val pinValid = Validation.validatePin(pin.value)
        _eventInvalidPin.value = Event(!pinValid)

        return pinValid
    }

    fun sendPayment() {
        var validationResult = true

        // validationResult = validationResult && validateAmount()
        validationResult = validationResult && validatePin()
        if (validationResult) {
            viewModelScope.launch {
                withContext(Dispatchers.Default) {
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
                        paymentRepository.sendAndSyncPayment(
                            account.accountId,
                            decryptedPrivateKey,
                            accountId.value!!,
                            amount = amount.value.toString()
                        )
                    }
                }
            }

            var newRecipient = true
            selectedContact?.let {
                newRecipient = accountId.value != it.contactId
            }
            if (newRecipient) {
                _eventPaymentSuccessful.value = Event(
                    actionCreateNewTransactionFragmentToSaveRecipientFragment(accountId.value.orEmpty())
                )
            } else {
                _eventPaymentSuccessful.value = Event(
                    actionCreateNewTransactionFragmentToMyBalanceFragment()
                )
            }
        } else {
            _eventPaymentSuccessful.value = Event(null)
        }
    }

    private fun validateAmount(): Boolean {
        // TODO validate or remove
        val result = true
        _eventEnoughMoneyHappyFace.value = Event(result)
        return result
    }
}