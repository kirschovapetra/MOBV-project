package sk.stuba.fei.uim.mobv_project.data.view_models.intro

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sk.stuba.fei.uim.mobv_project.R
import sk.stuba.fei.uim.mobv_project.data.exceptions.ValidationException
import sk.stuba.fei.uim.mobv_project.data.repositories.AccountRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.BalanceRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.PaymentRepository
import sk.stuba.fei.uim.mobv_project.data.view_models.event.Event
import sk.stuba.fei.uim.mobv_project.ui.intro.ImportWalletFragmentArgs
import sk.stuba.fei.uim.mobv_project.utils.SecurityContext

class ImportWalletViewModel(
    private val accountRepository: AccountRepository,
    private val paymentRepository: PaymentRepository,
    private val balanceRepository: BalanceRepository,
    private val args: ImportWalletFragmentArgs
) : ViewModel() {
    enum class FormError {
        KEY_EMPTY,
        KEY_INVALID_LENGTH
    }

    val privateKey = MutableLiveData<String>()

    private val _eventLoadingStart = MutableLiveData<Event<Boolean>>()
    val eventLoadingStart: LiveData<Event<Boolean>>
        get() = _eventLoadingStart

    private val _eventFormError = MutableLiveData<Event<Int>>()
    val eventFormError: LiveData<Event<Int>>
        get() = _eventFormError

    private val _eventRepositoryValidationError = MutableLiveData<Event<String>>()
    val eventRepositoryValidationError: LiveData<Event<String>>
        get() = _eventRepositoryValidationError

    private val _eventLocalAccountCreated = MutableLiveData<Event<Boolean>>()
    val eventLocalAccountCreated: LiveData<Event<Boolean>>
        get() = _eventLocalAccountCreated

    fun createLocalAccount() {
        _eventLoadingStart.value = Event(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val formError = validateForm()
                formError?.let {
                    _eventFormError.postValue(Event(getMessageResourceId(it)))
                    return@withContext
                }

                try {
                    val account = accountRepository.syncAccount(
                        args.firstName.orEmpty(),
                        args.lastName.orEmpty(),
                        args.pin,
                        privateKey.value.orEmpty()
                    )
                    paymentRepository.syncPayments(account.accountId)
                    balanceRepository.syncBalances(account.accountId)

                    SecurityContext.account = account
                    _eventLocalAccountCreated.postValue(Event(true))
                }
                catch (ex: ValidationException) {
                    _eventRepositoryValidationError.postValue(Event(ex.message.toString()))
                }
                catch (ex: Exception) {
                    Log.e("ImportWallet", ex.message.toString())
                    _eventRepositoryValidationError.postValue(Event(ex.message.toString()))
                }
            }
        }
    }

    private fun validateForm(): FormError? {
        return when {
            privateKey.value.isNullOrEmpty() -> FormError.KEY_EMPTY
            privateKey.value!!.length != 56 -> FormError.KEY_INVALID_LENGTH
            else -> null
        }
    }

    private fun getMessageResourceId(formError: FormError): Int {
        return when (formError) {
            FormError.KEY_EMPTY -> R.string.import_form_error_empty_key
            FormError.KEY_INVALID_LENGTH -> R.string.import_form_error_invalid_key_length
        }
    }
}