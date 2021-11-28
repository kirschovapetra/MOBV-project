package sk.stuba.fei.uim.mobv_project

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.launch
import sk.stuba.fei.uim.mobv_project.data.entities.*
import sk.stuba.fei.uim.mobv_project.data.repositories.*
import sk.stuba.fei.uim.mobv_project.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //TODO: set navigation start by checking whether pin exists in DB

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment
        ) as NavHostFragment

        navController = navHostFragment.navController
        val topNavItems = setOf(
            R.id.introFragment, R.id.settingsFragment, R.id.contactsFragment, R.id.myBalanceFragment
        )
        binding.actionBar.setupWithNavController(navController, AppBarConfiguration(topNavItems))
        setupBottomNav()

//        apiWithDbTest()
//        createDummyDbData()
        trackDbChanges()

    }

    private fun setupBottomNav() {
        val bottomNavView = binding.bottomNavView
        bottomNavView.setupWithNavController(navController)

        val bottomNavItems = setOf(
            R.id.settingsFragment, R.id.contactsFragment, R.id.myBalanceFragment
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (bottomNavItems.contains(destination.id)) {
                bottomNavView.visibility = View.VISIBLE
            } else {
                bottomNavView.visibility = View.GONE
            }
        }
    }


    /*********************** testiky *********************/


    private fun apiWithDbTest() {

        val accountRepo = AccountRepository.getInstance(this)
        val balanceRepo = BalanceRepository.getInstance(this)
        val paymentRepo = PaymentRepository.getInstance(this)

        GlobalScope.launch {
            val ss = accountRepo.createAndSyncAccount("Severus", "Snape")
            val hp = accountRepo.createAndSyncAccount( "Harry", "Potter")

            if (ss == null || hp == null) return@launch

            balanceRepo.syncBalances(ss.accountId)
            balanceRepo.syncBalances(hp.accountId)

            paymentRepo.sendAndSyncPayment(
                sourcePublicKey = ss.accountId, sourcePrivateKey = String(ss.secretSeed),
                destinationPublicKey = hp.accountId, amount = "20", memo = "Mistah Pottah")

            balanceRepo.syncBalances(ss.accountId)
            balanceRepo.syncBalances(hp.accountId)

            paymentRepo.syncPayments(ss.accountId)
            paymentRepo.syncPayments(hp.accountId)
        }
    }

    // Checkujem ci sa zmenili accounty v db
    private fun trackDbChanges() {
        val owner = this
        val accountRepo = AccountRepository.getInstance(this)
        val balanceRepo = BalanceRepository.getInstance(this)
        val paymentRepo = PaymentRepository.getInstance(this)
        val contactRepo = ContactRepository.getInstance(this)

        val tag = "DB_CHANGE"

        accountRepo.getAllAccounts().observe(
            owner,
            { accAll ->
                Log.i(tag, "Vsetky accounty: $accAll")
            }
        )

        contactRepo.getAllContacts().observe(
            owner,
            { contAll ->
                Log.i(tag, "Vsetky kontakty: $contAll")
            }
        )

        paymentRepo.getAllPayments().observe(
            owner,
            { paymAll ->
                Log.i(tag, "Vsetky paymenty: $paymAll")
            }
        )

        balanceRepo.getAllBalances().observe(
            owner,
            { balAll ->
                Log.i(tag, "Vsetky balances: $balAll")
            }
        )
    }

    private fun createDummyDbData() {
        val accountRepo = AccountRepository.getInstance(this)
        val balanceRepo = BalanceRepository.getInstance(this)
        val paymentRepo = PaymentRepository.getInstance(this)
        val contactRepo = ContactRepository.getInstance(this)

        val bill = Account("123", "Bill", "Gates")
        val jeff = Account("321", "Jeff", "Bezos", "123456")

        val accounts = listOf(bill, jeff)
        val contacts = listOf(
            Contact(contactId = jeff.accountId, name = "Bestie", sourceAccount = bill.accountId)
        )
        val balances = listOf(
            Balances(balance = "10000.0", limit = "100.0", sourceAccount = bill.accountId),
            Balances(
                assetCode = "DOGECOIN:aabbcc", balance = "20000.0", limit = "200.0",
                sourceAccount = jeff.accountId),
        )
        val payments = listOf(
            Payment(paymentId = 123, transactionHash = "QWERTYUIOP", transactionSuccessful = true,
                createdAt = "2021-11-18", assetCode = "DOGECOIN:aabbcc", from = bill.accountId,
                to = jeff.accountId, amount = "50.0", sourceAccount = bill.accountId)
        )

        lifecycleScope.launch {
            accounts.forEach { accountRepo.insertAccount(it) }
            contacts.forEach { contactRepo.insertContact(it) }
            balances.forEach { balanceRepo.insertBalance(it) }
            payments.forEach { paymentRepo.insertPayment(it) }
        }
    }
}