package sk.stuba.fei.uim.mobv_project.modules.database.dao

import androidx.room.*
import sk.stuba.fei.uim.mobv_project.modules.database.entities.BalanceHistory

@Dao
interface BalanceHistoryDao: EntityDao<BalanceHistory>{

    @Query("SELECT * FROM balance_history")
    fun getAll(): List<BalanceHistory>

    @Query("SELECT * FROM balance_history WHERE balance_id = :id")
    fun getBalanceById(id: String): BalanceHistory

    @Query("SELECT * FROM balance_history WHERE source_account = :accountId")
    fun getBalancesBySourceAccount(accountId: String): List<BalanceHistory>

    @Query("DELETE FROM balance_history")
    fun clear()

}
