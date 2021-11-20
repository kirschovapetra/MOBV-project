package sk.stuba.fei.uim.mobv_project.data.view_models.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sk.stuba.fei.uim.mobv_project.data.view_models.event.Event

class LoginViewModel : ViewModel() {

    val pin = MutableLiveData<String>()

    private val _eventPinCorrect = MutableLiveData<Event<Boolean>>()
    val eventPinCorrect: LiveData<Event<Boolean>>
        get() = _eventPinCorrect

    fun validatePin() {
        //TODO: validate against DB
        val pinCorrect = true
        _eventPinCorrect.value = Event(pinCorrect)
    }
}