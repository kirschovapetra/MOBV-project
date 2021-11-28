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

//        ked sa da volanie api do GlobalScope tak je vsetko zahojene
//        val policy = ThreadPolicy.Builder().permitAll().build()
//        StrictMode.setThreadPolicy(policy)

//        apiWithDbTest()

        createDummyDbData()
//        trackDbChanges()

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
                Log.i("$tag ACC", "Vsetky accounty: $accAll")
            }
        )

        contactRepo.getAllContacts().observe(
            owner,
            { contAll ->
                Log.i("$tag CONT", "Vsetky kontakty: $contAll")
            }
        )

        paymentRepo.getAllPayments().observe(
            owner,
            { paymAll ->
                Log.i("$tag PAYM", "Vsetky paymenty: $paymAll")
            }
        )

        balanceRepo.getAllBalances().observe(
            owner,
            { balAll ->
                Log.i("$tag BAL", "Vsetky balances: $balAll")
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
            accountRepo.clearAccounts()
            accounts.forEach { accountRepo.insertAccount(it) }
            contacts.forEach { contactRepo.insertContact(it) }
            balances.forEach { balanceRepo.insertBalance(it) }
            payments.forEach { paymentRepo.insertPayment(it) }
        }
    }
}