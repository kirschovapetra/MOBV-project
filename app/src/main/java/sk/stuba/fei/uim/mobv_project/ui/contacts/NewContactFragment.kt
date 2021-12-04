package sk.stuba.fei.uim.mobv_project.ui.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import sk.stuba.fei.uim.mobv_project.R
import sk.stuba.fei.uim.mobv_project.data.entities.Contact
import sk.stuba.fei.uim.mobv_project.data.repositories.AccountRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.ContactRepository
import sk.stuba.fei.uim.mobv_project.data.utils.ViewModelFactory
import sk.stuba.fei.uim.mobv_project.data.view_models.contacts.NewContactViewModel
import sk.stuba.fei.uim.mobv_project.databinding.FragmentNewContactBinding
import sk.stuba.fei.uim.mobv_project.ui.contacts.NewContactFragmentDirections.actionNewContactFragmentToContactsFragment
import sk.stuba.fei.uim.mobv_project.ui.utils.NotificationUtils
import sk.stuba.fei.uim.mobv_project.ui.utils.NotificationUtils.showSnackbar

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

        newContactViewModel.apply {
            isNew.observe(viewLifecycleOwner, { isNew ->
                if (isNew) {
                    binding.deleteContactButton.visibility = View.GONE
                }
                else {
                    binding.deleteContactButton.visibility = View.VISIBLE
                }
            })
            eventInvalidForm.observe(viewLifecycleOwner, { event ->
                event.getContentIfNotHandled()?.let { messageResourceId ->
                    showSnackbar(view, messageResourceId, Snackbar.LENGTH_LONG)
                }
            })
            eventFormSubmitted.observe(viewLifecycleOwner, { event ->
                event.getContentIfNotHandled()?.let { messageResourceId ->
                    navigateToContactsAndShowSnackbar(messageResourceId)
                }
            })
        }

        return binding.root
    }

    private fun navigateToContactsAndShowSnackbar(messageResourceId: Int) {
        findNavController().navigate(
            actionNewContactFragmentToContactsFragment()
        )
        NotificationUtils.showAnchorSnackbar(view, messageResourceId, Snackbar.LENGTH_LONG, R.id.bottom_nav_view)
    }
}