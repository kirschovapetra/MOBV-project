package sk.stuba.fei.uim.mobv_project.ui.my_balance

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import sk.stuba.fei.uim.mobv_project.R
import sk.stuba.fei.uim.mobv_project.data.entities.Payment
import sk.stuba.fei.uim.mobv_project.databinding.FragmentMyBalanceCardBinding


class PaymentsRecycleViewAdapter(
    var payments: List<Payment>
) :
    RecyclerView.Adapter<PaymentsRecycleViewAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            FragmentMyBalanceCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    // binds the list items to a view
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(payments[position])

    override fun getItemCount() =
        payments.size

    fun setData(newBalances: List<Payment>) {
        val diffUtil = PaymentsListDiffUtil(payments, newBalances)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        payments = newBalances
        diffResults.dispatchUpdatesTo(this)
    }

    class ViewHolder(private val binding: FragmentMyBalanceCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(payment: Payment) {
            val iconResource: Int
            val backgroundResource: Int

            if ("debit" == payment.paymentType) {
                iconResource = R.drawable.ic_baseline_add_24
                backgroundResource = R.drawable.custom_green_card
            } else {
                iconResource = R.drawable.ic_baseline_remove_24
                backgroundResource = R.drawable.custom_red_card
            }

            binding.apply {
                paymentCardTypeIcon.setBackgroundResource(iconResource)
                paymentCardWrapper.setBackgroundResource(backgroundResource)
                paymentSourceAccountName.text = payment.sourceAccount
                paymentSourceAccountId.text = payment.from
                paymentAmount.text = payment.amount?.toDouble().toString()
                paymentDate.text = payment.createdAt?.split("T")?.get(0).orEmpty()
            }
        }
    }
}