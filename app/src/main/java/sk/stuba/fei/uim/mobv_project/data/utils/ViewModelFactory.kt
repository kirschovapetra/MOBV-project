package sk.stuba.fei.uim.mobv_project.data.utils

import androidx.lifecycle.*
import sk.stuba.fei.uim.mobv_project.data.repositories.*
import sk.stuba.fei.uim.mobv_project.data.view_models.contacts.*
import sk.stuba.fei.uim.mobv_project.data.view_models.my_balance.*
import sk.stuba.fei.uim.mobv_project.data.view_models.settings.SettingsViewModel
import sk.stuba.fei.uim.mobv_project.data.view_models.transaction.*

// TODO odkomentovat /*repository*/ ked budeme mat take konstruktory

class ViewModelFactory(private vararg val repository: AppDbRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        // settings
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(
                repository[0] as AccountRepository,
                repository[1] as BalanceRepository,
                repository[2] as ContactRepository,
                repository[3] as PaymentRepository) as T
        }

        if (modelClass.isAssignableFrom(ContactsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContactsViewModel(
                repository[0] as ContactRepository
            ) as T
        }

        if (modelClass.isAssignableFrom(NewContactViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewContactViewModel(
                repository[0] as ContactRepository,
                repository[1] as AccountRepository
            ) as T
        }

        if (modelClass.isAssignableFrom(MyBalanceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyBalanceViewModel(
                repository[0] as BalanceRepository,
                repository[1] as PaymentRepository,
                repository[2] as ContactRepository) as T
        }

        if (modelClass.isAssignableFrom(CreateNewTransactionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateNewTransactionViewModel(
                repository[0] as ContactRepository,
                repository[1] as PaymentRepository
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
