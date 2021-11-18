package sk.stuba.fei.uim.mobv_project.data.repositories

import sk.stuba.fei.uim.mobv_project.data.dao.PaymentDao
import sk.stuba.fei.uim.mobv_project.data.entities.Payment

class PaymentRepository(private val dao: PaymentDao) {
    suspend fun getAllPayments(): List<Payment> = dao.getAll()
    suspend fun insertPayment(payment: Payment){
        dao.insert(payment)
    }
    suspend fun updatePayment(payment: Payment){
        dao.update(payment)
    }
    suspend fun deletePayment(payment: Payment){
        dao.delete(payment)
    }
    suspend fun getPaymentById(id: String) {
        dao.getById(id)
    }

    suspend fun getAccountPayments(sourceAccount: String): List<Payment>{
        return dao.getBySourceAccount(sourceAccount)
    }
}