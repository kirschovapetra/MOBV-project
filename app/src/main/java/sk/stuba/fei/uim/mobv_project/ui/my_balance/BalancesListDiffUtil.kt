package sk.stuba.fei.uim.mobv_project.ui.my_balance

import androidx.recyclerview.widget.DiffUtil
import sk.stuba.fei.uim.mobv_project.data.entities.Balances

class BalancesListDiffUtil(
    private val oldBalances: List<Balances>,
    private val newBalances: List<Balances>

): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldBalances.size
    }

    override fun getNewListSize(): Int {
        return newBalances.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldBalances[oldItemPosition].assetCode == newBalances[newItemPosition].assetCode
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldBalances[oldItemPosition].assetCode != newBalances[newItemPosition].assetCode -> {
                false
            }
            oldBalances[oldItemPosition].balance != newBalances[newItemPosition].balance -> {
                false
            }
            oldBalances[oldItemPosition].sourceAccount != newBalances[newItemPosition].sourceAccount -> {
                false
            }
            else -> true
        }
    }
}