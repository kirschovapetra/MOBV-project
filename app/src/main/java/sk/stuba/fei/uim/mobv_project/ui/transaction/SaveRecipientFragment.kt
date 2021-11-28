package sk.stuba.fei.uim.mobv_project.ui.transaction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import sk.stuba.fei.uim.mobv_project.R
import sk.stuba.fei.uim.mobv_project.data.repositories.AccountRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.ContactRepository
import sk.stuba.fei.uim.mobv_project.data.utils.ViewModelFactory
import sk.stuba.fei.uim.mobv_project.data.view_models.contacts.NewContactViewModel
import sk.stuba.fei.uim.mobv_project.data.view_models.transaction.CreateNewTransactionViewModel
import sk.stuba.fei.uim.mobv_project.data.view_models.transaction.SaveRecipientViewModel
import sk.stuba.fei.uim.mobv_project.databinding.FragmentCreateNewTransactionBinding
import sk.stuba.fei.uim.mobv_project.databinding.FragmentSaveRecipientBinding
import sk.stuba.fei.uim.mobv_project.ui.transaction.SaveRecipientFragmentDirections.actionSaveRecipientFragmentToMyBalanceFragment

class SaveRecipientFragment : Fragment() {
    // TODO replace with NewContactViewModel
    private val args: SaveRecipientFragmentArgs by navArgs()
    private val viewModel: SaveRecipientViewModel by viewModels()

    private lateinit var binding: FragmentSaveRecipientBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        viewModel.accountId.value = args.accountId
        return binding.root
    }
}