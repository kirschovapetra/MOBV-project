package sk.stuba.fei.uim.mobv_project.data.view_models.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.RoomDatabase
import sk.stuba.fei.uim.mobv_project.data.entities.Contact
import sk.stuba.fei.uim.mobv_project.data.repositories.ContactRepository
import sk.stuba.fei.uim.mobv_project.data.view_models.event.Event

class CreateNewTransactionViewModel() : ViewModel(){
    val accountId = MutableLiveData<String>()
    val amount = MutableLiveData<Float>()

    private val _eventPaymentSuccessful = MutableLiveData<Event<Any?>>()
    val eventPaymentSuccessful: LiveData<Event<Any?>>
        get() = _eventPaymentSuccessful

    private val _eventEnoughMoneyHappyFace = MutableLiveData<Event<Boolean>>()
    val eventEnoughMoneyHappyFace: LiveData<Event<Boolean>>
        get() = _eventEnoughMoneyHappyFace

    private val _eventAccountValid = MutableLiveData<Event<Boolean>>()
    val eventAccountValid: LiveData<Event<Boolean>>
        get() = _eventAccountValid

    private val _allContacts = MutableLiveData<List<Contact>>()
    val allContact: LiveData<List<Contact>>
        get() = _allContacts

    fun sendPayment(){
        var validationResult = true

        validationResult = validationResult && validateAccount()
        validationResult = validationResult && validateAmount()
        if(validationResult){
            // TODO send payment
            _eventPaymentSuccessful.value = Event(true)
        }else{
            _eventPaymentSuccessful.value = Event(false)
        }
    }

    private fun validateAmount():Boolean{
        val result = true
        _eventEnoughMoneyHappyFace.value = Event(result)
        return result
    }

    private fun validateAccount():Boolean{
        val result = true
        _eventAccountValid.value = Event(result)
        return result
    }
}