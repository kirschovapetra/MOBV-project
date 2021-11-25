package sk.stuba.fei.uim.mobv_project.ui.my_balance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import sk.stuba.fei.uim.mobv_project.R
import sk.stuba.fei.uim.mobv_project.data.view_models.my_balance.MyBalanceViewModel
import sk.stuba.fei.uim.mobv_project.databinding.FragmentMyBalanceBinding
import sk.stuba.fei.uim.mobv_project.ui.abstracts.NoNavigationUpFragment

class MyBalanceFragment : NoNavigationUpFragment() {

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

//        attachListerToNewTransactionButton(binding)
        attachViewModelToBinding(binding)
        funInitializeRecycleAdapter(binding)

        return binding.root
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

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment MyBalanceFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            MyBalanceFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}