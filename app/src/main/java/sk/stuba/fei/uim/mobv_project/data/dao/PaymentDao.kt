package sk.stuba.fei.uim.mobv_project.data.dao;

import androidx.lifecycle.LiveData
import androidx.room.*
import sk.stuba.fei.uim.mobv_project.data.entities.Payment;

@Dao
abstract class  PaymentDao : EntityDao<Payment>() {

    @Query("SELECT * FROM payment")
    abstract fun getAll(): LiveData<List<Payment>>

    @Query("SELECT * FROM payment WHERE payment_id = :id")
    abstract fun getById(id: String): LiveData<Payment>

    @Query("SELECT * FROM payment WHERE source_account = :accountId")
    abstract fun getBySourceAccount(accountId: String): LiveData<List<Payment>>

    @Query("DELETE FROM payment")
    abstract suspend fun clear(): Int
}