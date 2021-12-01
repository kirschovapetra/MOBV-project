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

    var account = SecurityContext.account!!

    val walletOwner =  MutableLiveData<String>()

    var assetOptions = balanceRepo.getAccountAssetCodes(account.accountId)

    var balanceToShow = MutableLiveData<String?>()
    var selectedPayments = MutableLiveData<List<Payment>>()

    init {
        walletOwner.value = account.firstName + " " + account.lastName
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

                    if(isNumeric(attachedContactName)) {
                        it.sourceAccount = "Unknown"
                        it.from = ""
                    } else {
                        it.sourceAccount = attachedContactName
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