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
import sk.stuba.fei.uim.mobv_project.ui.utils.LoadingLayoutUtils
import sk.stuba.fei.uim.mobv_project.ui.utils.NavigationGraphUtils.changeNavGraphStartDestination
import sk.stuba.fei.uim.mobv_project.ui.utils.NotificationUtils


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    private val settingsViewModel: SettingsViewModel by viewModels {
        ViewModelFactory(
            AccountRepository.getInstance(requireContext()),
            BalanceRepository.getInstance(requireContext()),
            ContactRepository.getInstance(requireContext()),
            PaymentRepository.getInstance(requireContext()),
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_settings,
            container,
            false
        )
        binding.settingsViewModel = settingsViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.unlinkButton.setOnClickListener {
            showDialog(requireContext())
        }

        settingsViewModel.eventTransactionSuccessful.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { message ->
                NotificationUtils.showSnackbar(view, message, Snackbar.LENGTH_SHORT)
                LoadingLayoutUtils.setLoadingLayoutVisibility(activity, false)
            }
        })
        settingsViewModel.eventInvalidPin.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { pinInvalid ->
                if (pinInvalid) {
                    NotificationUtils.showSnackbar(view,
                        resources.getString(R.string.intro_pin_invalid_text),
                        Snackbar.LENGTH_SHORT)
                    LoadingLayoutUtils.setLoadingLayoutVisibility(activity, false)
                }
            }
        })
        settingsViewModel.eventApiValidationFailed.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { errorMessage ->
                NotificationUtils.showSnackbar(view, errorMessage, Snackbar.LENGTH_SHORT)
                LoadingLayoutUtils.setLoadingLayoutVisibility(activity, false)
            }
        })
        settingsViewModel.eventLoadingStarted.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {
                LoadingLayoutUtils.setLoadingLayoutVisibility(activity, true)
            }
        })

        return binding.root
    }

    private fun showDialog(context: Context) {
        MaterialAlertDialogBuilder(context, R.style.CustomMaterialAlertDialog)
            .setTitle(R.string.settings_dialog_title)
            .setMessage(R.string.settings_dialog_message)
            .setIcon(R.drawable.ic_baseline_warning_24)
            .setPositiveButton(R.string.settings_dialog_positive) { _, _ ->
                val navController = findNavController()
                navController.navigate(
                    SettingsFragmentDirections.actionAboutFragmentToIntroFragment()
                )
                changeNavGraphStartDestination(this, R.id.introFragment)
                settingsViewModel.clearDatabase()
                NotificationUtils.showSnackbar(view, R.string.settings_snackbar_message, Snackbar.LENGTH_SHORT)
            }
            .setNegativeButton(R.string.settings_dialog_negative) { _, _ -> }
            .show()
    }
}