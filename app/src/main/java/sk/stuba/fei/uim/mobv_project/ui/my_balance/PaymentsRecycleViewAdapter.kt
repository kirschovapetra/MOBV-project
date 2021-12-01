package sk.stuba.fei.uim.mobv_project.ui.my_balance

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import sk.stuba.fei.uim.mobv_project.R
import sk.stuba.fei.uim.mobv_project.data.entities.Payment
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter


class PaymentsRecycleViewAdapter(
    var payments: List<Payment>
) :
    RecyclerView.Adapter<PaymentsRecycleViewAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemview = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_balance_card, parent, false)

//        val height: Int = parent.measuredHeight / 3
//        itemView. setMinimumHeight(height)

//        itemview.minimumHeight = height
        return ViewHolder(itemview)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun formatDate(dateString: String): LocalDate? {
        val date = LocalDate.parse(dateString)
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val text = date.format(formatter)
        return LocalDate.parse(text, formatter)

//        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
//        return LocalDate.parse(dateString, formatter)
    }

    // binds the list items to a view
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: PaymentsRecycleViewAdapter.ViewHolder, position: Int) {
        val payment = payments[position]

        if(payment.paymentType.equals("debit")) {
            holder.paymentTypeIcon.setBackgroundResource(R.drawable.ic_baseline_add_24)
        } else {
            holder.paymentTypeIcon.setBackgroundResource(R.drawable.ic_baseline_arrow_back_24)
        }

        val amountNum = payment.amount!!.toDouble()

        holder.paymentContactName.text = payment.sourceAccount
        holder.paymentContactId.text = payment.from
        holder.paymentAmount.text = amountNum.toString()
        holder.paymentDate.text = payment.createdAt!!.split("T")[0]
    }

    override fun getItemCount(): Int {
        return payments.size
    }

    fun setData(newBalances: List<Payment>){
        val diffUtil = PaymentsListDiffUtil(payments, newBalances)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        payments = newBalances
        diffResults.dispatchUpdatesTo(this)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val paymentTypeIcon: ImageView = itemView.findViewById(R.id.paymentCardTypeIcon)
        val paymentContactName: TextView = itemView.findViewById(R.id.paymentSourceAccountName)
        val paymentContactId: TextView = itemView.findViewById(R.id.paymentSourceAccountId)
        val paymentAmount: TextView = itemView.findViewById(R.id.paymentAmount)
        val paymentDate: TextView = itemView.findViewById(R.id.paymentDate)
    }
}