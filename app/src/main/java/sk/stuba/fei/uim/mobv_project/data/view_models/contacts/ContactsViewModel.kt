package sk.stuba.fei.uim.mobv_project.data.view_models.contacts

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sk.stuba.fei.uim.mobv_project.data.entities.Contact

class ContactsViewModel : ViewModel() {

    var contacts = MutableLiveData<ArrayList<Contact>>()

    var arrayList = ArrayList<Contact>()

    init {
//        arrayList.add(Contact("1", "Jozko", "1"))
//        arrayList.add(Contact("2", "Martin", "2"))
//        arrayList.add(Contact("3", "Marek", "3"))
        contacts.postValue(arrayList)
    }
}