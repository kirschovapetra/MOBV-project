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
    fun getAccountButDead(id: String): List<Account> = dao.getByIdButDead(id)

    fun getFirstAccount(): Account? = dao.getFirstAccount()

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

    suspend fun createAndSyncAccount(firstName: String, lastName: String): KeyPair? {

        try {
            // 1. vygenerujem klucovy par
            val pair = KeyPair.random()

            // 2. od friendbota si vypytam 10000 peniazkov
            val resp = api.createStellarAccount(pair.accountId)

            // 3. syncnem account
            dao.insertOrUpdate(
                Account(pair.accountId, firstName, lastName, String(pair.secretSeed))
            )

            Log.i(TAG, "createNewAccount: " +
                            "Success Private key: ${String(pair.secretSeed)}, " +
                            "Public Key: ${pair.accountId}")

            return pair

        } catch (e: Exception) {
            Log.e(TAG, "createNewAccount: ${e.message}")
        }
        return null
    }

    suspend fun syncAccount(accountId: String, privateKey: String,
                            firstName: String,  lastName: String,
    ): Boolean {
        try {
            // check ci kluc dava zmysel
            val keyPair = KeyPair.fromSecretSeed(privateKey)
            val keyPair2 = KeyPair.fromAccountId(accountId)

            if (keyPair.accountId != keyPair2.accountId) {
               throw java.lang.Exception("invalid accountId + privateKey combination")
            }

            // check ci existuje account
            val account = api.getStellarAccount(accountId)

            dao.insertOrUpdate(
                Account(accountId, firstName, lastName, privateKey)
            )
            Log.i(TAG, "syncAccount: Success $accountId")
            return true
        }
        catch (e: Exception){
            Log.e(TAG, "syncAccount: ${e.message}")
            return false
        }

    }

}