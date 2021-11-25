package sk.stuba.fei.uim.mobv_project.data.view_models.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sk.stuba.fei.uim.mobv_project.data.view_models.event.Event
import sk.stuba.fei.uim.mobv_project.utils.HashingUtils

class CreatePinViewModel : ViewModel() {

    val pin = MutableLiveData<String>()
    val confirmationPin = MutableLiveData<String>()

    private val _eventPinMatch = MutableLiveData<Event<Boolean>>()
    val eventPinMatch: LiveData<Event<Boolean>>
        get() = _eventPinMatch

    private val _eventRegistered = MutableLiveData<Event<Boolean>>()
    val eventRegistered: LiveData<Event<Boolean>>
        get() = _eventRegistered

    fun register() {
        if (pinsMatch()) {
            savePin()
        }
    }

    private fun pinsMatch(): Boolean {
        val pinsMatch = pin.value.equals(confirmationPin.value)
        _eventPinMatch.value = Event(pinsMatch)

        return pinsMatch
    }

    private fun savePin() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val pinHash: String = HashingUtils.hashString(pin.value.toString())

                Log.d(this::class.java.simpleName, "hashed pin: $pinHash")
                Log.d(this::class.java.simpleName, "hash match: ${HashingUtils.verifyHash(pin.value.toString(), pinHash)}")

                //TODO: save to DB

                _eventRegistered.postValue(Event(true))
            }
        }
    }

}