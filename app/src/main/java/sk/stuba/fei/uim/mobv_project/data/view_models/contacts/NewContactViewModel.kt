package sk.stuba.fei.uim.mobv_project.data.view_models.contacts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sk.stuba.fei.uim.mobv_project.data.entities.Contact
import sk.stuba.fei.uim.mobv_project.data.repositories.AccountRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.ContactRepository

class NewContactViewModel(
    val contactRepo: ContactRepository,
    val accountRepo: AccountRepository
) : ViewModel(){

    val MainAccountID = "1" // todo spravit static usera

    // UI
    var contactName = MutableLiveData<String>()
    var contactAccountId = MutableLiveData<String>()

    // DATA
    var isNew = MutableLiveData<Boolean>()

    var accountWithProvidedIdNotRegistered = MutableLiveData<Boolean>()
    var duplicatedContact = MutableLiveData<List<Contact>>()


    // OBSERVER
    var contactIdObserver = Unit

    init {
        createContactAccountIdObserver()
    }

    override fun onCleared() {
        super.onCleared()
        contactAccountId.removeObserver { contactIdObserver }
    }

    private fun fetchAccountFromDbAndSetIfItExist(id: String) {
        val account = accountRepo.getAccountButDead(id)
        accountWithProvidedIdNotRegistered.postValue(account.isEmpty())
    }

    private fun fetchContactDuplicity(contactId: String) {
        val contact = contactRepo.getDeadContactByIdAndSourceAccount(contactId, MainAccountID)
        duplicatedContact.postValue(contact)
    }

    private fun createContactAccountIdObserver(){
       contactIdObserver =  contactAccountId.observeForever { accountId ->
            CoroutineScope(Dispatchers.IO).launch {
                fetchAccountFromDbAndSetIfItExist(accountId)
                fetchContactDuplicity(accountId)
            }
        }
    }
}