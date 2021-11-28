package sk.stuba.fei.uim.mobv_project.data.view_models.contacts

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sk.stuba.fei.uim.mobv_project.data.entities.Contact
import sk.stuba.fei.uim.mobv_project.data.repositories.ContactRepository

class ContactsViewModel(
    val contactRepo: ContactRepository
) : ViewModel() {}