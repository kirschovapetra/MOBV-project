package sk.stuba.fei.uim.mobv_project.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import sk.stuba.fei.uim.mobv_project.data.entities.Contact

@Dao
abstract class  ContactDao: EntityDao<Contact>()  {

    @Query("SELECT * FROM contact")
    abstract fun getAll(): LiveData<List<Contact>>

    @Query("SELECT * FROM contact WHERE contact_id = :id")
    abstract fun getById(id: String): LiveData<Contact>

    @Query("SELECT * FROM contact WHERE contact_id = :id")
    abstract fun getDeadById(id: String): Contact?

    @Query("SELECT * FROM contact WHERE source_account = :accountId")
    abstract fun getBySourceAccount(accountId: String): LiveData<List<Contact>>

    @Query("SELECT * FROM contact WHERE contact_id = :contactId and source_account = :sourceAccountId")
    abstract fun getByContactIdAndSourceAccount(contactId: String, sourceAccountId: String): List<Contact>

    @Query("DELETE FROM contact")
    abstract suspend fun clear(): Int
}