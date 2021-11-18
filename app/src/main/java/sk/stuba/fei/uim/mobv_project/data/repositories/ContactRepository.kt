package sk.stuba.fei.uim.mobv_project.data.repositories

import sk.stuba.fei.uim.mobv_project.data.dao.ContactDao
import sk.stuba.fei.uim.mobv_project.data.entities.Contact

class ContactRepository(private val dao: ContactDao) {
    suspend fun getAllContacts(): List<Contact> = dao.getAll()
    suspend fun insertContact(contact: Contact){
        dao.insert(contact)
    }
    suspend fun updateContact(contact: Contact){
        dao.update(contact)
    }
    suspend fun deleteContact(contact: Contact){
        dao.delete(contact)
    }
    suspend fun getContactById(id: String){
        dao.getById(id)
    }

    suspend fun getAccountContacts(sourceAccount: String): List<Contact>{
        return dao.getBySourceAccount(sourceAccount)
    }

}