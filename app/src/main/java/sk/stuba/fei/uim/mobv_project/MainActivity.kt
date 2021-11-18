package sk.stuba.fei.uim.mobv_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import sk.stuba.fei.uim.mobv_project.data.*
import sk.stuba.fei.uim.mobv_project.data.entities.*
import sk.stuba.fei.uim.mobv_project.data.repositories.*
import sk.stuba.fei.uim.mobv_project.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbCheck()
    }

    private fun dbCheck() {

        val db = AppDatabase.getInstance(this)

        val accountRepo = AccountRepository(db.accountDao)
        val balanceRepo = BalanceRepository(db.balanceDao)
        val paymentRepo = PaymentRepository(db.paymentDao)
        val contactRepo = ContactRepository(db.contactDao)

        val bill = Account("123", "Bill", "Gates", "654321",
            "abcabcabc", "112233")
        val jeff = Account("321", "Jeff", "Bezos", "123456",
            "cbacbacba", "445566")

        val accounts = listOf(bill, jeff)
        val contacts = listOf(
            Contact(jeff.accountId, "Bestie", "Bezos", bill.accountId)
        )
        val balances = listOf(
            Balances(10000.0, 100.0, Constants.AssetType.native,  bill.accountId),
            Balances(20000.0, 200.0, Constants.AssetType.native,  jeff.accountId),
        )
        val payments = listOf(
            Payment("0", "QWERTYUIOP", true,
                "2021-11-18", Constants.AssetType.native, bill.accountId, jeff.accountId,
                50.0, bill.accountId)
        )

        lifecycleScope.launch {
            accounts.forEach { accountRepo.insertAccount(it) }
            contacts.forEach { contactRepo.insertContact(it) }
            balances.forEach { balanceRepo.insertBalance(it) }
            payments.forEach { paymentRepo.insertPayment(it) }

            val accAll = accountRepo.getAllAccounts()
            println("----------------------------------------------------------------------\n")
            println("Vsetky accounty: $accAll")
            println("Billovi bffs: " + contactRepo.getAccountContacts(bill.accountId))
            println("Billove paymenty: " + paymentRepo.getAccountPayments(bill.accountId))
            println("Billove balances: " + balanceRepo.getAccountBalances(bill.accountId))
            println("Jeffove balances: " + balanceRepo.getAccountBalances(jeff.accountId))

            binding.tvHelloWorld.text = "pocet accountov: " + accAll.size

        }
    }
}