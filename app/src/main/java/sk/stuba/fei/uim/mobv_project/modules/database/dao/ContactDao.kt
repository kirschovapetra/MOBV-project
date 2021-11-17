package sk.stuba.fei.uim.mobv_project.modules.database.dao

import androidx.room.*
import sk.stuba.fei.uim.mobv_project.modules.database.entities.Contact

@Dao
interface ContactDao : EntityDao<Contact> {

    @Query("SELECT * FROM contact")
    fun getAll(): List<Contact>

    @Query("SELECT * FROM contact WHERE contact_id = :id")
    fun getContactById(id: String): Contact

    @Query("SELECT * FROM contact WHERE source_account = :accountId")
    fun getContactsBySourceAccount(accountId: String): List<Contact>

    @Query("DELETE FROM contact")
    fun clear()
}