package sk.stuba.fei.uim.mobv_project.data.view_models.contacts

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sk.stuba.fei.uim.mobv_project.data.entities.Contact

class NewContactViewModel : ViewModel(){

    var contact = MutableLiveData<Contact>()

//    init {
//        contact.value = Contact()
//        name.value = "Hextech Vidlicka"
//        sourceAccount.value = "84654"
//    }

    fun addToContacts(){
        contact.value?.let {
            val contactVal = contact.value!!
            Log.i("Serus", contact.value.toString())

            // insert/update do db asi takto nejak
//            if (contact.value.contactId == "" || contact.value.name == "")
//                return@let
//            contactRepo.insertContact(contact.value)
//            contactRepo.updateContact(contact.value)

        }
        // add to DB and to live data
    }
}