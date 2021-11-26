package sk.stuba.fei.uim.mobv_project.data.view_models.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sk.stuba.fei.uim.mobv_project.data.entities.Account
import sk.stuba.fei.uim.mobv_project.data.repositories.AccountRepository


class SettingsViewModel(private val accountRepo: AccountRepository) : ViewModel() {

    // Naviazane na Test button v SettingsFragmente - insertne novy accound do db
    fun insertAccountToDb(){
        viewModelScope.launch {
            accountRepo.insertAccount(
                Account("666", "Severus", "Snape", "666666",
                "QWERTYUIOP", "222222")
            )
            accountRepo.insertAccount(
                Account("22", "Taylor", "Swift", "122112",
                    "POIUYTREWQ", "111111")
            )
        }
    }
}