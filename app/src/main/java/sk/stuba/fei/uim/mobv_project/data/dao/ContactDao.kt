package sk.stuba.fei.uim.mobv_project.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import sk.stuba.fei.uim.mobv_project.data.entities.Contact

@Dao
interface ContactDao: EntityDao<Contact>  {

    @Query("SELECT * FROM contact")
    fun getAll(): LiveData<List<Contact>>

    @Query("SELECT * FROM contact WHERE contact_id = :id")
    fun getById(id: String): LiveData<Contact>

    @Query("SELECT * FROM contact WHERE source_account = :accountId")
    fun getBySourceAccount(accountId: String): LiveData<List<Contact>>

    @Query("DELETE FROM contact")
     suspend fun clear(): Int
}