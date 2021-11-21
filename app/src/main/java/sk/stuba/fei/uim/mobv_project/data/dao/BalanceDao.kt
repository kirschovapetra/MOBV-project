package sk.stuba.fei.uim.mobv_project.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import sk.stuba.fei.uim.mobv_project.data.entities.Balances

@Dao
interface BalanceDao: EntityDao<Balances> {

    @Query("SELECT * FROM balances")
    fun getAll(): LiveData<List<Balances>>

    @Query("SELECT * FROM balances WHERE asset_code = :code")
    fun getByAssetCode(code: String): LiveData<Balances>

    @Query("SELECT * FROM balances WHERE asset_issuer = :issuer")
    fun getByAssetIssuer(issuer: String): LiveData<Balances>

    @Query("SELECT * FROM balances WHERE asset_code = :code AND asset_issuer = :issuer")
    fun getByAssetCodeAndIssuer(code: String, issuer: String): LiveData<Balances>

    @Query("SELECT * FROM balances WHERE source_account = :accountId")
    fun getBySourceAccount(accountId: String): LiveData<List<Balances>>

    @Query("DELETE FROM balances")
    suspend fun clear(): Int

}