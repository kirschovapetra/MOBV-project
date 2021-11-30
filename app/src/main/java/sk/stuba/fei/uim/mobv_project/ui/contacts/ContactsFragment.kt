package sk.stuba.fei.uim.mobv_project.ui.contacts

import android.os.Bundle
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

class ContactsFragment : Fragment(), ContactsRecycleViewAdapter.OnContactClickListener {

    private val contactsViewModel: ContactsViewModel by viewModels {
        ViewModelFactory(
            ContactRepository.getInstance(context!!)
        )
    }
    private lateinit var binding: FragmentContactsBinding
    private lateinit var adapter: ContactsRecycleViewAdapter

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
//        contactsViewModel.contacts.postValue(returnDummyData()) // dotiahnit data na zobrazenie

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
        contactsViewModel.contactRepo.getAccountContacts("1").observe(
            this,
            { contacts ->
                adapter.setData(contacts)
            }
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

//    fun returnDummyData(): ArrayList<Contact> {
//        var arrayList = ArrayList<Contact>()
//        arrayList.add(Contact("1", "Jozko", "1"))
//        arrayList.add(Contact("2", "Betka", "2"))
//        arrayList.add(Contact("3", "Dan", "3"))
//        arrayList.add(Contact("4", "Johny", "4"))
//        arrayList.add(Contact("1", "Soky", "1"))
//        arrayList.add(Contact("2", "Martin", "2"))
//        arrayList.add(Contact("3", "Marek", "3"))
//        arrayList.add(Contact("4", "Zuzka", "4"))
//        arrayList.add(Contact("1", "JAROOO", "1"))
//        arrayList.add(Contact("2", "Matus", "2"))
//        arrayList.add(Contact("3", "Ondo", "3"))
//        arrayList.add(Contact("4", "Huto", "4"))
//        arrayList.add(Contact("1", "Jozko", "1"))
//        arrayList.add(Contact("2", "Betka", "2"))
//        arrayList.add(Contact("3", "Dan", "3"))
//        arrayList.add(Contact("4", "Johny", "4"))
//        arrayList.add(Contact("1", "Soky", "1"))
//        arrayList.add(Contact("2", "Martin", "2"))
//        arrayList.add(Contact("3", "Marek", "3"))
//        arrayList.add(Contact("4", "Zuzka", "4"))
//        arrayList.add(Contact("1", "JAROOO", "1"))
//        arrayList.add(Contact("2", "Matus", "2"))
//        arrayList.add(Contact("3", "Ondo", "3"))
//        arrayList.add(Contact("4", "Huto", "4"))
//        return arrayList
//    }
}