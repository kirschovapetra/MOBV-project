package sk.stuba.fei.uim.mobv_project.ui.transaction

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
import sk.stuba.fei.uim.mobv_project.data.repositories.ContactRepository
import sk.stuba.fei.uim.mobv_project.data.utils.ViewModelFactory
import sk.stuba.fei.uim.mobv_project.data.view_models.contacts.NewContactViewModel
import sk.stuba.fei.uim.mobv_project.databinding.FragmentSaveRecipientBinding
import sk.stuba.fei.uim.mobv_project.ui.transaction.SaveRecipientFragmentDirections.actionSaveRecipientFragmentToMyBalanceFragment
import sk.stuba.fei.uim.mobv_project.ui.utils.NotificationUtils.showSnackbar
import sk.stuba.fei.uim.mobv_project.ui.utils.NotificationUtils.showSnackbarFromResourceEvent

class SaveRecipientFragment : Fragment() {
    private val args: SaveRecipientFragmentArgs by navArgs()
    private val viewModel: NewContactViewModel by viewModels {
        ViewModelFactory(
            ContactRepository.getInstance(context!!),
            AccountRepository.getInstance(context!!)
        )
    }
    private lateinit var binding: FragmentSaveRecipientBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_save_recipient,
            container,
            false
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.addSkipButton.setOnClickListener {
            findNavController().navigate(actionSaveRecipientFragmentToMyBalanceFragment())
        }
        viewModel.contactAccountId.value = args.accountId
        viewModel.eventInvalidForm.observe(this, { event ->
            showSnackbarFromResourceEvent(view, event, Snackbar.LENGTH_LONG)
        })
        viewModel.eventContactSave.observe(this, { event ->
            event.getContentIfNotHandled()?.let { messageResourceId ->
                showSnackbar(view, messageResourceId, Snackbar.LENGTH_LONG)
                findNavController().navigate(actionSaveRecipientFragmentToMyBalanceFragment())
            }
        })
        return binding.root
    }
}