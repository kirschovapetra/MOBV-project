package sk.stuba.fei.uim.mobv_project.data.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import sk.stuba.fei.uim.mobv_project.api.StellarApi
import sk.stuba.fei.uim.mobv_project.data.AppDatabase
import sk.stuba.fei.uim.mobv_project.data.utils.Converters
import sk.stuba.fei.uim.mobv_project.data.dao.PaymentDao
import sk.stuba.fei.uim.mobv_project.data.entities.Payment
import sk.stuba.fei.uim.mobv_project.data.exceptions.ApiException
import sk.stuba.fei.uim.mobv_project.data.exceptions.TransactionFailedException
import sk.stuba.fei.uim.mobv_project.data.exceptions.ValidationException
import kotlin.jvm.Throws

class PaymentRepository(
    private val api: StellarApi,
    private val dao: PaymentDao,
) : AppDbRepository() {


    companion object {
        const val TAG = "PaymentRepository"

        @Volatile
        private var INSTANCE: PaymentRepository? = null

        fun getInstance(context: Context): PaymentRepository {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    val db = AppDatabase.getInstance(context)
                    val apiInstance = StellarApi.getInstance(context)
                    instance = PaymentRepository(apiInstance, db.paymentDao)
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    /********************* DB *********************/

    fun getAllPayments(): LiveData<List<Payment>> = dao.getAll()
    fun getPaymentById(id: String): LiveData<Payment> = dao.getById(id)
    fun getAccountPayments(id: String?): LiveData<List<Payment>> = dao.getBySourceAccount(id)

    fun getAccountPaymentsByAssetCode(id: String?, assetCode:String?): LiveData<List<Payment>> = dao.getBySourceAccountAssetCode(id, assetCode)
    fun getAccountPaymentsByAssetCodeAndSourceAccount(assetCode:String, accountId: String): LiveData<List<Payment>> =
        dao.getByAssetCodeAndSourceAccount(assetCode, accountId)

    suspend fun insertPayment(payment: Payment) {
        dao.insert(payment)
    }

    suspend fun updatePayment(payment: Payment) {
        dao.update(payment)
    }

    suspend fun deletePayment(payment: Payment) {
        dao.delete(payment)
    }

    suspend fun clearPayments() {
        dao.clear()
    }

    /********************* API *********************/

    @Throws(ValidationException::class,ApiException::class)
    suspend fun syncPayments(sourceAccount: String) {

        val paymentsResponse = api.getStellarPayments(sourceAccount)
        Log.d(TAG, "syncPayments: payment count ${paymentsResponse.size}")

        paymentsResponse.forEach { payment ->

            val paymentType = when {
                // odoslana platba
                payment.from == sourceAccount -> {"credit"}
                // prijata platba
                payment.to == sourceAccount -> {"debit"}
                else -> {"invalid"}
            }

            dao.insertOrUpdate(
                Payment(
                    paymentId = payment.id,
                    createdAt = payment.createdAt,
                    assetCode = Converters.assetToAssetCode(payment.asset),
                    from = payment.from,
                    to = payment.to,
                    amount = payment.amount,
                    paymentType = paymentType,
                    sourceAccount = sourceAccount
                )
            )
        }

        Log.i(TAG, "syncPayments: Success $sourceAccount")
    }

    @Throws(ValidationException::class, TransactionFailedException::class, ApiException::class)
    suspend fun sendAndSyncPayment(
        sourcePublicKey: String,
        sourcePrivateKey: String,
        destinationPublicKey: String,
        amount: String,
        memo: String = "",
    ) {

        api.sendStellarPayment(sourcePrivateKey, destinationPublicKey, amount, memo)
        syncPayments(sourcePublicKey)
        Log.i(TAG, "sendPayment: Success SRC=$sourcePublicKey DST=$destinationPublicKey")

    }
}