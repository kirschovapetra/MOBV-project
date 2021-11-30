package sk.stuba.fei.uim.mobv_project.data.view_models.contacts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sk.stuba.fei.uim.mobv_project.data.entities.Contact
import sk.stuba.fei.uim.mobv_project.data.repositories.AccountRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.ContactRepository

class NewContactViewModel(
    val contactRepo: ContactRepository,
    val accountRepo: AccountRepository
) : ViewModel(){

    enum class FormStatus {
        OK,
        INVALID_NAME,
        INVALID_CONTACT_ID,
        NON_EXISTING_ACCOUNT,
        DUPLICATED_ACCOUNT,
        ADDING_YOURSELF
    }

    val MainAccountID = "1" // todo spravit static usera

    // UI
    var contactName = MutableLiveData<String>()
    var contactAccountId = MutableLiveData<String>()

    // DATA
    var isNew = MutableLiveData<Boolean>()

    private var accountWithProvidedIdNotRegistered = MutableLiveData<Boolean>()
    private var duplicatedContact = MutableLiveData<List<Contact>>()


    // OBSERVER
    private var contactIdObserver = Unit

    init {
        createContactAccountIdObserver()
        initEmptyData()
    }

    fun setContact(contact: Contact){
        contactName.value = contact.name!!
        contactAccountId.value = contact.contactId
        isNew.value = false
    }

    fun getContact() : Contact {
        return Contact(
            contactAccountId.value!!,
            contactName.value!!,
            MainAccountID
        )
    }

    fun saveContact(contact: Contact) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (isNew.value!!) {
                    contact.sourceAccount = MainAccountID
                    contactRepo.insertContact(contact)// insertContact(contact)
                } else {
                    contactRepo.updateContact(contact)
                }
            }
        }
    }

     fun returnStatusOfTheForm() : FormStatus {
        val contact = Contact(
            contactAccountId.value!!,
            contactName.value!!,
            MainAccountID
        )

        return validateFields(contact)
    }

    fun getToasterMessage(formStatus : FormStatus) : String{
        return when (formStatus) {
            FormStatus.INVALID_NAME -> { "Invalid name! Text can only contain" }
            FormStatus.INVALID_CONTACT_ID -> "You have to set existing contact id"
            FormStatus.NON_EXISTING_ACCOUNT -> "Account with this id is not registered"
            FormStatus.DUPLICATED_ACCOUNT ->
                "You already have contact with this id! Name: ${duplicatedContact.value!![0].name}"
            FormStatus.ADDING_YOURSELF -> "You can not add yourself!"
            else -> ""
        }
    }

    private fun initEmptyData(){
        contactName.value = ""
        contactAccountId.value = ""
        isNew.value = true
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

    private fun validateFields(contact: Contact): FormStatus {
       return when {
           contact.name == "" -> FormStatus.INVALID_NAME

           contact.contactId == "" -> FormStatus.INVALID_CONTACT_ID

           isNew.value!! && accountWithProvidedIdNotRegistered.value!!
                -> FormStatus.NON_EXISTING_ACCOUNT

           isNew.value!! && duplicatedContact.value!!.isNotEmpty()
                -> FormStatus.DUPLICATED_ACCOUNT

           contact.contactId == MainAccountID -> FormStatus.ADDING_YOURSELF

           else -> FormStatus.OK
       }
    }
}