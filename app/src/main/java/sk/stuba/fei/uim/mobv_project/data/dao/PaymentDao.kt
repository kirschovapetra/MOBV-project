package sk.stuba.fei.uim.mobv_project.data.dao;

import androidx.room.*
import sk.stuba.fei.uim.mobv_project.data.entities.Payment;

@Dao
interface PaymentDao : EntityDao<Payment> {

    @Query("SELECT * FROM payment")
    suspend fun getAll(): List<Payment>

    @Query("SELECT * FROM payment WHERE payment_id = :id")
    suspend fun getById(id: String): Payment

    @Query("SELECT * FROM payment WHERE source_account = :accountId")
    suspend fun getBySourceAccount(accountId: String): List<Payment>

    @Query("DELETE FROM payment")
    suspend fun clear()
}