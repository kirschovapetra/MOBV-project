package sk.stuba.fei.uim.mobv_project.data.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import org.stellar.sdk.Asset
import sk.stuba.fei.uim.mobv_project.api.StellarApi
import sk.stuba.fei.uim.mobv_project.data.AppDatabase
import sk.stuba.fei.uim.mobv_project.data.utils.Converters
import sk.stuba.fei.uim.mobv_project.data.dao.BalanceDao
import sk.stuba.fei.uim.mobv_project.data.entities.Balances
import sk.stuba.fei.uim.mobv_project.data.exceptions.ApiException
import sk.stuba.fei.uim.mobv_project.data.exceptions.TransactionFailedException
import sk.stuba.fei.uim.mobv_project.data.exceptions.ValidationException
import kotlin.jvm.Throws

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
    fun getDeadBalancesByAssetCodeAndSourceAccount(assetCode: String, sourceAccount: String): List<Balances> =
        dao.getDeadByAssetCodeAndSourceAccount(assetCode, sourceAccount)

    fun getAccountBalances(id: String): LiveData<List<Balances>> = dao.getBySourceAccount(id)

    fun getAccountAssetCodes(id: String?): LiveData<List<String>> = dao.getAccountAssetCodes(id)
    fun getAccountBalanceAmounts(id: String?): LiveData<List<String>> = dao.getAccountBalanceAmounts(id)

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

    @Throws(ValidationException::class)
    suspend fun syncBalances(sourceAccount: String) {

        // + check ci existuje
        val accountResp = api.getStellarAccount(sourceAccount)

        accountResp.balances?.forEach { balance ->

            dao.insertOrUpdate(
                Balances(
                    assetCode = Converters.assetToAssetCode(balance.asset.orNull()),
                    balance = balance.balance,
                    limit = balance.limit,
                    sourceAccount = accountResp.accountId
                ))
        }
        Log.i(TAG, "syncBalances: Success $sourceAccount")
    }

    @Throws(ValidationException::class, TransactionFailedException::class, ApiException::class)
    suspend fun addAndSyncTrustedAsset(
        accountId: String,
        privateKey: String,
        assetCode: String,
        assetIssuer: String,
        limit: String = "10000",
    ) {

        val asset = Asset.createNonNativeAsset(assetCode, assetIssuer)
        val res = api.changeTrust(asset, accountId, privateKey, limit)

        Log.i(TAG, "addTrustedAsset: Success ${Converters.objectToJson(res)}")

        syncBalances(accountId)

    }
}