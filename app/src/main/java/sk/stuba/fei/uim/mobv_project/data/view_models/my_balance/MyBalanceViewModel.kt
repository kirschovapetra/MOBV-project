package sk.stuba.fei.uim.mobv_project.data.view_models.my_balance

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sk.stuba.fei.uim.mobv_project.data.Constants
import sk.stuba.fei.uim.mobv_project.data.entities.Balances

class MyBalanceViewModel : ViewModel() {

    var balances = MutableLiveData<ArrayList<Balances>>()

    var exportBalances = ArrayList<Balances>()

    init {
        exportBalances.add(Balances("1", Double.MAX_VALUE.toString(),
            Double.MAX_VALUE.toString(),"84654"))
        balances.postValue(exportBalances)
    }
}