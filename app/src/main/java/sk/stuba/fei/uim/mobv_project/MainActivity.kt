package sk.stuba.fei.uim.mobv_project

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import sk.stuba.fei.uim.mobv_project.data.repositories.AccountRepository
import sk.stuba.fei.uim.mobv_project.databinding.ActivityMainBinding
import sk.stuba.fei.uim.mobv_project.utils.SecurityContext


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavView: BottomNavigationView
    private var onGlobalLayoutListenerEnabled = false

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

        navController = navHostFragment.navController
        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.nav_graph)
        // use intro fragment as default nav graph start
        graph.startDestination = R.id.introFragment

        val accountRepository = AccountRepository.getInstance(this)
        val account = accountRepository.getFirstAccount()

        account?.let {
            // replace with MyBalance if account already exists
            SecurityContext.account = it
            graph.startDestination = R.id.myBalanceFragment
        }
        navController.graph = graph

        val topNavItems = setOf(
            R.id.introFragment, R.id.settingsFragment, R.id.contactsFragment, R.id.myBalanceFragment
        )
        binding.actionBar.setupWithNavController(navController, AppBarConfiguration(topNavItems))
        setupBottomNav()
    }

    private fun setupBottomNav() {
        bottomNavView = binding.bottomNavView
        addOnGlobalLayoutListener()

        bottomNavView.setupWithNavController(navController)

        val bottomNavItems = setOf(
            R.id.settingsFragment, R.id.contactsFragment, R.id.myBalanceFragment
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (bottomNavItems.contains(destination.id)) {
                bottomNavView.visibility = View.VISIBLE
                onGlobalLayoutListenerEnabled = true
            } else {
                onGlobalLayoutListenerEnabled = false
                bottomNavView.visibility = View.GONE
            }
        }
    }

    private fun addOnGlobalLayoutListener() {
        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            if (!onGlobalLayoutListenerEnabled) {
                return@addOnGlobalLayoutListener
            }

            val view = binding.root
            val r = Rect()

            view.getWindowVisibleDisplayFrame(r)

            val heightDiff = view.rootView.height - (r.bottom - r.top)
            if (heightDiff > 400) {
                Log.d("hide", heightDiff.toString())
                bottomNavView.visibility = View.GONE
            } else {
                Log.d("show", heightDiff.toString())
                val transition = Fade()
                transition.duration = 100
                transition.addTarget(bottomNavView)

                TransitionManager.beginDelayedTransition(bottomNavView, transition)
                bottomNavView.visibility = View.VISIBLE
            }
        }
    }
}