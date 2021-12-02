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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import sk.stuba.fei.uim.mobv_project.R
import sk.stuba.fei.uim.mobv_project.data.repositories.BalanceRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.ContactRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.PaymentRepository
import sk.stuba.fei.uim.mobv_project.data.utils.ViewModelFactory
import sk.stuba.fei.uim.mobv_project.data.view_models.my_balance.MyBalanceViewModel
import sk.stuba.fei.uim.mobv_project.databinding.FragmentMyBalanceBinding
import sk.stuba.fei.uim.mobv_project.ui.my_balance.MyBalanceFragmentDirections.actionMyBalanceFragmentToCreateNewTransactionFragment
import sk.stuba.fei.uim.mobv_project.ui.utils.ClipboardUtils
import sk.stuba.fei.uim.mobv_project.utils.SecurityContext


class MyBalanceFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val myBalanceViewModel: MyBalanceViewModel by viewModels {
        ViewModelFactory(
            BalanceRepository.getInstance(requireContext()),
            PaymentRepository.getInstance(requireContext()),
            ContactRepository.getInstance(requireContext())
        )
    }
    private lateinit var binding: FragmentMyBalanceBinding
    private lateinit var adapter: PaymentsRecycleViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_my_balance,
            container,
            false
        )
        binding.myBalanceViewModel = myBalanceViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setAssetsObserver()
        binding.assetOptionsSpinner.onItemSelectedListener = this

        setNewTransactionButtonListener()
        initializeRecycleAdapter()
        setBalanceObserver()
        setPaymentsObserver()
        setAccountIdClickListener()

        return binding.root
    }

    private fun setAssetsObserver() {
        myBalanceViewModel.assetOptions.observe(viewLifecycleOwner,{ assetOptions ->
            setSpinnerAdapter(assetOptions.toMutableList())
        })
    }

    private fun setSpinnerAdapter(options: MutableList<String>) {
        val spinner: Spinner = binding.assetOptionsSpinner
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            context!!,
            android.R.layout.simple_spinner_item,
            options
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
    }

    private fun initializeRecycleAdapter() {
        val paymentsRecycleView = binding.paymentsRecyclerView

        adapter = PaymentsRecycleViewAdapter(listOf())

        paymentsRecycleView.layoutManager = LinearLayoutManager(context)
        paymentsRecycleView.adapter = adapter
    }

    private fun setNewTransactionButtonListener() {
        binding.newTransactionButton.setOnClickListener {
            findNavController().navigate(
                actionMyBalanceFragmentToCreateNewTransactionFragment()
            )
        }
    }

    private fun setBalanceObserver() {
        //TODO: neviem preco databinding z XML nefunguje pre tento field (kvoli transformacii?)
        myBalanceViewModel.balance.observe(viewLifecycleOwner, {
            binding.accountBalance.text = it
        })
    }

    private fun setPaymentsObserver() {
        myBalanceViewModel.paymentsToShow.observe(viewLifecycleOwner, { selectedPayments ->
            adapter.setData(selectedPayments)
        })
    }

    private fun setAccountIdClickListener() {
        binding.accountIdTextView.setOnClickListener {
            ClipboardUtils.copyToClipboard(
                context,
                "accountId",
                SecurityContext.account?.accountId.orEmpty(),
                view,
                R.string.my_balance_copy_account_key_toast_text
            )
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedAsset = binding.assetOptionsSpinner.selectedItem as String
        Log.d("SPINNER", "Selected asset: $selectedAsset")
        myBalanceViewModel.selectedAsset.value = selectedAsset
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        Log.d("SPINNER", "NIC SA NEDEJE")
    }
}