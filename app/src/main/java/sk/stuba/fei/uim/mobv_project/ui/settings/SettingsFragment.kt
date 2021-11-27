package sk.stuba.fei.uim.mobv_project.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import sk.stuba.fei.uim.mobv_project.R
import sk.stuba.fei.uim.mobv_project.data.repositories.AccountRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.BalanceRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.ContactRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.PaymentRepository
import sk.stuba.fei.uim.mobv_project.data.utils.ViewModelFactory
import sk.stuba.fei.uim.mobv_project.data.view_models.settings.SettingsViewModel
import sk.stuba.fei.uim.mobv_project.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    private val settingsViewModel: SettingsViewModel by viewModels {
        ViewModelFactory(
            AccountRepository.getInstance(context!!),
            BalanceRepository.getInstance(context!!),
            ContactRepository.getInstance(context!!),
            PaymentRepository.getInstance(context!!),
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_settings,
            container,
            false
        )
        binding.settingsViewModel = settingsViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.unlinkButton.setOnClickListener {
            showDialog(context!!)
        }

        return binding.root
    }

    private fun showDialog(context: Context) {
        MaterialAlertDialogBuilder(context, R.style.CustomMaterialAlertDialog)
            .setTitle("Are you sure?")
            .setMessage("If you lose your Private Key you won't be able to recover your wallet if you change your device or forget your pin code.")
            .setIcon(R.drawable.ic_baseline_warning_24)
            .setPositiveButton("Yes, unlink account") { _, _ ->
                findNavController().navigate(
                    SettingsFragmentDirections.actionAboutFragmentToIntroFragment()
                )
                settingsViewModel.clearDatabase()
                view?.let {
                    Snackbar.make(it,
                        "You have been successfully logged out",
                        Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
            .setNegativeButton("No, go back") { _, _ -> }
            .show()
    }
}