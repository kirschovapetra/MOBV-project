package sk.stuba.fei.uim.mobv_project.ui.contacts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import sk.stuba.fei.uim.mobv_project.R
import sk.stuba.fei.uim.mobv_project.data.view_models.ContactsViewModel
import sk.stuba.fei.uim.mobv_project.databinding.FragmentContactsBinding
import androidx.navigation.fragment.findNavController
import sk.stuba.fei.uim.mobv_project.data.entities.Contact

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ContactsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContactsFragment : Fragment(), ContactsRecycleViewAdapter.OnContactClickListener {

    private val contactsViewModel: ContactsViewModel by viewModels()
    private lateinit var binding: FragmentContactsBinding
    private lateinit var adapter: ContactsRecycleViewAdapter

    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContactsObserver()
        adapter = ContactsRecycleViewAdapter(contactsViewModel.arrayList, this)
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

        contactsViewModel.contacts.postValue(returnDummyData()) // dotiahnit data na zobrazenie

        attachListerToNewContactButton(binding)
        attachViewModelToBinding(binding)
        funInitializeRecycleAdapter(binding)

        return binding.root
    }

    private fun attachViewModelToBinding(binding: FragmentContactsBinding){
        binding.contactsViewModel = contactsViewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun funInitializeRecycleAdapter(binding: FragmentContactsBinding){
        val contactsRecyclerView = binding.contactsRecyclerView

        contactsRecyclerView.layoutManager = LinearLayoutManager(context)
        contactsRecyclerView.adapter = adapter
    }

    private fun attachListerToNewContactButton(binding: FragmentContactsBinding){
        val clickButtonListener: View.OnClickListener = View.OnClickListener { view -> Unit
            findNavController().navigate(
                ContactsFragmentDirections.actionContactsFragmentToNewContactFragment()
            )
        }

        binding.newContactButton.setOnClickListener(
            clickButtonListener
        )
    }

    private fun setContactsObserver(){
        contactsViewModel.contacts.observe(
            this,
            { contacts ->
                contactsViewModel.arrayList = contacts
                adapter.setData(contacts)
            }
        )
    }

    override fun onContactClick(position: Int) {
        val clickedContact = contactsViewModel.arrayList[position]
        findNavController().navigate(
            ContactsFragmentDirections.actionContactsFragmentToNewContactFragment().setContact(clickedContact)
        )
        adapter.notifyItemChanged(position)

    }

    fun returnDummyData(): ArrayList<Contact> {
        var arrayList = ArrayList<Contact>()
        arrayList.add(Contact("1", "Jozko", "1"))
        arrayList.add(Contact("2", "Betka", "2"))
        arrayList.add(Contact("3", "Dan", "3"))
        arrayList.add(Contact("4", "Johny", "4"))
        arrayList.add(Contact("1", "Soky", "1"))
        arrayList.add(Contact("2", "Martin", "2"))
        arrayList.add(Contact("3", "Marek", "3"))
        arrayList.add(Contact("4", "Zuzka", "4"))
        arrayList.add(Contact("1", "JAROOO", "1"))
        arrayList.add(Contact("2", "Matus", "2"))
        arrayList.add(Contact("3", "Ondo", "3"))
        arrayList.add(Contact("4", "Huto", "4"))
        return arrayList
    }

    //    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment ContactsFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            ContactsFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}