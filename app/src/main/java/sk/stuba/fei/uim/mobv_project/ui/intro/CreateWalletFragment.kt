package sk.stuba.fei.uim.mobv_project.ui.intro

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import sk.stuba.fei.uim.mobv_project.R
import sk.stuba.fei.uim.mobv_project.data.repositories.AccountRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.BalanceRepository
import sk.stuba.fei.uim.mobv_project.data.utils.CreateWalletViewModelFactory
import sk.stuba.fei.uim.mobv_project.data.view_models.intro.CreateWalletViewModel
import sk.stuba.fei.uim.mobv_project.databinding.FragmentCreateWalletBinding
import sk.stuba.fei.uim.mobv_project.ui.intro.CreateWalletFragmentDirections.actionCreateWalletFragmentToMyBalanceFragment
import sk.stuba.fei.uim.mobv_project.ui.utils.LoadingLayoutUtils.setLoadingLayoutVisibility
import sk.stuba.fei.uim.mobv_project.ui.utils.NotificationUtils
import sk.stuba.fei.uim.mobv_project.ui.utils.NotificationUtils.showSnackbarFromMessageEvent

class CreateWalletFragment : Fragment(R.layout.fragment_create_wallet) {

    private val args: CreateWalletFragmentArgs by navArgs()
    private val viewModel: CreateWalletViewModel by viewModels {
        CreateWalletViewModelFactory(
            AccountRepository.getInstance(context!!),
            BalanceRepository.getInstance(context!!),
            args
        )
    }
    private lateinit var binding: FragmentCreateWalletBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_create_wallet,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.eventCopyToClipboard.observe(this, {
            it.getContentIfNotHandled()?.let { text ->
                copyToClipboard(text)
            }
        })
        viewModel.eventContinue.observe(this, { event ->
            event.getContentIfNotHandled()?.let {
                if (it) {
                    showContinueDialog()
                }
            }
        })
        viewModel.eventLoadingStart.observe(this, {
            setLoadingLayoutVisibility(activity, true)
        })
        viewModel.eventRepositoryValidationError.observe(this, {
            showSnackbarFromMessageEvent(view, it, Snackbar.LENGTH_LONG)
            setLoadingLayoutVisibility(activity, false)
        })
        viewModel.eventLocalAccountCreated.observe(this, { event ->
            event.getContentIfNotHandled()?.let {
                if (it) {
                    setLoadingLayoutVisibility(activity, false)
                    findNavController().navigate(
                        actionCreateWalletFragmentToMyBalanceFragment()
                    )
                }
            }
        })

        return binding.root
    }

    private fun copyToClipboard(content: String) {
        context?.let {
            val clipboardManager = it.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData: ClipData = ClipData.newPlainText("privateKey", content)

            clipboardManager.setPrimaryClip(clipData)

            NotificationUtils.showSnackbar(
                view, R.string.registered_copy_private_key_toast_text, Snackbar.LENGTH_LONG
            )
        }
    }

    private fun showContinueDialog() {
        context?.let {
            MaterialAlertDialogBuilder(it, R.style.CustomMaterialAlertDialog)
                .setTitle(resources.getString(R.string.registered_warning_popup_title))
                .setMessage(resources.getString(R.string.registered_warning_popup_text))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(resources.getString(R.string.registered_warning_popup_answer_positive)) { _, _ ->
                    viewModel.createLocalAccount()
                }
                .setNegativeButton(resources.getString(R.string.registered_warning_popup_answer_negative)) { _, _ -> }
                .show()
        }
    }
}