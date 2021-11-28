package sk.stuba.fei.uim.mobv_project.data.view_models.contacts

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sk.stuba.fei.uim.mobv_project.data.entities.Contact
import sk.stuba.fei.uim.mobv_project.data.repositories.AccountRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.ContactRepository

class NewContactViewModel(
    val contactRepo: ContactRepository,
    val accountRepo: AccountRepository
) : ViewModel(){

    var contact = MutableLiveData<Contact>()
    var isNew = MutableLiveData<Boolean>()
}