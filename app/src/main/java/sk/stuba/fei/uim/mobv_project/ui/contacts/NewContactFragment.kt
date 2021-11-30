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
import androidx.navigation.fragment.findNavController
import sk.stuba.fei.uim.mobv_project.data.repositories.AccountRepository
import sk.stuba.fei.uim.mobv_project.data.view_models.contacts.NewContactViewModel.SaveResult
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
            event.getContentIfNotHandled()?.let { formStatus ->
                showToast(getToasterMessage(formStatus))
            }
        })
        newContactViewModel.eventContactSave.observe(this, { event ->
            event.getContentIfNotHandled()?.let { saveResult ->
                navigateToContactsAndMakeToast(saveResult)
            }
        })

        return binding.root
    }

    private fun navigateToContactsAndMakeToast(saveResult: SaveResult) {
        findNavController().navigate(
            actionNewContactFragmentToContactsFragment()
        )
        showToast(getToasterMessage(saveResult))
    }

    private fun showToast(message: String) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.TOP, 0, 150)
        toast.show()
    }

    private fun getToasterMessage(saveResult: SaveResult): String {
        return when (saveResult) {
            SaveResult.CREATED -> resources.getString(R.string.new_contact_created)
            SaveResult.UPDATED -> resources.getString(R.string.new_contact_updated)
        }
    }

    private fun getToasterMessage(formStatus: FormStatus): String {
        return when (formStatus) {
            FormStatus.INVALID_NAME -> resources.getString(R.string.new_contact_invalid_contact_name)
            FormStatus.INVALID_CONTACT_ID -> resources.getString(R.string.new_contact_invalid_contact_account_id)
            FormStatus.NON_EXISTING_ACCOUNT -> resources.getString(R.string.new_contact_non_existing_account_id)
            FormStatus.DUPLICATED_ACCOUNT -> resources.getString(R.string.new_contact_duplicate_contact)
            FormStatus.ADDING_YOURSELF -> resources.getString(R.string.new_contact_you_like_yourself)
            else -> ""
        }
    }
}