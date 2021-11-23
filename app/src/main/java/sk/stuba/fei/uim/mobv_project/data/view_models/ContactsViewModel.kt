package sk.stuba.fei.uim.mobv_project.data.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sk.stuba.fei.uim.mobv_project.data.entities.Contact

class ContactsViewModel : ViewModel() {

    val contacts = MutableLiveData<ArrayList<Contact>>() // neskor mapa

    val arrayList = ArrayList<Contact>()

    init {
        arrayList.add(Contact("1", "Jozko", "1"))
        arrayList.add(Contact("2", "Betka", "2"))
        arrayList.add(Contact("3", "Dan", "3"))
        arrayList.add(Contact("4", "Johny", "4"))
        arrayList.add(Contact("1", "Soky", "1"))
        arrayList.add(Contact("2", "Martin", "2"))
        arrayList.add(Contact("3", "Marek", "3"))
        arrayList.add(Contact("4", "Zuzka", "4"))
        arrayList.add(Contact("1", "JAROOO", "1"))
        arrayList.add(Contact("2", "Matus", "2"))
        arrayList.add(Contact("3", "Ondo", "3"))
        arrayList.add(Contact("4", "Huto", "4"))
        contacts.postValue(arrayList)
    }
}