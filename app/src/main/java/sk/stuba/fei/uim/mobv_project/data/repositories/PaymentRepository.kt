package sk.stuba.fei.uim.mobv_project.data.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import sk.stuba.fei.uim.mobv_project.data.AppDatabase
import sk.stuba.fei.uim.mobv_project.data.dao.ContactDao
import sk.stuba.fei.uim.mobv_project.data.dao.PaymentDao
import sk.stuba.fei.uim.mobv_project.data.entities.Payment

class PaymentRepository(private val dao: PaymentDao) {


    companion object {
        const val TAG = "PaymentRepository"
        @Volatile
        private var INSTANCE: PaymentRepository? = null

        fun getInstance(context: Context): PaymentRepository {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    val db = AppDatabase.getInstance(context)
                    instance = PaymentRepository(db.paymentDao)
                    INSTANCE = instance
                }
                return instance
            }
        }
    }


    fun getAllPayments(): LiveData<List<Payment>> = dao.getAll()
    fun getPaymentById(id: String): LiveData<Payment> = dao.getById(id)
    fun getAccountPayments(id: String): LiveData<List<Payment>> = dao.getBySourceAccount(id)

    suspend fun insertPayment(payment: Payment){
        dao.insert(payment)
    }
    suspend fun updatePayment(payment: Payment){
        dao.update(payment)
    }
    suspend fun deletePayment(payment: Payment){
        dao.delete(payment)
    }
}