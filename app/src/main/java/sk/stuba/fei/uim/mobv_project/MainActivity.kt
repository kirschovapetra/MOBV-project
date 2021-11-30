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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.stellar.sdk.KeyPair
import sk.stuba.fei.uim.mobv_project.data.entities.*
import sk.stuba.fei.uim.mobv_project.data.repositories.*
import sk.stuba.fei.uim.mobv_project.databinding.ActivityMainBinding
import sk.stuba.fei.uim.mobv_project.utils.CipherUtils
import sk.stuba.fei.uim.mobv_project.utils.SecurityContext


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

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment
        ) as NavHostFragment
        val navInflater = navHostFragment.navController.navInflater
        val graph = navInflater.inflate(R.navigation.nav_graph)

        // run once and comment it out to start at My Balance
        // reason: it runs truncate and inserts in a coroutine, so I guess its not
        //          really in sync with query run in main thread
        //createDummyDbData()

        val accountRepository = AccountRepository.getInstance(this)
        val account = accountRepository.getFirstAccount()

        if (account == null) {
            graph.startDestination = R.id.introFragment
        }
        else {
            SecurityContext.account = account
            graph.startDestination = R.id.myBalanceFragment
        }
        navHostFragment.navController.graph = graph

        navController = navHostFragment.navController
        val topNavItems = setOf(
            R.id.introFragment, R.id.settingsFragment, R.id.contactsFragment, R.id.myBalanceFragment
        )
        binding.actionBar.setupWithNavController(navController, AppBarConfiguration(topNavItems))
        setupBottomNav()

//        apiWithDbTest()
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

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val ss =
                    accountRepo.createAndSyncAccount("Severus", "Snape", "1234", KeyPair.random())
                val hp =
                    accountRepo.createAndSyncAccount("Harry", "Potter", "5678", KeyPair.random())

                balanceRepo.syncBalances(ss.accountId)
                balanceRepo.syncBalances(hp.accountId)

                paymentRepo.sendAndSyncPayment(
                    sourcePublicKey = ss.accountId,
                    sourcePrivateKey = CipherUtils.decrypt(ss.privateKey!!,
                        "1234",
                        ss.salt!!,
                        ss.iv!!),
                    destinationPublicKey = hp.accountId,
                    amount = "20",
                    memo = "Mistah Pottah")

                balanceRepo.syncBalances(ss.accountId)
                balanceRepo.syncBalances(hp.accountId)

                paymentRepo.syncPayments(ss.accountId)
                paymentRepo.syncPayments(hp.accountId)
            }
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

        val bill = Account("1", "Bill", "Gates")
        val jeff = Account("2", "Jeff", "Bezos", "123456")
        val elon = Account("3", "Elon", "Musk", "123456")
        val wozniak = Account("4", "Wozi", "Nejaky", "123456")
        val jobs = Account("5", "Steve", "Robotny", "123456")
        val edison = Account("6", "Thomas", "Edison", "123456")
        val newton = Account("7", "Newton", "Bohvieaky", "123456")
        val kirchof = Account("8", "Kirchof", "TiezNeviem", "123456")
        val einstein = Account("9", "Albert", "Einstein", "123456")
        val rockefeler = Account("10", "Rockefeler", "Bezos", "123456")

        val accounts = listOf(bill,jeff,elon,wozniak,jobs,edison,newton,kirchof,einstein,rockefeler)
        val contacts = listOf(
            Contact(contactId = jeff.accountId, name = "Bestie", sourceAccount = bill.accountId),
            Contact(contactId = elon.accountId, name = "Jeffie", sourceAccount = bill.accountId),
            Contact(contactId = wozniak.accountId, name = "Wozi", sourceAccount = bill.accountId),
            Contact(contactId = jobs.accountId, name = "Gauner", sourceAccount = bill.accountId),
            Contact(contactId = edison.accountId, name = "Edie", sourceAccount = bill.accountId),
            Contact(contactId = newton.accountId, name = "Newton", sourceAccount = bill.accountId),
            Contact(contactId = kirchof.accountId, name = "Kirchof", sourceAccount = bill.accountId),
            Contact(contactId = einstein.accountId, name = "Einstein", sourceAccount = bill.accountId),
        )
        val balances = listOf(
            Balances(balance = "10000.0", sourceAccount = bill.accountId),
            Balances(
                assetCode = "DOGECOIN:aabbcc", balance = "20000.0", sourceAccount = jeff.accountId),
            Balances(
                assetCode = "nejakyAssetCode:Issuer", balance = "10000.0", sourceAccount = jeff.accountId),
        )
        val payments = listOf(
            Payment(paymentId = 123, createdAt = "2021-11-18", assetCode = "DOGECOIN:aabbcc", from = bill.accountId,
                to = jeff.accountId, amount = "50.0", sourceAccount = bill.accountId, paymentType = "credit"),
            Payment(paymentId = 456, createdAt = "2021-11-18", assetCode = "nejakyAssetCode:Issuer", from = jeff.accountId,
                to = elon.accountId, amount = "50.0", sourceAccount = jeff.accountId, paymentType = "credit")
        )

        lifecycleScope.launch {
            accountRepo.clearAccounts()
            accounts.forEach { accountRepo.insertAccount(it) }
            contacts.forEach { contactRepo.insertContact(it) }
            balances.forEach { balanceRepo.insertBalance(it) }
            payments.forEach { paymentRepo.insertPayment(it) }
        }
    }
}