package sk.stuba.fei.uim.mobv_project.ui.contacts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import sk.stuba.fei.uim.mobv_project.R
import sk.stuba.fei.uim.mobv_project.data.entities.Contact
import sk.stuba.fei.uim.mobv_project.data.repositories.ContactRepository
import sk.stuba.fei.uim.mobv_project.data.utils.ViewModelFactory
import sk.stuba.fei.uim.mobv_project.data.view_models.contacts.NewContactViewModel
import sk.stuba.fei.uim.mobv_project.data.view_models.contacts.NewContactViewModel.FormStatus
import sk.stuba.fei.uim.mobv_project.databinding.FragmentNewContactBinding
import android.view.Gravity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import sk.stuba.fei.uim.mobv_project.data.repositories.AccountRepository

class NewContactFragment : Fragment() {

    private var CONTACT_PARAM_KEY = "contact"

    private lateinit var binding: FragmentNewContactBinding
    private val newContactViewModel: NewContactViewModel by viewModels {
        ViewModelFactory(
            ContactRepository.getInstance(context!!),
            AccountRepository.getInstance(context!!)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val serializedContact  = it.getSerializable(CONTACT_PARAM_KEY) //as Contact

            if(serializedContact != null){
                newContactViewModel.setContact(serializedContact as Contact)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_new_contact,
            container,
            false
        )
        attachClickListenerToAddButton(binding)
        View.VISIBLE
        binding.newContactViewModel = newContactViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    private fun attachClickListenerToAddButton(binding: FragmentNewContactBinding){
        val clickButtonListener: View.OnClickListener = View.OnClickListener {Unit
            updateContacts()
        }

        binding.addNewContactButtonTitle.setOnClickListener(clickButtonListener)
    }

    private fun updateContacts() {
        val formStatus = newContactViewModel.returnStatusOfTheForm()

        if (formStatus != FormStatus.OK) {
            showToast(newContactViewModel.getToasterMessage(formStatus))
            return
        }

        insertOrUpdateContact(newContactViewModel.getContact())
    }

    private fun insertOrUpdateContact(contact: Contact){
        lifecycleScope.launch{
            if(newContactViewModel.isNew.value!!){
                newContactViewModel.insertContact(contact)
                navigateToContactsAndMakeToast("Contact added!")
            } else {
                newContactViewModel.contactRepo.updateContact(contact)
                navigateToContactsAndMakeToast("Contact updated!")
            }
        }
    }

    private fun navigateToContactsAndMakeToast(message: String){
        findNavController().navigate(
            NewContactFragmentDirections.actionNewContactFragmentToContactsFragment()
        )
        showToast(message)
    }

    private fun showToast(message: String){
        var toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.TOP, 0, 150)
        toast.show()
    }
}