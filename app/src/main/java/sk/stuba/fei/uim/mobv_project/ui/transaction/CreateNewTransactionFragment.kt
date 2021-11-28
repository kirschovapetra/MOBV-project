package sk.stuba.fei.uim.mobv_project.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import sk.stuba.fei.uim.mobv_project.R
import sk.stuba.fei.uim.mobv_project.data.entities.Contact
import sk.stuba.fei.uim.mobv_project.data.repositories.ContactRepository
import sk.stuba.fei.uim.mobv_project.data.utils.ViewModelFactory
import sk.stuba.fei.uim.mobv_project.data.view_models.transaction.CreateNewTransactionViewModel
import sk.stuba.fei.uim.mobv_project.databinding.FragmentCreateNewTransactionBinding

class CreateNewTransactionFragment : Fragment(), AdapterView.OnItemSelectedListener{
    private val viewModel: CreateNewTransactionViewModel by viewModels(){
        ViewModelFactory(ContactRepository.getInstance(context!!))
    }
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
        binding.spinner.onItemSelectedListener = this
        viewModel.allContacts.observe(viewLifecycleOwner,{
            val spinnerAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item,it)
            binding.spinner.adapter = spinnerAdapter
        })
        viewModel.eventPaymentSuccessful.observe(viewLifecycleOwner,{ event ->
            event.getContentIfNotHandled()?.let {
                findNavController().navigate(it)
            }
        })
        return binding.root
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val adapterItem = binding.spinner.adapter.getItem(position) as Contact
        viewModel.setSelectedContact(adapterItem)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // TODO("Not yet implemented")
    }
}