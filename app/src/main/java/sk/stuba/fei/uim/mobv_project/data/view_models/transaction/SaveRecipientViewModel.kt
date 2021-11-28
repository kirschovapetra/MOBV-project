package sk.stuba.fei.uim.mobv_project.data.view_models.transaction

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sk.stuba.fei.uim.mobv_project.data.repositories.ContactRepository

class SaveRecipientViewModel: ViewModel() {
    val accountId = MutableLiveData<String>()
    val contactName = MutableLiveData<String>()
}