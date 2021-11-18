package sk.stuba.fei.uim.mobv_project.data.repositories

import sk.stuba.fei.uim.mobv_project.data.dao.BalanceDao
import sk.stuba.fei.uim.mobv_project.data.entities.Balances

class BalanceRepository(private val dao: BalanceDao) {
    suspend fun getAllBalances(): List<Balances> = dao.getAll()
    suspend fun insertBalance(balance: Balances){
        dao.insert(balance)
    }
    suspend fun updateBalance(balance: Balances){
        dao.update(balance)
    }
    suspend fun deleteBalance(balance: Balances){
        dao.delete(balance)
    }
    suspend fun getBalanceById(id: Long){
        dao.getById(id)
    }

    suspend fun getAccountBalances(sourceAccount: String): List<Balances>{
        return dao.getBySourceAccount(sourceAccount)
    }
}