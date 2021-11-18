package sk.stuba.fei.uim.mobv_project.data.dao

import androidx.room.*
import sk.stuba.fei.uim.mobv_project.data.entities.Balances

@Dao
interface BalanceDao: EntityDao<Balances> {

    @Query("SELECT * FROM balances")
    fun getAll(): List<Balances>

    @Query("SELECT * FROM balances WHERE balance_id = :id")
    fun getById(id: Long): Balances

    @Query("SELECT * FROM balances WHERE source_account = :accountId")
    fun getBySourceAccount(accountId: String): List<Balances>

    @Query("DELETE FROM balances")
    suspend fun clear()

}