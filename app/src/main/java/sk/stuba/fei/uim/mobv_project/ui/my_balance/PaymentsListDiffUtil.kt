package sk.stuba.fei.uim.mobv_project.ui.my_balance

import androidx.recyclerview.widget.DiffUtil
import sk.stuba.fei.uim.mobv_project.data.entities.Payment

class PaymentsListDiffUtil(
    private val oldPayments: List<Payment>,
    private val newPayments: List<Payment>

): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldPayments.size
    }

    override fun getNewListSize(): Int {
        return newPayments.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldPayments[oldItemPosition].paymentId == newPayments[newItemPosition].paymentId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldPayments[oldItemPosition].paymentId != newPayments[newItemPosition].paymentId -> {
                false
            }
            oldPayments[oldItemPosition].transactionHash != newPayments[newItemPosition].transactionHash -> {
                false
            }
            oldPayments[oldItemPosition].transactionSuccessful != newPayments[newItemPosition].transactionSuccessful -> {
                false
            }
            oldPayments[oldItemPosition].createdAt != newPayments[newItemPosition].createdAt -> {
                false
            }
            oldPayments[oldItemPosition].assetCode != newPayments[newItemPosition].assetCode -> {
                false
            }
            oldPayments[oldItemPosition].from != newPayments[newItemPosition].from -> {
                false
            }
            oldPayments[oldItemPosition].to != newPayments[newItemPosition].to -> {
                false
            }
            oldPayments[oldItemPosition].amount != newPayments[newItemPosition].amount -> {
                false
            }
            oldPayments[oldItemPosition].paymentType != newPayments[newItemPosition].paymentType -> {
                false
            }
            oldPayments[oldItemPosition].sourceAccount != newPayments[newItemPosition].sourceAccount -> {
                false
            }

            else -> true
        }
    }
}