package sk.stuba.fei.uim.mobv_project.data.view_models.settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sk.stuba.fei.uim.mobv_project.data.entities.Account
import sk.stuba.fei.uim.mobv_project.data.repositories.AccountRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.BalanceRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.ContactRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.PaymentRepository


class SettingsViewModel(
    private val accountRepo: AccountRepository,
    private val balanceRepo: BalanceRepository,
    private val contactRepo: ContactRepository,
    private val paymentRepo: PaymentRepository,
) : ViewModel() {

    val accounts = accountRepo.getAllAccounts()

    // Naviazane na Test button v SettingsFragmente - insertne novy accound do db
    fun insertAccountToDb() {
        GlobalScope.launch {
            val ss = accountRepo.createNewAccount("Severus", "Snape")
            val hp = accountRepo.createNewAccount( "Harry", "Potter")

            if (ss == null || hp == null) return@launch

            balanceRepo.syncBalances(ss.accountId)
            balanceRepo.syncBalances(hp.accountId)

            Log.i("BULLSHIT_LOG", "Platba Severus Snape -> Harry Potter")
            paymentRepo.sendPayment(
                sourcePublicKey = ss.accountId, sourcePrivateKey = String(ss.secretSeed),
                destinationPublicKey = hp.accountId, amount = "20", memo = "Mistah Pottah")

            balanceRepo.syncBalances(ss.accountId)
            balanceRepo.syncBalances(hp.accountId)

            paymentRepo.syncPayments(ss.accountId)
            paymentRepo.syncPayments(hp.accountId)

        }
    }

    fun clearDatabase() {
        GlobalScope.launch {
            accountRepo.clearAccounts()
            balanceRepo.clearBalances()
            contactRepo.clearContacts()
            paymentRepo.clearPayments()

            Log.i("CLEAR_DB", "Databaza je vymazana")
        }
    }
}