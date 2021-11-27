package sk.stuba.fei.uim.mobv_project.data.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import org.stellar.sdk.KeyPair
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

    suspend fun clearAccounts() {
        dao.clear()
    }

    /********************* API *********************/

    suspend fun createNewAccount(firstName: String, lastName: String): KeyPair? {

        try {
            // 1. vygenerujem klucovy par
            val pair = KeyPair.random()
            Log.i("CREATE_ACCOUNT",
                "Keypair = Secret: ${String(pair.secretSeed)}, Public Key: ${pair.accountId}")

            // 2. od friendbota si vypytam 10000 peniazkov
            val resp = api.createStellarAccount(pair.accountId)

            // 3. syncnem account
            dao.insertOrUpdate(
                Account(pair.accountId, firstName, lastName, String(pair.secretSeed))
            )

            Log.i("CREATE_ACCOUNT", "Success ${pair.accountId}")
            return pair

        } catch (e: java.lang.Exception) {
            Log.e("CREATE_ACCOUNT", e.message!!)
        }
        return null
    }

}