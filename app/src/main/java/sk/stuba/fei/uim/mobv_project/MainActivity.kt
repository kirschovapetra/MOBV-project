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
import org.stellar.sdk.KeyPair
import sk.stuba.fei.uim.mobv_project.data.entities.*
import sk.stuba.fei.uim.mobv_project.data.repositories.*
import sk.stuba.fei.uim.mobv_project.databinding.ActivityMainBinding
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import sk.stuba.fei.uim.mobv_project.api.StellarApi


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

        // lebo kotlin je retardovany (testing purposes)
        // https://stackoverflow.com/questions/6343166/how-can-i-fix-android-os-networkonmainthreadexception
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        apiWithDbTest()

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

        val api = StellarApi.getInstance(this)
        val accRepo = AccountRepository.getInstance(this)
        val balRepo = BalanceRepository.getInstance(this)
        val payRepo = PaymentRepository.getInstance(this)

        lifecycleScope.launch {

            // 1. vygenerujem klucovy par
            val pair = KeyPair.random()
            Log.i("KEYPAIR", "Secret: ${pair.secretSeed}, Public Key: ${pair.accountId}")

            // 2. od friendbota si vypytam 10000 peniazkov
            val resp = api.createStellarAccount(pair.accountId)
            Log.i("CREATE_ACC", resp.toString())

            // 3. syncnem account
            val newAccount = Account(pair.accountId, "King of", "Hell", "666666",
                pair.accountId, null)
            accRepo.syncAccount(newAccount)

            // 4. syncnem balances a payments
            balRepo.syncBalances(newAccount.accountId)
            payRepo.syncPayments(newAccount.accountId)
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

        val bill = Account("123", "Bill", "Gates", "654321",
            "abcabcabc", 112233)
        val jeff = Account("321", "Jeff", "Bezos", "123456",
            "cbacbacba", 445566)

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