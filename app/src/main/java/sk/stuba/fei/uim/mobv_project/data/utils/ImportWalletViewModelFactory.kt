package sk.stuba.fei.uim.mobv_project.data.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sk.stuba.fei.uim.mobv_project.data.repositories.AccountRepository
import sk.stuba.fei.uim.mobv_project.data.view_models.intro.ImportWalletViewModel
import sk.stuba.fei.uim.mobv_project.ui.intro.ImportWalletFragmentArgs

class ImportWalletViewModelFactory(
    private val accountRepository: AccountRepository,
    private val args: ImportWalletFragmentArgs
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImportWalletFragmentArgs::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ImportWalletViewModel(accountRepository, args) as T
        }
        else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}