package sk.stuba.fei.uim.mobv_project.ui.my_balance

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import sk.stuba.fei.uim.mobv_project.R
import sk.stuba.fei.uim.mobv_project.data.entities.Balances

class BalancesRecycleViewAdapter(
    private var balances: List<Balances>
) :
    RecyclerView.Adapter<BalancesRecycleViewAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_balance_card, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: BalancesRecycleViewAdapter.ViewHolder, position: Int) {

        val balancesViewModel = balances[position]

        // sets the image to the imageview from our itemHolder class
//        holder.imageView.setImageResource(ItemsViewModel.image)

        // sets the text to the textview from our itemHolder class
        holder.balanceContactNameTextView.text = balancesViewModel.assetIssuer
    }

    override fun getItemCount(): Int {
        return balances.size
    }

    fun setData(newBalances: List<Balances>){
        val diffUtil = BalancesListDiffUtil(balances, newBalances)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        balances = newBalances
        diffResults.dispatchUpdatesTo(this)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //                val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val balanceContactNameTextView: TextView = itemView.findViewById(R.id.balanceContactName)
    }
}