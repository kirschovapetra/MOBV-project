package sk.stuba.fei.uim.mobv_project.ui.contacts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import sk.stuba.fei.uim.mobv_project.R
import sk.stuba.fei.uim.mobv_project.data.entities.Contact
import sk.stuba.fei.uim.mobv_project.data.view_models.NewContactViewModel
import sk.stuba.fei.uim.mobv_project.databinding.FragmentNewContactBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
//todo set action bar title to ADD or EDIT
class NewContactFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private var CONTACT_PARAM_KEY = "contact"


    private val newContactViewModel: NewContactViewModel by viewModels()
    private lateinit var binding: FragmentNewContactBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val serializedContact  = it.getSerializable(CONTACT_PARAM_KEY) //as Contact

            if(serializedContact == null){
                newContactViewModel.contactId.postValue("")
                newContactViewModel.name.postValue("")
                newContactViewModel.sourceAccount.postValue("")
            } else {
                val contact = serializedContact as Contact
                newContactViewModel.contactId.postValue(contact.contactId)
                newContactViewModel.name.postValue(contact.name)
                newContactViewModel.sourceAccount.postValue(contact.sourceAccount)
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

        val arrayList = ArrayList<Contact>()
        arrayList.add(Contact("1", "Jozko", "1"))
        arrayList.add(Contact("2", "Betka", "2"))
        arrayList.add(Contact("3", "Dan", "3"))

        binding.newContactViewModel = newContactViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    private fun attachClickListenerToAddButton(binding: FragmentNewContactBinding){
        val clickButtonListener: View.OnClickListener = View.OnClickListener { view -> Unit
            updateContactsViewModel()
        }

        binding.addNewContactButtonTitle.setOnClickListener(
            clickButtonListener
        )
    }

    private fun sendRequestToDB(){

    }

    private fun updateContactsViewModel(){ // toto asi nie
//        var oldContacts = contactsViewModel.arrayList
        var arrayList = ArrayList<Contact>()


        arrayList.add(Contact("1", "Jozko", "1"))
        arrayList.add(Contact("2", "Martin", "2"))
        arrayList.add(Contact("3", "Marek", "3"))
        contactsViewModel.contacts.postValue(arrayList)
        Log.e("NEW FRAGMENT", contactsViewModel.contacts.value.toString())
    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment NewContactFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            NewContactFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}