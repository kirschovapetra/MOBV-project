package sk.stuba.fei.uim.mobv_project.data.dao

import androidx.room.*
import sk.stuba.fei.uim.mobv_project.data.entities.*

@Dao
interface AccountDao: EntityDao<Account> {

    @Query("SELECT * FROM account")
    suspend fun getAll(): List<Account>

    @Query("SELECT * FROM account WHERE account_id = :id")
    suspend fun getById(id: String): Account

    @Query("DELETE FROM account")
    suspend fun clear(): Int

}
