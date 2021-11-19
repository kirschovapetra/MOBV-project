package sk.stuba.fei.uim.mobv_project.data.repositories

import androidx.lifecycle.LiveData
import sk.stuba.fei.uim.mobv_project.data.dao.*
import sk.stuba.fei.uim.mobv_project.data.entities.*

class AccountRepository(private val dao: AccountDao) {
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