package sk.stuba.fei.uim.mobv_project

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.coroutines.launch
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import sk.stuba.fei.uim.mobv_project.data.*
import sk.stuba.fei.uim.mobv_project.data.entities.*
import sk.stuba.fei.uim.mobv_project.data.repositories.*


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TODO: set navigation start by checking whether pin exists in DB

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment
        ) as NavHostFragment

        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)

        dbCheck()
        testCrash()
    }

    private fun testCrash() {
        var firebaseAnalytics = Firebase.analytics
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
            param(FirebaseAnalytics.Param.ITEM_ID, "test-id")
            param(FirebaseAnalytics.Param.ITEM_NAME, "trest-name")
            param(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
        }

        val crashButton = Button(this)
        crashButton.text = "Test Crash"
        crashButton.setOnClickListener {
            throw RuntimeException("Test Crash") // Force a crash
        }

        addContentView(crashButton, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT))
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
            Log.i(MainActivity::class.java.simpleName, "Vsetky accounty: $accAll")
            Log.i(MainActivity::class.java.simpleName, "Billovi bffs: " + contactRepo.getAccountContacts(bill.accountId))
            Log.i(MainActivity::class.java.simpleName, "Billove paymenty: " + paymentRepo.getAccountPayments(bill.accountId))
            Log.i(MainActivity::class.java.simpleName, "Billove balances: " + balanceRepo.getAccountBalances(bill.accountId))
            Log.i(MainActivity::class.java.simpleName, "Jeffove balances: " + balanceRepo.getAccountBalances(jeff.accountId))

            Log.i(MainActivity::class.java.simpleName, "pocet accountov: " + accAll.size)
        }
    }
}