package sk.stuba.fei.uim.mobv_project.data.view_models.my_balance

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sk.stuba.fei.uim.mobv_project.data.entities.Account
import sk.stuba.fei.uim.mobv_project.data.entities.Payment
import sk.stuba.fei.uim.mobv_project.data.repositories.BalanceRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.PaymentRepository


class MyBalanceViewModel(balanceRepo: BalanceRepository, paymentRepo: PaymentRepository) : ViewModel() {

    /*
    z Balances - k menu
     - assetCode
     - balance

    Payments - do recycler view
     - pretty much asi vsetko :D
    */

    // neskor cez SecurityContext
    var MAIN_ACCOUNT = Account("2", "Jeff", "Bezos", "123456")

    var walletOwner = MAIN_ACCOUNT?.firstName + " " + MAIN_ACCOUNT?.lastName

    // TODO z tychto dvoch asi spravit mapu cez transformers.map ale neviem ako sa to robi :(
    // ale asi ked mapu tak by som getla cele balance, nie iba assetCodes + amount
    var assetOptions = balanceRepo.getAccountAssetCodes(MAIN_ACCOUNT?.accountId) // list assetov - do spinneru
    var balances = balanceRepo.getAccountBalanceAmounts(MAIN_ACCOUNT?.accountId) // list balancov (iba sumy)

    // mutable
    var selectedAsset = MutableLiveData<String>()
    var selectedBalance = MutableLiveData<String>()
    var selectedPayments: LiveData<List<Payment>>

    // export budu asi tie platby ktore patria sourceAccountu a maju assetCode taky aky bol selectnuty
//    var exportPayments: List<Payment> = ArrayList()

    init {

        // toto funguje len in theory, lebo
        // jednak neviem ci sedia indexy balancov s asset kodmi ked sa getnu zvlast
        // a jednak vracia mi to null zase
        // zatial som svacla do accountBalance cely balance list nech to aspon nieco vypisuje
        selectedBalance.value = balances.value?.get(0)
        selectedAsset.value = assetOptions.value?.get(0)
        selectedPayments = paymentRepo.getAccountPaymentsByAssetCode(MAIN_ACCOUNT.accountId, selectedAsset.value)

//        exportPayments =
//            if (selectedPayments.value != null) selectedPayments.value!!
//            else ArrayList()

        Log.i("MyBalanceViewModel", "selectedAsset: ${selectedAsset.value}")
        Log.i("MyBalanceViewModel", "MAIN_ACCOUNT: $MAIN_ACCOUNT")
        Log.i("MyBalanceViewModel", "balances: ${balances.value}")
        Log.i("MyBalanceViewModel", "assetOptions: ${assetOptions.value}")
        Log.i("MyBalanceViewModel", "walletOwner: $walletOwner")
        Log.i("MyBalanceViewModel", "selectedAsset: ${selectedAsset.value}")
        Log.i("MyBalanceViewModel", "selectedPayments: ${selectedPayments.value}")
    }

}