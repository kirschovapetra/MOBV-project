package sk.stuba.fei.uim.mobv_project.data.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import sk.stuba.fei.uim.mobv_project.api.StellarApi
import sk.stuba.fei.uim.mobv_project.data.AppDatabase
import sk.stuba.fei.uim.mobv_project.data.Converters
import sk.stuba.fei.uim.mobv_project.data.dao.BalanceDao
import sk.stuba.fei.uim.mobv_project.data.entities.Balances

class BalanceRepository(
    private val api: StellarApi,
    private val dao: BalanceDao,
) : AppDbRepository() {

    companion object {
        const val TAG = "BalanceRepository"

        @Volatile
        private var INSTANCE: BalanceRepository? = null

        fun getInstance(context: Context): BalanceRepository {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    val db = AppDatabase.getInstance(context)
                    val apiInstance = StellarApi.getInstance(context)
                    instance = BalanceRepository(apiInstance, db.balanceDao)
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    /********************* DB *********************/

    fun getAllBalances(): LiveData<List<Balances>> = dao.getAll()
    fun getBalancesByAssetCode(code: String): LiveData<Balances> = dao.getByAssetCode(code)
    fun getAccountBalances(id: String): LiveData<List<Balances>> = dao.getBySourceAccount(id)

    suspend fun insertBalance(balance: Balances) {
        dao.insert(balance)
    }

    suspend fun updateBalance(balance: Balances) {
        dao.update(balance)
    }

    suspend fun deleteBalance(balance: Balances) {
        dao.delete(balance)
    }
    suspend fun clearBalances() {
        dao.clear()
    }

    /********************* API *********************/

    suspend fun syncBalances(sourceAccount: String) {
        try {
            val response = api.getStellarAccount(sourceAccount)!!
            response.balances.forEach { balance ->

                dao.insertOrUpdate(
                    Balances(
                        assetCode = Converters.assetToAssetCode(balance.asset.orNull()),
                        balance = balance.balance,
                        limit = balance.limit,
                        sourceAccount = sourceAccount
                    ))
            }
            Log.i("UPDATE_BALANCES", "Success $sourceAccount")

        } catch (e: java.lang.Exception) {
            Log.e("UPDATE_BALANCES", e.message!!)
        }
    }
}