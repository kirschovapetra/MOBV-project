package sk.stuba.fei.uim.mobv_project.data.view_models.my_balance

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sk.stuba.fei.uim.mobv_project.data.entities.Balances
import sk.stuba.fei.uim.mobv_project.data.entities.Payment
import sk.stuba.fei.uim.mobv_project.data.repositories.BalanceRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.PaymentRepository
import sk.stuba.fei.uim.mobv_project.utils.SecurityContext


class MyBalanceViewModel(
    private val balanceRepo: BalanceRepository,
    private val  paymentRepo: PaymentRepository) : ViewModel() {

    /*
    z Balances - k menu
     - assetCode
     - balance

    Payments - do recycler view
     - pretty much asi vsetko :D
    */

    var mainAccount = SecurityContext.account!!// Account("2", "Jeff", "Bezos", "123456")

    val walletOwner =  MutableLiveData<String>()//mainAccount.firstName + " " + mainAccount.lastName

    var assetOptions = balanceRepo.getAccountAssetCodes(mainAccount?.accountId) // list assetov - do spinneru
    var balanceToShow = MutableLiveData<String?>()
    var selectedPayments = MutableLiveData<List<Payment>>()

    init {
        walletOwner.value = mainAccount.firstName + " " + mainAccount.lastName
//        paymentRepo.getAllPayments().observeForever { payments ->
//            Log.e("ALLPAYMENTS", payments.toString())
//        }
    }

    fun updatePaymentsAndBalance(selectedAsset: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val balance =
                    balanceRepo.getDeadBalancesByAssetCodeAndSourceAccount(selectedAsset, mainAccount.accountId) // getDeadBalance
                val payments =
                    paymentRepo.getDeadAccountPaymentsByAssetCodeAndSourceAccount(selectedAsset, mainAccount.accountId)

                selectedPayments.postValue(payments)
                balanceToShow.postValue(parseBalance(balance))
            }
        }
    }

    private fun parseBalance(dbBalance: List<Balances>): String {
        return if(dbBalance.isNotEmpty()) {
            return dbBalance[0].balance!!
        }
        else {"NO BALANCE"}
    }

}