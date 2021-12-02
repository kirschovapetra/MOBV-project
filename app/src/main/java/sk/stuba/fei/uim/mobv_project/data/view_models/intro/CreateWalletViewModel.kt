package sk.stuba.fei.uim.mobv_project.data.view_models.intro

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.stellar.sdk.KeyPair
import sk.stuba.fei.uim.mobv_project.data.exceptions.TransactionFailedException
import sk.stuba.fei.uim.mobv_project.data.exceptions.ValidationException
import sk.stuba.fei.uim.mobv_project.data.repositories.AccountRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.BalanceRepository
import sk.stuba.fei.uim.mobv_project.data.view_models.event.Event
import sk.stuba.fei.uim.mobv_project.ui.intro.CreateWalletFragmentArgs
import sk.stuba.fei.uim.mobv_project.utils.SecurityContext

class CreateWalletViewModel(
    private val accountRepository: AccountRepository,
    private val balanceRepository: BalanceRepository,
    private val args: CreateWalletFragmentArgs
) : ViewModel() {

    private val keyPair = MutableLiveData<KeyPair>()
    val privateKey: LiveData<String> = Transformations.map(keyPair) {
        String(it.secretSeed)
    }

    private val _eventLoadingStart = MutableLiveData<Event<Boolean>>()
    val eventLoadingStart: LiveData<Event<Boolean>>
        get() = _eventLoadingStart

    private val _eventRepositoryValidationError = MutableLiveData<Event<String>>()
    val eventRepositoryValidationError: LiveData<Event<String>>
        get() = _eventRepositoryValidationError

    private val _eventLocalAccountCreated = MutableLiveData<Event<Boolean>>()
    val eventLocalAccountCreated: LiveData<Event<Boolean>>
        get() = _eventLocalAccountCreated

    private val _eventContinue = MutableLiveData<Event<Boolean>>()
    val eventContinue: LiveData<Event<Boolean>>
        get() = _eventContinue

    private val _eventCopyToClipboard = MutableLiveData<Event<String>>()
    val eventCopyToClipboard: LiveData<Event<String>>
        get() = _eventCopyToClipboard

    init {
        keyPair.value = KeyPair.random()
        Log.d(javaClass.simpleName, args.toString())
    }

    fun createLocalAccount() {
        _eventLoadingStart.value = Event(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val account = accountRepository.createAndSyncAccount(
                        args.firstName.orEmpty(),
                        args.lastName.orEmpty(),
                        args.pin,
                        keyPair.value!!
                    )
                    balanceRepository.syncBalances(account.accountId)

                    SecurityContext.account = account
                    _eventLocalAccountCreated.postValue(Event(true))
                }
                catch (ex: TransactionFailedException) {
                    _eventRepositoryValidationError.postValue(Event(ex.message.toString()))
                }
                catch (ex: ValidationException) {
                    _eventRepositoryValidationError.postValue(Event(ex.message.toString()))
                }
            }
        }
    }

    fun continueToMyBalance() {
        _eventContinue.value = Event(true)
    }

    fun copyPrivateKeyToClipboard() {
        _eventCopyToClipboard.value = Event(privateKey.value.orEmpty())
    }
}