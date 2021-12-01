package sk.stuba.fei.uim.mobv_project.ui.my_balance

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import sk.stuba.fei.uim.mobv_project.R
import sk.stuba.fei.uim.mobv_project.data.repositories.BalanceRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.ContactRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.PaymentRepository
import sk.stuba.fei.uim.mobv_project.data.utils.ViewModelFactory
import sk.stuba.fei.uim.mobv_project.data.view_models.my_balance.MyBalanceViewModel
import sk.stuba.fei.uim.mobv_project.databinding.FragmentMyBalanceBinding
import sk.stuba.fei.uim.mobv_project.ui.utils.NotificationUtils
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setPaymentsObserver()
        adapter = PaymentsRecycleViewAdapter(listOf())
    }

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

        attachObserverToSpinner()
        binding.assetOptionsSpinner.onItemSelectedListener = this

        attachListenerToNewTransactionButton(binding)
        attachViewModelToBinding(binding)
        funInitializeRecycleAdapter(binding)


        binding.accountIdTextView.setOnClickListener {
            copyToClipboard()
        }

        return binding.root
    }

    private fun attachObserverToSpinner(){
        myBalanceViewModel.assetOptions.observe(
            this,
            { assetOptions ->
                setSpinnerAdapter(assetOptions.toMutableList())
            }
        )
    }

    private fun setSpinnerAdapter(options: MutableList<String>){
        val spinner: Spinner = binding.assetOptionsSpinner
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            context!!,
            android.R.layout.simple_spinner_item,
            options
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
    }

    private fun funInitializeRecycleAdapter(binding: FragmentMyBalanceBinding) {
        val paymentsRecycleView = binding.paymentsRecyclerView

        paymentsRecycleView.layoutManager = LinearLayoutManager(context)
        paymentsRecycleView.adapter = adapter
    }

    private fun attachViewModelToBinding(binding: FragmentMyBalanceBinding) {
        binding.myBalanceViewModel = myBalanceViewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun attachListenerToNewTransactionButton(binding: FragmentMyBalanceBinding) {
        val clickButtonListener: View.OnClickListener = View.OnClickListener {
            findNavController().navigate(
                MyBalanceFragmentDirections.actionMyBalanceFragmentToCreateNewTransactionFragment()
            )
        }

        binding.newTransactionButton.setOnClickListener(
            clickButtonListener
        )
    }

    private fun setPaymentsObserver(){
        myBalanceViewModel.selectedPayments.observe(
            this,
            { selectedPayments ->
                adapter.setData(selectedPayments)
            }
        )
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, assetOptionIndex: Int, p3: Long) {
        val selectedAssetOption = myBalanceViewModel.assetOptions.value!![assetOptionIndex]
        myBalanceViewModel.updatePaymentsAndBalance(selectedAssetOption)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        Log.e("SPINNER", "NIC SA NEDEJE")
    }

    private fun copyToClipboard() {

        context?.let {
            val clipboardManager = it.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData: ClipData = ClipData.newPlainText("account id", SecurityContext.account!!.accountId)

            clipboardManager.setPrimaryClip(clipData)

            NotificationUtils.showSnackbar(
                view, R.string.registered_copy_private_key_toast_text, Snackbar.LENGTH_LONG
            )
        }
    }
}