package sk.stuba.fei.uim.mobv_project.data.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sk.stuba.fei.uim.mobv_project.data.repositories.AccountRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.BalanceRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.PaymentRepository
import sk.stuba.fei.uim.mobv_project.data.view_models.intro.CreateWalletViewModel
import sk.stuba.fei.uim.mobv_project.ui.intro.CreateWalletFragmentArgs

class CreateWalletViewModelFactory(
    private val accountRepository: AccountRepository,
    private val balanceRepository: BalanceRepository,
    private val paymentRepository: PaymentRepository,
    private val args: CreateWalletFragmentArgs
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateWalletViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateWalletViewModel(
                accountRepository,
                balanceRepository,
                paymentRepository,
                args
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}