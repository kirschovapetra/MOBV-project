package sk.stuba.fei.uim.mobv_project.data.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import sk.stuba.fei.uim.mobv_project.api.StellarApi
import sk.stuba.fei.uim.mobv_project.data.AppDatabase
import sk.stuba.fei.uim.mobv_project.data.dao.*
import sk.stuba.fei.uim.mobv_project.data.entities.Account

class AccountRepository(
    private val api: StellarApi,
    private val dao: AccountDao,
) : AppDbRepository() {

    companion object {
        const val TAG = "AccountRepository"

        @Volatile
        private var INSTANCE: AccountRepository? = null

        fun getInstance(context: Context): AccountRepository {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    val db = AppDatabase.getInstance(context)
                    val apiInstance = StellarApi.getInstance(context)
                    instance = AccountRepository(apiInstance, db.accountDao)
                    INSTANCE = instance
                }
                return instance
            }
        }

    }

    /********************* DB *********************/

    fun getAllAccounts(): LiveData<List<Account>> = dao.getAll()
    fun getAccountById(id: String): LiveData<Account> = dao.getById(id)

    suspend fun insertAccount(account: Account) {
        dao.insert(account)
    }

    suspend fun updateAccount(account: Account) {
        dao.update(account)
    }

    suspend fun deleteAccount(account: Account) {
        dao.delete(account)
    }

    /********************* API *********************/

    suspend fun syncAccount(account: Account) {
        try {
            val response = api.getStellarAccount(account.accountId)!!
            Log.i("GET_STELLAR_ACCOUNT", "Success accountId=${response.accountId}")

            // asi iba sequence number sa bude z api updatovat
            // pin, kluce atd su stale tie iste a meno + priezvisko mame iba lokalne
            account.sequence = response.sequenceNumber
            dao.insertOrUpdate(account)

            Log.i("SYNC_ACCOUNT", "Success ${account.accountId}")

        } catch (e: java.lang.Exception) {
            Log.e("SYNC_ACCOUNT", e.message!!)
        }
    }

}