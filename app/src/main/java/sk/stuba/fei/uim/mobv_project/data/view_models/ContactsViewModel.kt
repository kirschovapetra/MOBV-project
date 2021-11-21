package sk.stuba.fei.uim.mobv_project.data.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sk.stuba.fei.uim.mobv_project.data.entities.Contact

class ContactsViewModel : ViewModel() {

    val contacts = MutableLiveData<ArrayList<Contact>>() // neskor mapa

    private val arrayList = ArrayList<Contact>()

    init {
        arrayList.add(Contact("86223", "Jozko", "Vajda", "1"))
        contacts.postValue(arrayList)
    }
}