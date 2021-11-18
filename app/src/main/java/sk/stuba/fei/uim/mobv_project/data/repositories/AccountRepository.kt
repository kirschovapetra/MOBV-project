package sk.stuba.fei.uim.mobv_project.data.repositories

import sk.stuba.fei.uim.mobv_project.data.dao.*
import sk.stuba.fei.uim.mobv_project.data.entities.*

class AccountRepository(private val dao: AccountDao) {
    suspend fun getAllAccounts(): List<Account> = dao.getAll()
    suspend fun insertAccount(account: Account){
        dao.insert(account)
    }
    suspend fun updateAccount(account: Account){
        dao.update(account)
    }
    suspend fun deleteAccount(account: Account){
        dao.delete(account)
    }
    suspend fun getAccountById(id: String){
        dao.getById(id)
    }
}