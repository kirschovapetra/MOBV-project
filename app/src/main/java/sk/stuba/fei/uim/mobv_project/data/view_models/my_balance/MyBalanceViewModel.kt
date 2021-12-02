package sk.stuba.fei.uim.mobv_project.data.view_models.my_balance

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sk.stuba.fei.uim.mobv_project.data.entities.Payment
import sk.stuba.fei.uim.mobv_project.data.repositories.BalanceRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.ContactRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.PaymentRepository
import sk.stuba.fei.uim.mobv_project.utils.SecurityContext


class MyBalanceViewModel(
    private val balanceRepo: BalanceRepository,
    private val paymentRepo: PaymentRepository,
    private val contactRepo: ContactRepository,
) : ViewModel() {

    val account = SecurityContext.account!!
    val walletOwner = "${account.firstName} ${account.lastName}"

    val assetOptions: LiveData<List<String>>
        get() = balanceRepo.getAccountAssetCodes(account.accountId)

    val selectedAsset = MutableLiveData<String>()

    val balance: LiveData<String>
        get() = Transformations.switchMap(selectedAsset) { selectedAsset ->
            val accountBalance = balanceRepo.getBalanceByAssetCodeAndSourceAccount(selectedAsset, account.accountId)

            Transformations.map(accountBalance) {
                it.balance
            }
        }

    val paymentsToShow: LiveData<List<Payment>>
        get() = Transformations.switchMap(selectedAsset) { selectedAsset ->
            val accountPayments = paymentRepo.getAccountPaymentsByAssetCodeAndSourceAccount(
                selectedAsset,
                account.accountId
            )

            Transformations.map(accountPayments) {
                it.map { payment ->
                    // TODO: asi by bolo lepsie db view, ale who cares at this point
                    val attachedContactName = fetchContactNameForPayment(payment)
                    payment.sourceAccount = attachedContactName
                    payment.from = setPaymentSender(payment)
                    payment
                }
            }
        }

    init {
        syncPaymentsAndBalances()
    }

    private fun syncPaymentsAndBalances() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    paymentRepo.syncPayments(account.accountId)
                    balanceRepo.syncBalances(account.accountId)
                } catch (e: Exception) {
                    Log.e("MyBalanceViewModel", "${e.message}")
                }
            }
        }
    }

    private fun fetchContactNameForPayment(payment: Payment): String {
        val associatedContactId =
            if ("debit" == payment.paymentType) payment.from else payment.to

        val attachedContact =
            contactRepo.getDeadContactByIdAndSourceAccount(associatedContactId!!, account.accountId)

        return if (attachedContact.isNotEmpty() && !attachedContact[0].name.isNullOrEmpty()) {
            attachedContact[0].name!!
        } else {
            "Unknown"
        }
    }

    private fun setPaymentSender(payment: Payment): String {
        return if ("debit" == payment.paymentType) payment.from.toString() else payment.to.toString()
    }

}