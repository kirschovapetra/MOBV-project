package sk.stuba.fei.uim.mobv_project.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import sk.stuba.fei.uim.mobv_project.data.entities.*

@Dao
abstract class AccountDao : EntityDao<Account>() {

    @Query("SELECT * FROM account")
    abstract fun getAll(): LiveData<List<Account>>

    @Query("SELECT * FROM account WHERE account_id = :id")
    abstract fun getById(id: String): LiveData<Account>

    @Query("DELETE FROM account")
    abstract suspend fun clear()
}
