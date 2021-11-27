package sk.stuba.fei.uim.mobv_project.data.utils

import androidx.lifecycle.*
import sk.stuba.fei.uim.mobv_project.data.repositories.AccountRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.AppDbRepository
import sk.stuba.fei.uim.mobv_project.data.view_models.contacts.*
import sk.stuba.fei.uim.mobv_project.data.view_models.login.*
import sk.stuba.fei.uim.mobv_project.data.view_models.my_balance.*
import sk.stuba.fei.uim.mobv_project.data.view_models.settings.SettingsViewModel
import sk.stuba.fei.uim.mobv_project.data.view_models.transaction.*

// TODO odkomentovat /*repository*/ ked budeme mat take konstruktory

class ViewModelFactory(private val repository: AppDbRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        // settings
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(repository as AccountRepository) as T
        }

        if (modelClass.isAssignableFrom(ContactsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContactsViewModel(/*repository as ...Repository */) as T
        }

        if (modelClass.isAssignableFrom(NewContactViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewContactViewModel(/*repository as ...Repository */) as T
        }

        if (modelClass.isAssignableFrom(CreatePinViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreatePinViewModel(/*repository as ...Repository  */) as T
        }

        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(/*repository as ...Repository  */) as T
        }

        if (modelClass.isAssignableFrom(MyBalanceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyBalanceViewModel(/*repository as ...Repository  */) as T
        }

        if (modelClass.isAssignableFrom(CreateNewTransactionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateNewTransactionViewModel(/*repository as ...Repository */) as T
        }

        if (modelClass.isAssignableFrom(CreateNewTransactionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateNewTransactionViewModel(/*repository as ...Repository */) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}