package sk.stuba.fei.uim.mobv_project.ui.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import sk.stuba.fei.uim.mobv_project.R
import sk.stuba.fei.uim.mobv_project.data.entities.Contact

class ContactsRecycleViewAdapter(
    var contacts: List<Contact>,
    private val listener: OnContactClickListener

) :
    RecyclerView.Adapter<ContactsRecycleViewAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_contact_card, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val contactsViewModel = contacts[position]

        // sets the image to the imageview from our itemHolder class
//        holder.imageView.setImageResource(ItemsViewModel.image)

        // sets the text to the textview from our itemHolder class
        holder.contactNameTextView.text = contactsViewModel.name
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return contacts.size
    }

    fun setData(newContacts: List<Contact>){
        val diffUtil = ContactListDiffUtil(contacts, newContacts)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        contacts = newContacts
        diffResults.dispatchUpdatesTo(this)
    }

    // Holds the views for adding it to image and text
   inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
//                val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val contactNameTextView: TextView = itemView.findViewById(R.id.contactCardName)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                listener.onContactClick(adapterPosition)
            }
        }
    }

    interface OnContactClickListener {
        fun onContactClick(position: Int)
    }
}