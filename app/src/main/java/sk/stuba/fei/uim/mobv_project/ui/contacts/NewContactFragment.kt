package sk.stuba.fei.uim.mobv_project.ui.contacts

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import sk.stuba.fei.uim.mobv_project.R
import sk.stuba.fei.uim.mobv_project.data.entities.Contact
import sk.stuba.fei.uim.mobv_project.data.repositories.AccountRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.ContactRepository
import sk.stuba.fei.uim.mobv_project.data.utils.ViewModelFactory
import sk.stuba.fei.uim.mobv_project.data.view_models.contacts.NewContactViewModel
import sk.stuba.fei.uim.mobv_project.databinding.FragmentNewContactBinding
import sk.stuba.fei.uim.mobv_project.ui.contacts.NewContactFragmentDirections.actionNewContactFragmentToContactsFragment

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
            val serializedContact = it.getSerializable(CONTACT_PARAM_KEY)

            if (serializedContact != null) {
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
        binding.newContactViewModel = newContactViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        newContactViewModel.eventInvalidForm.observe(this, { event ->
            event.getContentIfNotHandled()?.let { messageResourceId ->
                showToast(messageResourceId)
            }
        })
        newContactViewModel.eventContactSave.observe(this, { event ->
            event.getContentIfNotHandled()?.let { messageResourceId ->
                navigateToContactsAndMakeToast(messageResourceId)
            }
        })

        return binding.root
    }

    private fun navigateToContactsAndMakeToast(messageResourceId: Int) {
        findNavController().navigate(
            actionNewContactFragmentToContactsFragment()
        )
        showToast(messageResourceId)
    }

    private fun showToast(messageResourceId: Int) {
        val toast = Toast.makeText(
            context,
            resources.getString(messageResourceId),
            Toast.LENGTH_LONG
        )
        toast.setGravity(Gravity.TOP, 0, 150)
        toast.show()
    }
}