package sk.stuba.fei.uim.mobv_project.data.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import sk.stuba.fei.uim.mobv_project.data.AppDatabase
import sk.stuba.fei.uim.mobv_project.data.dao.AccountDao
import sk.stuba.fei.uim.mobv_project.data.dao.BalanceDao
import sk.stuba.fei.uim.mobv_project.data.entities.Balances

class BalanceRepository(private val dao: BalanceDao) {

    companion object {
        const val TAG = "BalanceRepository"
        @Volatile
        private var INSTANCE: BalanceRepository? = null

        fun getInstance(context: Context): BalanceRepository {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    val db = AppDatabase.getInstance(context)
                    instance = BalanceRepository(db.balanceDao)
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    fun getAllBalances(): LiveData<List<Balances>> = dao.getAll()
    fun getBalancesByAssetCode(code: String): LiveData<Balances>  = dao.getByAssetCode(code)
    fun getBalancesByAssetIssuer(issuer: String): LiveData<Balances>  = dao.getByAssetIssuer(issuer)
    fun getBalancesByAssetCodeAndIssuer(code: String, issuer: String): LiveData<Balances>  = dao.getByAssetCodeAndIssuer(code, issuer)
    fun getAccountBalances(id: String): LiveData<List<Balances>> = dao.getBySourceAccount(id)

    suspend fun insertBalance(balance: Balances){
        dao.insert(balance)
    }
    suspend fun updateBalance(balance: Balances){
        dao.update(balance)
    }
    suspend fun deleteBalance(balance: Balances){
        dao.delete(balance)
    }
}