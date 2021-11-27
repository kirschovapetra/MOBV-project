package sk.stuba.fei.uim.mobv_project.data.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import sk.stuba.fei.uim.mobv_project.data.AppDatabase
import sk.stuba.fei.uim.mobv_project.data.dao.ContactDao
import sk.stuba.fei.uim.mobv_project.data.entities.Contact

class ContactRepository(private val dao: ContactDao): AppDbRepository() {

    companion object {
        const val TAG = "ContactRepository"
        @Volatile
        private var INSTANCE: ContactRepository? = null

        fun getInstance(context: Context): ContactRepository {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    val db = AppDatabase.getInstance(context)
                    instance = ContactRepository(db.contactDao)
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

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
    suspend fun clearContacts() {
        dao.clear()
    }
}