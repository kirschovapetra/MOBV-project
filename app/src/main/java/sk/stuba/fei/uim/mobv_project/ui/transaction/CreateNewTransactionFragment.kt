package sk.stuba.fei.uim.mobv_project.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import sk.stuba.fei.uim.mobv_project.R
import sk.stuba.fei.uim.mobv_project.data.view_models.transaction.CreateNewTransactionViewModel
import sk.stuba.fei.uim.mobv_project.databinding.FragmentCreateNewTransactionBinding

class CreateNewTransactionFragment : Fragment(){
    private val viewModel: CreateNewTransactionViewModel by viewModels()
    private lateinit var binding: FragmentCreateNewTransactionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_create_new_transaction,
            container,
            false
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }
}