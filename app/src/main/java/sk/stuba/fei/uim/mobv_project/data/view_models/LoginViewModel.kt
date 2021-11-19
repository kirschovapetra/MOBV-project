package sk.stuba.fei.uim.mobv_project.data.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    val pin = MutableLiveData<String>()

    private val _eventPinCorrect = MutableLiveData<Boolean>()
    val eventPinCorrect: LiveData<Boolean>
        get() = _eventPinCorrect

    fun validatePin() {
        //TODO: validate against DB
        _eventPinCorrect.value = true
    }

    fun afterNavigationComplete() {
        _eventPinCorrect.value = false
    }
}