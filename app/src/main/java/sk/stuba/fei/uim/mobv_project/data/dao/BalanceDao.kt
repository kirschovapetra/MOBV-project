package sk.stuba.fei.uim.mobv_project.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import sk.stuba.fei.uim.mobv_project.data.entities.Balances

@Dao
abstract class BalanceDao : EntityDao<Balances>() {

    @Query("SELECT * FROM balances")
    abstract fun getAll(): LiveData<List<Balances>>

    @Query("SELECT * FROM balances WHERE asset_code = :code")
    abstract fun getByAssetCode(code: String): LiveData<Balances>

    @Query("SELECT * FROM balances WHERE source_account = :accountId")
    abstract fun getBySourceAccount(accountId: String): LiveData<List<Balances>>

    @Query("DELETE FROM balances")
    abstract suspend fun clear(): Int
}