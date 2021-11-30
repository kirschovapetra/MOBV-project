package sk.stuba.fei.uim.mobv_project.data.view_models.intro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import sk.stuba.fei.uim.mobv_project.data.view_models.event.Event
import sk.stuba.fei.uim.mobv_project.ui.intro.IntroFragmentDirections.actionIntroFragmentToCreateWalletFragment
import sk.stuba.fei.uim.mobv_project.ui.intro.IntroFragmentDirections.actionIntroFragmentToImportWalletFragment
import sk.stuba.fei.uim.mobv_project.ui.utils.Validation

class IntroViewModel : ViewModel() {

    val firstName = MutableLiveData<String>()
    val lastName = MutableLiveData<String>()
    val pin = MutableLiveData<String>()
    val confirmationPin = MutableLiveData<String>()

    private val _eventInvalidPin = MutableLiveData<Event<Boolean>>()
    val eventInvalidPin: LiveData<Event<Boolean>>
        get() = _eventInvalidPin

    private val _eventPinMatch = MutableLiveData<Event<Boolean>>()
    val eventPinMatch: LiveData<Event<Boolean>>
        get() = _eventPinMatch

    private val _eventLocalAccountCreated = MutableLiveData<Event<NavDirections>>()
    val eventLocalAccountCreated: LiveData<Event<NavDirections>>
        get() = _eventLocalAccountCreated

    fun createNewWallet() {
        if (validateInput()) {
            _eventLocalAccountCreated.value = Event(
                actionIntroFragmentToCreateWalletFragment(
                    firstName.value, lastName.value, pin.value!!
                )
            )
        }
    }

    fun importWallet() {
        if (validateInput()) {
            _eventLocalAccountCreated.value = Event(
                actionIntroFragmentToImportWalletFragment(
                    firstName.value, lastName.value, pin.value!!
                )
            )
        }
    }

    private fun validateInput(): Boolean {
        var inputsValid = true;

        inputsValid = inputsValid && validatePin()
        inputsValid = inputsValid && pinsMatch()

        return inputsValid
    }

    private fun validatePin(): Boolean {
        val pinValid = Validation.validatePin(pin.value)
        _eventInvalidPin.value = Event(!pinValid)

        return pinValid
    }

    private fun pinsMatch(): Boolean {
        val pinsMatch = pin.value.equals(confirmationPin.value)
        _eventPinMatch.value = Event(pinsMatch)

        return pinsMatch
    }
}