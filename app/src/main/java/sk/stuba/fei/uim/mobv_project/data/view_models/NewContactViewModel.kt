package sk.stuba.fei.uim.mobv_project.data.view_models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewContactViewModel : ViewModel(){
    val isStringEmpty = MutableLiveData<Boolean>()

    var contactId = MutableLiveData<String>()
    var name = MutableLiveData<String>()
    var sourceAccount = MutableLiveData<String>()

    init {
        isStringEmpty.value = false
        name.value = "Hextech Vidlicka"
        sourceAccount.value = "84654"
    }

    fun addToContacts(){
        sourceAccount.value?.let { Log.i("LALIHOOO", it) }
        // add to DB and to live data
    }
}