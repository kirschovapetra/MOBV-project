package sk.stuba.fei.uim.mobv_project.data.view_models.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import sk.stuba.fei.uim.mobv_project.data.entities.Contact
import sk.stuba.fei.uim.mobv_project.data.repositories.ContactRepository

class ContactsViewModel(
    private val contactRepo: ContactRepository
) : ViewModel() {

    val allContacts: LiveData<List<Contact>>
        get() = contactRepo.getAllContacts()
}