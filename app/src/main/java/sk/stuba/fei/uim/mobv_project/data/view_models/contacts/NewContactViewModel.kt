package sk.stuba.fei.uim.mobv_project.data.view_models.contacts

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewContactViewModel : ViewModel(){
    var contactId = MutableLiveData<String>()
    var name = MutableLiveData<String>()
    var sourceAccount = MutableLiveData<String>()

//    init {
//        name.value = "Hextech Vidlicka"
//        sourceAccount.value = "84654"
//    }

    fun addToContacts(){
        sourceAccount.value?.let { Log.i("LALIHOOO", it) }
        // add to DB and to live data
    }
}