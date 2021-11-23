package sk.stuba.fei.uim.mobv_project.data.view_models

import android.util.Log
import android.widget.Toast
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewContactViewModel : ViewModel(){
    val isStringEmpty = MutableLiveData<Boolean>()

    var entityId = MutableLiveData<Int>()
    var inputName = MutableLiveData<String>()
    var inputAccountId = MutableLiveData<String>()


    init {
        isStringEmpty.value = false
        inputName.value = "Hextech Vidlicka"
        inputAccountId.value = "84654"
    }

    fun addToContacts(){
        inputAccountId.value?.let { Log.i("LALIHOOO", it) }
        // add to DB and to live data
    }
}