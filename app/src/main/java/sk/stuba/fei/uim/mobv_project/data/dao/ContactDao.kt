package sk.stuba.fei.uim.mobv_project.data.dao

import androidx.room.*
import sk.stuba.fei.uim.mobv_project.data.entities.Contact

@Dao
interface ContactDao: EntityDao<Contact>  {

    @Query("SELECT * FROM contact")
    suspend fun getAll(): List<Contact>

    @Query("SELECT * FROM contact WHERE contact_id = :id")
    suspend fun getById(id: String): Contact

    @Query("SELECT * FROM contact WHERE source_account = :accountId")
    suspend fun getBySourceAccount(accountId: String): List<Contact>

    @Query("DELETE FROM contact")
     suspend fun clear()
}