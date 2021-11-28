package sk.stuba.fei.uim.mobv_project.ui.contacts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import sk.stuba.fei.uim.mobv_project.R
import sk.stuba.fei.uim.mobv_project.data.entities.Contact
import sk.stuba.fei.uim.mobv_project.data.view_models.contacts.NewContactViewModel
import sk.stuba.fei.uim.mobv_project.databinding.FragmentNewContactBinding

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
                newContactViewModel.contact.value = Contact()
//                newContactViewModel.contactId.postValue("")
//                newContactViewModel.name.postValue("")
//                newContactViewModel.sourceAccount.postValue("")
            } else {
                val contact = serializedContact as Contact
                newContactViewModel.contact.value = contact
//                newContactViewModel.contactId.postValue(contact.contactId)
//                newContactViewModel.name.postValue(contact.name)
//                newContactViewModel.sourceAccount.postValue(contact.sourceAccount)
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
        val clickButtonListener: View.OnClickListener = View.OnClickListener {Unit
            sendRequestToDB()
        }

        binding.addNewContactButtonTitle.setOnClickListener(
            clickButtonListener

        )
    }

    private fun sendRequestToDB(){

    }
}