package sk.stuba.fei.uim.mobv_project.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import sk.stuba.fei.uim.mobv_project.R
import sk.stuba.fei.uim.mobv_project.databinding.FragmentContactsBinding
import sk.stuba.fei.uim.mobv_project.databinding.FragmentSettingsBinding
import sk.stuba.fei.uim.mobv_project.ui.contacts.ContactsFragmentDirections

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

//        binding = (FragmentSettingsBinding) DataBindingUtil.inflate(
//            inflater, R.layout.fragment_settings,container,false
//        )

        binding = FragmentSettingsBinding.inflate(inflater)

        binding.unlinkButton.setOnClickListener {
            showDialog()
        }
//
        return binding.root//inflater.inflate(R.layout.fragment_settings, container, false)
    }

    private fun showDialog() {

        MaterialAlertDialogBuilder(requireContext(), R.style.CustomMaterialAlertDialog)
            .setTitle("Are you sure?")
            .setMessage("If you lose your Private Key you won't be able to recover your wallet if you change your device or forget your pin code.")
            .setIcon(R.drawable.ic_baseline_warning_24)

            .setNegativeButton("No, go back") { dialog, _ -> dialog.cancel() }
            .setPositiveButton("Yes, unlink account") { _, _ ->
                findNavController().navigate(
                    SettingsFragmentDirections.actionAboutFragmentToIntroFragment()
                )
                // TODO iba presmerovanie ci nieco speci?
                view?.let {
                    Snackbar.make(it,
                        "You have been successfully logged out",
                        Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
    }
}