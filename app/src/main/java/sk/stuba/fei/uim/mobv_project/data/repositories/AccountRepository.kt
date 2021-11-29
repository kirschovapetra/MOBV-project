package sk.stuba.fei.uim.mobv_project.data.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import org.stellar.sdk.KeyPair
import sk.stuba.fei.uim.mobv_project.api.StellarApi
import sk.stuba.fei.uim.mobv_project.data.AppDatabase
import sk.stuba.fei.uim.mobv_project.data.dao.AccountDao
import sk.stuba.fei.uim.mobv_project.data.entities.Account
import sk.stuba.fei.uim.mobv_project.data.exceptions.TransactionFailedException
import sk.stuba.fei.uim.mobv_project.data.exceptions.ValidationException
import sk.stuba.fei.uim.mobv_project.data.utils.Validation
import kotlin.jvm.Throws
import sk.stuba.fei.uim.mobv_project.utils.CipherUtils

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

    @Throws(TransactionFailedException::class)
    suspend fun createAndSyncAccount(firstName: String, lastName: String, pin: String, pair: KeyPair): Account {

        // 1. od friendbota si vypytam 10000 peniazkov
        val resp = api.createStellarAccount(pair.accountId)

        // 2. syncnem account
        val salt = CipherUtils.getSalt()
        val iv = CipherUtils.getIv()
        val encryptedSecretSeed = CipherUtils.encrypt(String(pair.secretSeed), pin, salt, iv)

        val account = Account(pair.accountId, firstName, lastName, encryptedSecretSeed, salt, iv)
        dao.insertOrUpdate(
            account
        )

        Log.i(TAG, "createNewAccount: " +
                "Success Private key: ${String(pair.secretSeed)}, " +
                "Public Key: ${pair.accountId}")

        return account
    }

    @Throws(ValidationException::class)
    suspend fun syncAccount(
        accountId: String, privateKey: String,
        firstName: String, lastName: String,
    ) {

        // check ci kluce matchuju k rovnakemu accountu
        val kp = Validation.doKeysMatch(accountId, privateKey)

        // check ci existuje account
        val acc = api.getStellarAccount(kp.accountId)

        dao.insertOrUpdate(
            Account(acc.accountId, firstName, lastName, String(kp.secretSeed))
        )
        Log.i(TAG, "syncAccount: Success ${acc.accountId}")


    }

}