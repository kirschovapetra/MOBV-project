package sk.stuba.fei.uim.mobv_project.modules.database.dao

import androidx.room.*
import sk.stuba.fei.uim.mobv_project.modules.database.entities.Account

@Dao
interface AccountDao : EntityDao<Account> {
    @Query("SELECT * FROM account")
    fun getAll(): List<Account>

    @Query("SELECT * FROM account WHERE account_id = :id")
    fun getById(id: String): Account

    @Query("DELETE FROM account")
    fun clear()
}
