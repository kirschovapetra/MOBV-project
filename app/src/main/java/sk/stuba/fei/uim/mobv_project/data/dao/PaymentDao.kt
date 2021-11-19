package sk.stuba.fei.uim.mobv_project.data.dao;

import androidx.lifecycle.LiveData
import androidx.room.*
import sk.stuba.fei.uim.mobv_project.data.entities.Payment;

@Dao
interface PaymentDao : EntityDao<Payment> {

    @Query("SELECT * FROM payment")
    fun getAll(): LiveData<List<Payment>>

    @Query("SELECT * FROM payment WHERE payment_id = :id")
    fun getById(id: String): LiveData<Payment>

    @Query("SELECT * FROM payment WHERE source_account = :accountId")
    fun getBySourceAccount(accountId: String): LiveData<List<Payment>>

    @Query("DELETE FROM payment")
    suspend fun clear(): Int
}