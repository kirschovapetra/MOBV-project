package sk.stuba.fei.uim.mobv_project.ui.my_balance

import androidx.recyclerview.widget.DiffUtil
import sk.stuba.fei.uim.mobv_project.data.entities.Payment

class PaymentsListDiffUtil(
    private val oldPayments: List<Payment>,
    private val newPayments: List<Payment>

): DiffUtil.Callback() {
    override fun getOldListSize() =
        oldPayments.size

    override fun getNewListSize() =
        newPayments.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldPayments[oldItemPosition].paymentId == newPayments[newItemPosition].paymentId

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldPayments[oldItemPosition] == newPayments[newItemPosition]
}