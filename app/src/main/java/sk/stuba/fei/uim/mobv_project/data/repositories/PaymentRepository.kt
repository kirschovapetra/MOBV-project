package sk.stuba.fei.uim.mobv_project.data.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import sk.stuba.fei.uim.mobv_project.api.StellarApi
import sk.stuba.fei.uim.mobv_project.data.AppDatabase
import sk.stuba.fei.uim.mobv_project.data.Converters
import sk.stuba.fei.uim.mobv_project.data.dao.PaymentDao
import sk.stuba.fei.uim.mobv_project.data.entities.Payment

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
    fun getAccountPayments(id: String): LiveData<List<Payment>> = dao.getBySourceAccount(id)

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

    suspend fun syncPayments(sourceAccount: String): Boolean {
        try {
            val paymentsResponse = api.getStellarPayments(sourceAccount)

            paymentsResponse?.forEach { payment ->

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
                        transactionHash = payment.transactionHash,
                        transactionSuccessful = payment.isTransactionSuccessful,
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
            return true

        } catch (e: java.lang.Exception) {
            Log.e(TAG, "syncPayments: ${e.message}")
            return false
        }

    }

    suspend fun sendAndSyncPayment(
        sourcePublicKey: String,
        sourcePrivateKey: String,
        destinationPublicKey: String,
        assetCode: String = "Lumens",
        assetIssuer: String = "",
        amount: String,
        memo: String = "",
    ): Boolean {
        try {
            val transactionResponse = api.sendStellarTransaction(
                sourcePrivateKey, destinationPublicKey, assetCode, assetIssuer, amount, memo)
            syncPayments(sourcePublicKey)
            Log.i(TAG, "sendPayment: Success SRC=$sourcePublicKey DST=$destinationPublicKey")
            return true
        } catch (e: Exception) {
            Log.e(TAG, "sendPayment: ${e.message}")
            return false
        }
    }
}