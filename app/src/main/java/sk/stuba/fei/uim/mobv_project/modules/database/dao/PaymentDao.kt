package sk.stuba.fei.uim.mobv_project.modules.database.dao;

import androidx.room.*
import sk.stuba.fei.uim.mobv_project.modules.database.entities.Payment;

@Dao
interface PaymentDao : EntityDao<Payment> {

    @Query("SELECT * FROM payment")
    fun getAll(): List<Payment>

    @Query("SELECT * FROM payment WHERE payment_id = :id")
    fun getPaymentById(id: String): Payment

    @Query("SELECT * FROM payment WHERE source_account = :accountId")
    fun getPaymentsBySourceAccount(accountId: String): List<Payment>

    @Query("DELETE FROM payment")
    fun clear()
}
