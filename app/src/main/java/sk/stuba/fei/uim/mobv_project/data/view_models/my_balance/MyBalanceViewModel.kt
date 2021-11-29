package sk.stuba.fei.uim.mobv_project.data.view_models.my_balance

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sk.stuba.fei.uim.mobv_project.data.entities.Balances

class MyBalanceViewModel : ViewModel() {

    var balances = MutableLiveData<ArrayList<Balances>>()

    var walletOwner = MutableLiveData<String>()
    var accountId = MutableLiveData<String>()
    var accountBalance = MutableLiveData<String>()


    var tokenOptions = MutableLiveData<MutableList<String>>()

    var selectedToken = MutableLiveData<String>()
//    var tokenOptions = listOf<String>("86223","86823","90223")
//    var accountBalance = MutableLiveData<String>()

    var exportBalances = ArrayList<Balances>()


    var tmpOptions = MutableLiveData<MutableList<String>>()


    init {
        exportBalances.add(Balances("1", Double.MAX_VALUE.toString(),
            Double.MAX_VALUE.toString(),"84654"))
        balances.postValue(exportBalances)

        tmpOptions.postValue(mutableListOf("8923"))
    }
}