package sk.stuba.fei.uim.mobv_project.data.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sk.stuba.fei.uim.mobv_project.data.entities.Contact

class ContactsViewModel : ViewModel() {

    val contacts = MutableLiveData<ArrayList<Contact>>() // neskor mapa

    val arrayList = ArrayList<Contact>()

    init {
        arrayList.add(Contact("86223", "Jozko", "1"))
        arrayList.add(Contact("86223", "Jozko", "2"))
        arrayList.add(Contact("86223", "Jozko", "3"))
        arrayList.add(Contact("86223", "Jozko", "4"))
        arrayList.add(Contact("86223", "Jozko", "1"))
        arrayList.add(Contact("86223", "Jozko", "2"))
        arrayList.add(Contact("86223", "Jozko", "3"))
        arrayList.add(Contact("86223", "Jozko", "4"))
        arrayList.add(Contact("86223", "Jozko", "1"))
        arrayList.add(Contact("86223", "Jozko", "2"))
        arrayList.add(Contact("86223", "Jozko", "3"))
        arrayList.add(Contact("86223", "Jozko", "4"))
        contacts.postValue(arrayList)
    }
}