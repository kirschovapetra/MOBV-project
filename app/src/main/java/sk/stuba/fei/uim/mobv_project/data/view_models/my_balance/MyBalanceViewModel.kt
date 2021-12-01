package sk.stuba.fei.uim.mobv_project.data.view_models.my_balance

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sk.stuba.fei.uim.mobv_project.data.entities.Balances
import sk.stuba.fei.uim.mobv_project.data.entities.Payment
import sk.stuba.fei.uim.mobv_project.data.repositories.BalanceRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.ContactRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.PaymentRepository
import sk.stuba.fei.uim.mobv_project.utils.SecurityContext


class MyBalanceViewModel(
    private val balanceRepo: BalanceRepository,
    private val  paymentRepo: PaymentRepository,
    private val contactRepo: ContactRepository) : ViewModel() {

    /*
    z Balances - k menu
     - assetCode
     - balance

    Payments - do recycler view
     - pretty much asi vsetko :D
    */

    var account = SecurityContext.account!!// Account("2", "Jeff", "Bezos", "123456")

    val walletOwner =  MutableLiveData<String>()//mainAccount.firstName + " " + mainAccount.lastName

    var assetOptions = balanceRepo.getAccountAssetCodes(account.accountId)
//    var selectedAssetOption = MutableLiveData<Int>()

    var balanceToShow = MutableLiveData<String?>()
    var selectedPayments = MutableLiveData<List<Payment>>()

    init {
        walletOwner.value = account.firstName + " " + account.lastName
//        Log.e("SELECTED ST", selectedAssetOption.value.toString())
//        paymentRepo.getAllPayments().observeForever { payments ->
//            Log.e("ALLPAYMENTS", payments.toString())
//        }
    }

    private fun fetchContactNameForPayment(payment: Payment): String {
        val associatedContactId = if(payment.paymentType.equals("debit")) payment.from else payment.to

        val attachedContact = contactRepo.getDeadContactByIdAndSourceAccount(associatedContactId!!, account.accountId)

        if(attachedContact.isNotEmpty()) {
            return attachedContact[0].name!!
        }
        return payment.sourceAccount!!
    }

    private fun setPaymentSender(payment: Payment): String {
        return if(payment.paymentType.equals("debit")) payment.from!! else payment.to!!
    }

    private fun isNumeric(string: String): Boolean {
        return string.toIntOrNull() != null
    }

    fun updatePaymentsAndBalance(selectedAsset: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val balance =
                    balanceRepo.getDeadBalancesByAssetCodeAndSourceAccount(selectedAsset, account.accountId)
                val payments =
                    paymentRepo.getDeadAccountPaymentsByAssetCodeAndSourceAccount(selectedAsset, account.accountId)

                payments.forEach {
                    val attachedContactName = fetchContactNameForPayment(it)
                    it.sourceAccount = attachedContactName

                    if(isNumeric(attachedContactName)) {
                        it.from = ""
                    } else {
                        it.from = setPaymentSender(it)
                    }
                }

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