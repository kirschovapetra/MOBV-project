package sk.stuba.fei.uim.mobv_project.data.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import sk.stuba.fei.uim.mobv_project.data.AppDatabase
import sk.stuba.fei.uim.mobv_project.data.dao.*
import sk.stuba.fei.uim.mobv_project.data.entities.*

class AccountRepository(private val dao: AccountDao): AppDbRepository() {


    companion object {
        const val TAG = "AccountRepository"
        @Volatile
        private var INSTANCE: AccountRepository? = null


        fun getInstance(context: Context): AccountRepository {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    val db = AppDatabase.getInstance(context)
                    instance = AccountRepository(db.accountDao)
                    INSTANCE = instance
                }
                return instance
            }
        }

    }

    fun getAllAccounts(): LiveData<List<Account>> = dao.getAll()
    fun getAccountById(id: String): LiveData<Account> = dao.getById(id)

    suspend fun insertAccount(account: Account){
        dao.insert(account)
    }
    suspend fun updateAccount(account: Account){
        dao.update(account)
    }
    suspend fun deleteAccount(account: Account){
        dao.delete(account)
    }
}