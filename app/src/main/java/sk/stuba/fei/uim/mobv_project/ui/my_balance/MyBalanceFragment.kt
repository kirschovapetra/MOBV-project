package sk.stuba.fei.uim.mobv_project.ui.my_balance

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import sk.stuba.fei.uim.mobv_project.R
import sk.stuba.fei.uim.mobv_project.data.view_models.my_balance.MyBalanceViewModel
import sk.stuba.fei.uim.mobv_project.databinding.FragmentMyBalanceBinding
import sk.stuba.fei.uim.mobv_project.ui.abstracts.NoNavigationUpFragment


//todo ked chalan otoci telefon, tak by sa mohla zachovat value selectnuta v spinneri
class MyBalanceFragment : NoNavigationUpFragment(), AdapterView.OnItemSelectedListener {

    private val myBalanceViewModel: MyBalanceViewModel by viewModels()
    private lateinit var binding: FragmentMyBalanceBinding
    private lateinit var adapter: BalancesRecycleViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBalancesObserver()
        adapter = BalancesRecycleViewAdapter(myBalanceViewModel.exportBalances)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_my_balance,
            container,
            false
        )

        attachObserverToSpinner()

//        myBalanceViewModel.tokenOptions = myBalanceViewModel.tmpOptions
//        myBalanceViewModel.tokenOptions.postValue(getDummyTokens()) // tpm todo dat het


//        attachListerToNewTransactionButton(binding)
        attachViewModelToBinding(binding)
        funInitializeRecycleAdapter(binding)

        return binding.root
    }

    private fun attachObserverToSpinner(){
        myBalanceViewModel.tokenOptions.observe(
            this,
            { tokenOptions ->
                setSpinnerAdapter(tokenOptions.toMutableList())
            }
        )
    }

    private fun setSpinnerAdapter(options: MutableList<String>){
        val spinner: Spinner = binding.tokenOptionsSpinner
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            context!!,
            android.R.layout.simple_spinner_item,
            options
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
    }

    private fun getDummyTokens(): MutableList<String> {
        return mutableListOf("86223","86823","90223","90223")// ArrayList()
    }

    private fun funInitializeRecycleAdapter(binding: FragmentMyBalanceBinding) {
        val balancesRecycleView = binding.balancesRecyclerView

        balancesRecycleView.layoutManager = LinearLayoutManager(context)
        balancesRecycleView.adapter = adapter
    }

    private fun attachViewModelToBinding(binding: FragmentMyBalanceBinding) {
        binding.myBalanceViewModel = myBalanceViewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }



    private fun attachListerToNewTransactionButton(binding: FragmentMyBalanceBinding) {
        val clickButtonListener: View.OnClickListener = View.OnClickListener {
//            findNavController().navigate(
//                ContactsFragmentDirections.actionContactsFragmentToNewContactFragment()
//            )
        }

        binding.newTransactionButton.setOnClickListener(
            clickButtonListener
        )
    }

    private fun setBalancesObserver(){
        myBalanceViewModel.balances.observe(
            this,
            { balances ->
                myBalanceViewModel.exportBalances = balances
                adapter.setData(balances)
            }
        )
    }


    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        Log.e("SPINNER", "position" + p2.toString() + " Id " + p3.toString())
//        myBalanceViewModel.selectedToken.postValue()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        Log.e("SPINNER", "NIC SA NEDEJE")
    }
}