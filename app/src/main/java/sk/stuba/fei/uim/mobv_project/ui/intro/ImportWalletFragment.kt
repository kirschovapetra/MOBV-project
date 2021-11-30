package sk.stuba.fei.uim.mobv_project.ui.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import sk.stuba.fei.uim.mobv_project.R
import sk.stuba.fei.uim.mobv_project.data.repositories.AccountRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.BalanceRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.PaymentRepository
import sk.stuba.fei.uim.mobv_project.data.utils.ImportWalletViewModelFactory
import sk.stuba.fei.uim.mobv_project.data.view_models.intro.ImportWalletViewModel
import sk.stuba.fei.uim.mobv_project.databinding.FragmentImportWalletBinding
import sk.stuba.fei.uim.mobv_project.ui.intro.ImportWalletFragmentDirections.actionImportWalletFragmentToMyBalanceFragment
import sk.stuba.fei.uim.mobv_project.ui.utils.NotificationUtils.showSnackbarFromMessageEvent
import sk.stuba.fei.uim.mobv_project.ui.utils.NotificationUtils.showSnackbarFromResourceEvent

class ImportWalletFragment : Fragment(R.layout.fragment_import_wallet) {

    private val args: ImportWalletFragmentArgs by navArgs()
    private val viewModel: ImportWalletViewModel by viewModels {
        ImportWalletViewModelFactory(
            AccountRepository.getInstance(context!!),
            PaymentRepository.getInstance(context!!),
            BalanceRepository.getInstance(context!!),
            args
        )
    }
    private lateinit var binding: FragmentImportWalletBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_import_wallet,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.eventFormError.observe(this, { event ->
            showSnackbarFromResourceEvent(view, event, Snackbar.LENGTH_LONG)
        })
        viewModel.eventRepositoryValidationError.observe(this, { event ->
            showSnackbarFromMessageEvent(view, event, Snackbar.LENGTH_LONG)
        })
        viewModel.eventLocalAccountCreated.observe(this, { event ->
            event.getContentIfNotHandled()?.let {
                if (it) {
                    findNavController().navigate(actionImportWalletFragmentToMyBalanceFragment())
                }
            }
        })

        return binding.root
    }
}