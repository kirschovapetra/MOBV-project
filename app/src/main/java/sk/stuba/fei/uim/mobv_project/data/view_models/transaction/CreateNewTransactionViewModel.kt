package sk.stuba.fei.uim.mobv_project.data.view_models.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import androidx.room.RoomDatabase
import sk.stuba.fei.uim.mobv_project.data.entities.Contact
import sk.stuba.fei.uim.mobv_project.data.repositories.ContactRepository
import sk.stuba.fei.uim.mobv_project.data.view_models.event.Event
import sk.stuba.fei.uim.mobv_project.ui.transaction.CreateNewTransactionFragmentDirections
import sk.stuba.fei.uim.mobv_project.ui.transaction.CreateNewTransactionFragmentDirections.actionCreateNewTransactionFragmentToMyBalanceFragment
import sk.stuba.fei.uim.mobv_project.ui.transaction.CreateNewTransactionFragmentDirections.actionCreateNewTransactionFragmentToSaveRecipientFragment

class CreateNewTransactionViewModel(private val contactRepository: ContactRepository) : ViewModel(){
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

    fun setSelectedContact(contact: Contact){
        selectedContact = contact
        accountId.value = contact.contactId
    }


    fun sendPayment(){
        var validationResult = true

        validationResult = validationResult && validateAccount()
        validationResult = validationResult && validateAmount()
        if(validationResult){
            // TODO do payment
            var newRecipient = true
            selectedContact?.let {
                newRecipient = accountId.value != it.contactId
            }
            if(newRecipient){
                _eventPaymentSuccessful.value = Event(
                    actionCreateNewTransactionFragmentToSaveRecipientFragment(accountId.value.orEmpty())
                )
            }else{
                _eventPaymentSuccessful.value = Event(
                    actionCreateNewTransactionFragmentToMyBalanceFragment()
                )
            }
        }else{
            _eventPaymentSuccessful.value = Event(null)
        }
    }

    private fun validateAmount():Boolean{
        // TODO validate or remove
        val result = true
        _eventEnoughMoneyHappyFace.value = Event(result)
        return result
    }

    private fun validateAccount():Boolean{
        // TODO validate or remove
        val result = true
        _eventAccountValid.value = Event(result)
        return result
    }
}