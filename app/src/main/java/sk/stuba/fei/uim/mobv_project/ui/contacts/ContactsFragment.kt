package sk.stuba.fei.uim.mobv_project.ui.contacts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import sk.stuba.fei.uim.mobv_project.R
import sk.stuba.fei.uim.mobv_project.data.repositories.ContactRepository
import sk.stuba.fei.uim.mobv_project.data.utils.ViewModelFactory
import sk.stuba.fei.uim.mobv_project.data.view_models.contacts.ContactsViewModel
import sk.stuba.fei.uim.mobv_project.databinding.FragmentContactsBinding
import sk.stuba.fei.uim.mobv_project.utils.SecurityContext

class ContactsFragment : Fragment(), ContactsRecycleViewAdapter.OnContactClickListener {

    private val contactsViewModel: ContactsViewModel by viewModels {
        ViewModelFactory(
            ContactRepository.getInstance(context!!)
        )
    }
    private lateinit var binding: FragmentContactsBinding
    private lateinit var adapter: ContactsRecycleViewAdapter
    private val account = SecurityContext.account!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContactObserver()
        adapter = ContactsRecycleViewAdapter(listOf(), this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_contacts,
            container,
            false
        )

        attachListerToNewContactButton(binding)
        attachViewModelToBinding(binding)
        funInitializeRecycleAdapter(binding)

        return binding.root
    }

    private fun attachViewModelToBinding(binding: FragmentContactsBinding) {
        binding.contactsViewModel = contactsViewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun funInitializeRecycleAdapter(binding: FragmentContactsBinding) {
        val contactsRecyclerView = binding.contactsRecyclerView

        contactsRecyclerView.layoutManager = LinearLayoutManager(context)
        contactsRecyclerView.adapter = adapter
    }

    private fun attachListerToNewContactButton(binding: FragmentContactsBinding) {
        val clickButtonListener: View.OnClickListener = View.OnClickListener {
            findNavController().navigate(
                ContactsFragmentDirections.actionContactsFragmentToNewContactFragment()
            )
        }

        binding.newContactButton.setOnClickListener(
            clickButtonListener
        )
    }

    private fun setContactObserver(){
        contactsViewModel.contactRepo.getAccountContacts(account.accountId).observe(
            this,
            { contacts -> adapter.setData(contacts) }
        )
    }


    override fun onContactClick(position: Int) {
        val clickedContact = adapter.contacts[position]

        findNavController().navigate(
            ContactsFragmentDirections.actionContactsFragmentToNewContactFragment()
                .setContact(clickedContact)
        )
        adapter.notifyItemChanged(position)

    }
}