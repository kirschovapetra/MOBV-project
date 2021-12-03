package sk.stuba.fei.uim.mobv_project.data.view_models.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sk.stuba.fei.uim.mobv_project.R
import sk.stuba.fei.uim.mobv_project.data.entities.Account
import sk.stuba.fei.uim.mobv_project.data.entities.Contact
import sk.stuba.fei.uim.mobv_project.data.repositories.AccountRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.ContactRepository
import sk.stuba.fei.uim.mobv_project.data.utils.Validation
import sk.stuba.fei.uim.mobv_project.data.view_models.event.Event
import sk.stuba.fei.uim.mobv_project.utils.SecurityContext

class NewContactViewModel(
    private val contactRepo: ContactRepository,
    private val accountRepo: AccountRepository
) : ViewModel() {

    enum class FormError {
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
    private val _eventInvalidForm = MutableLiveData<Event<Int>>()
    val eventInvalidForm: LiveData<Event<Int>>
        get() = _eventInvalidForm

    private val _eventContactSaved = MutableLiveData<Event<Int>>()
    val eventContactSave: LiveData<Event<Int>>
        get() = _eventContactSaved


    fun setContact(contact: Contact) {
        contactName.value = contact.name
        contactAccountId.value = contact.contactId
        _isNew.value = false
    }

    fun saveContact() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val formError = validateFields()
                formError?.let {
                    _eventInvalidForm.postValue(Event(getMessageResourceId(it)))
                    return@withContext
                }

                val contact = getContact()
                if (isNew.value!!) {
                    contactRepo.insertContact(contact)
                    _eventContactSaved.postValue(
                        Event(
                            getMessageResourceId(SaveResult.CREATED)
                        )
                    )
                } else {
                    contactRepo.updateContact(contact)
                    _eventContactSaved.postValue(
                        Event(
                            getMessageResourceId(SaveResult.UPDATED)
                        )
                    )
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

    private fun validateFields(): FormError? {
        return when {
            contactName.value == "" -> FormError.INVALID_NAME

            !Validation.isAccountIdValid(contactAccountId.value) -> FormError.INVALID_CONTACT_ID

            isNew.value!! && accountDoesNotExist(account.accountId)
            -> FormError.NON_EXISTING_ACCOUNT

            isNew.value!! && isContactDuplicate(contactAccountId.value!!)
            -> FormError.DUPLICATED_ACCOUNT

            contactAccountId.value == account.accountId -> FormError.ADDING_YOURSELF

            else -> null
        }
    }

    private fun getMessageResourceId(saveResult: SaveResult): Int {
        return when (saveResult) {
            SaveResult.CREATED -> R.string.new_contact_created
            SaveResult.UPDATED -> R.string.new_contact_updated
        }
    }

    private fun getMessageResourceId(formError: FormError): Int {
        return when (formError) {
            FormError.INVALID_NAME -> R.string.new_contact_invalid_contact_name
            FormError.INVALID_CONTACT_ID -> R.string.new_contact_invalid_contact_account_id
            FormError.NON_EXISTING_ACCOUNT -> R.string.new_contact_non_existing_account_id
            FormError.DUPLICATED_ACCOUNT -> R.string.new_contact_duplicate_contact
            FormError.ADDING_YOURSELF -> R.string.new_contact_you_like_yourself
        }
    }
}