package sk.stuba.fei.uim.mobv_project.data.repositories

import androidx.lifecycle.LiveData
import sk.stuba.fei.uim.mobv_project.data.dao.ContactDao
import sk.stuba.fei.uim.mobv_project.data.entities.Contact

class ContactRepository(private val dao: ContactDao) {
    fun getAllContacts(): LiveData<List<Contact>> = dao.getAll()
    fun getContactById(id: String): LiveData<Contact> = dao.getById(id)
    fun getAccountContacts(id: String): LiveData<List<Contact>> = dao.getBySourceAccount(id)

    suspend fun insertContact(contact: Contact){
        dao.insert(contact)
    }
    suspend fun updateContact(contact: Contact){
        dao.update(contact)
    }
    suspend fun deleteContact(contact: Contact){
        dao.delete(contact)
    }
}