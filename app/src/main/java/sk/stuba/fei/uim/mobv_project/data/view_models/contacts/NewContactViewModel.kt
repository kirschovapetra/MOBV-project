package sk.stuba.fei.uim.mobv_project.data.view_models.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sk.stuba.fei.uim.mobv_project.data.entities.Account
import sk.stuba.fei.uim.mobv_project.data.entities.Contact
import sk.stuba.fei.uim.mobv_project.data.repositories.AccountRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.ContactRepository
import sk.stuba.fei.uim.mobv_project.data.view_models.event.Event
import sk.stuba.fei.uim.mobv_project.utils.SecurityContext

class NewContactViewModel(
    private val contactRepo: ContactRepository,
    private val accountRepo: AccountRepository
) : ViewModel() {

    enum class FormStatus {
        OK,
        INVALID_NAME,
        INVALID_CONTACT_ID,
        NON_EXISTING_ACCOUNT,
        DUPLICATED_ACCOUNT,
        ADDING_YOURSELF
    }

    enum class SaveResult {
        CREATED, UPDATED
    }

    // UI
    val contactName = MutableLiveData("")
    val contactAccountId = MutableLiveData("")

    // DATA
    private val _isNew = MutableLiveData(true)
    val isNew: LiveData<Boolean>
        get() = _isNew

    private val account: Account = SecurityContext.account!!

    // EVENTS
    private val _eventInvalidForm = MutableLiveData<Event<FormStatus>>()
    val eventInvalidForm: LiveData<Event<FormStatus>>
        get() = _eventInvalidForm

    private val _eventContactSaved = MutableLiveData<Event<SaveResult>>()
    val eventContactSave: LiveData<Event<SaveResult>>
        get() = _eventContactSaved


    fun setContact(contact: Contact) {
        contactName.value = contact.name
        contactAccountId.value = contact.contactId
        _isNew.value = false
    }

    fun saveContact() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val formStatus = validateFields()
                if (formStatus != FormStatus.OK) {
                    _eventInvalidForm.postValue(Event(formStatus))
                    return@withContext
                }

                val contact = getContact()
                if (isNew.value!!) {
                    contactRepo.insertContact(contact)
                    _eventContactSaved.postValue(Event(SaveResult.CREATED))
                } else {
                    contactRepo.updateContact(contact)
                    _eventContactSaved.postValue(Event(SaveResult.UPDATED))
                }
            }
        }
    }

    private fun getContact(): Contact {
        return Contact(
            contactAccountId.value!!,
            contactName.value!!,
            account.accountId
        )
    }

    private fun accountDoesNotExist(id: String): Boolean {
        return accountRepo.getAccountButDead(id).isEmpty()
    }

    private fun isContactDuplicate(contactId: String): Boolean {
        return contactRepo.getDeadContactByIdAndSourceAccount(contactId, account.accountId)
                   .isNotEmpty()
    }

    private fun validateFields(): FormStatus {
        return when {
            contactName.value == "" -> FormStatus.INVALID_NAME

            contactAccountId.value == "" -> FormStatus.INVALID_CONTACT_ID

            isNew.value!! && accountDoesNotExist(account.accountId)
            -> FormStatus.NON_EXISTING_ACCOUNT

            isNew.value!! && isContactDuplicate(contactAccountId.value!!)
            -> FormStatus.DUPLICATED_ACCOUNT

            contactAccountId.value == account.accountId -> FormStatus.ADDING_YOURSELF

            else -> FormStatus.OK
        }
    }
}