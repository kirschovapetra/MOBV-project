package sk.stuba.fei.uim.mobv_project.data.view_models.intro

import androidx.lifecycle.ViewModel
import sk.stuba.fei.uim.mobv_project.data.repositories.AccountRepository
import sk.stuba.fei.uim.mobv_project.ui.intro.ImportWalletFragmentArgs

class ImportWalletViewModel(
    private val accountRepository: AccountRepository,
    private val importWalletFragmentArgs: ImportWalletFragmentArgs
): ViewModel() {
}