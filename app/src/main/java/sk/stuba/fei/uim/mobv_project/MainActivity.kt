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
import sk.stuba.fei.uim.mobv_project.data.*
import sk.stuba.fei.uim.mobv_project.data.entities.*
import sk.stuba.fei.uim.mobv_project.data.repositories.*
import sk.stuba.fei.uim.mobv_project.databinding.ActivityMainBinding
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import shadow.com.google.gson.Gson
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
        // ...

        createAccountAndGetInfo()

//        trackAccountDbChanges()
//        dbCheck()
    }

    private fun setupBottomNav() {
        val bottomNavView = binding.bottomNavView
        bottomNavView.setupWithNavController(navController)

        val bottomNavItems = setOf(
            R.id.settingsFragment,R.id.contactsFragment, R.id.myBalanceFragment
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(bottomNavItems.contains(destination.id)) {
                bottomNavView.visibility = View.VISIBLE
            } else {
                bottomNavView.visibility = View.GONE
            }
        }
    }


    /*********************** testiky *********************/


    private fun createAccountAndGetInfo() {

        val api = StellarApi.getInstance(this)
        val accRepo = AccountRepository.getInstance(this)

        // 1. vygenerujem klucovy par
        val pair = KeyPair.random()
        Log.i("KEYPAIR", "Secret: ${pair.secretSeed}, Public Key: ${pair.accountId}")

        // 2. od friendbota si vypytam 10000 peniazkov
        val resp = api.createStellarAccount(pair.accountId)
        Log.i("CREATE_ACC", resp.toString())

        // 3. getnem si data o accounte
        val acc = api.getStellarAccount(pair.accountId)
        Log.i("ACCOUNT_INFO", Gson().toJson(acc))
    }

    // Checkujem ci sa zmenili accounty v db
    private fun trackAccountDbChanges() {
        val owner = this
        val accountRepo = AccountRepository.getInstance(this)

        accountRepo.getAllAccounts().observe(
            owner,
            { accAll ->
                Log.i("TERAZ SA ZMENILI DATA", "Vsetky accounty: $accAll")
                Log.i("TERAZ SA ZMENILI DATA", "pocet accountov: " + accAll.size)
            }
        )
    }

    private fun dbCheck() {
        // TODO check aby asset_code sedel s asset_type

        val accountRepo = AccountRepository.getInstance(this)
        val balanceRepo = BalanceRepository.getInstance(this)
        val paymentRepo = PaymentRepository.getInstance(this)
        val contactRepo = ContactRepository.getInstance(this)

        val bill = Account("123", "Bill", "Gates", "654321",
            "abcabcabc", "112233")
        val jeff = Account("321", "Jeff", "Bezos", "123456",
            "cbacbacba", "445566")

        val accounts = listOf(bill, jeff)
        val contacts = listOf(
            Contact(contactId = jeff.accountId, name = "Bestie", sourceAccount = bill.accountId)
        )
        val balances = listOf(
            Balances(
                assetIssuer = bill.accountId, balance = 10000.0, limit = 100.0,
                assetType = Constants.AssetType.native, sourceAccount = bill.accountId),
            Balances(
                assetCode ="DOGECOIN", assetIssuer = jeff.accountId, balance = 20000.0,
                limit = 200.0, assetType = Constants.AssetType.native, sourceAccount = jeff.accountId),
        )
        val payments = listOf(
            Payment(paymentId = "0", transactionHash =  "QWERTYUIOP", transactionSuccessful = true,
                createdAt = "2021-11-18", assetType = Constants.AssetType.native, assetCode = "DOGECOIN",
                assetIssuer=bill.accountId, from=bill.accountId,to = jeff.accountId,
                amount = 50.0, sourceAccount = bill.accountId)
        )

        val owner = this
        val appName = MainActivity::class.java.simpleName

        lifecycleScope.launch {
            accounts.forEach { accountRepo.insertAccount(it) }
            contacts.forEach { contactRepo.insertContact(it) }
            balances.forEach { balanceRepo.insertBalance(it) }
            payments.forEach { paymentRepo.insertPayment(it) }

            accountRepo.getAllAccounts().observe(
                owner,
                { accAll ->
                    Log.i(appName, "Vsetky accounty: $accAll")
                    Log.i(appName, "pocet accountov: " + accAll.size)
                }
            )

            contactRepo.getAccountContacts(bill.accountId).observe(
                owner,
                { billsContacts ->
                    Log.i(appName, "Billovi bffs: $billsContacts")
                }
            )

            paymentRepo.getAccountPayments(bill.accountId).observe(
                owner,
                { billsPayments ->
                    Log.i(appName, "Billove paymenty: $billsPayments")
                }
            )

            balanceRepo.getAccountBalances(bill.accountId).observe(
                owner,
                { billsBalances ->
                    Log.i(appName, "Billove balances: $billsBalances")
                }
            )

            balanceRepo.getBalancesByAssetCode("DOGECOIN").observe(
                owner,
                { dogecoinBalances ->
                    Log.i(appName, "Dogecoinove balances: $dogecoinBalances")
                }
            )
        }
    }
}