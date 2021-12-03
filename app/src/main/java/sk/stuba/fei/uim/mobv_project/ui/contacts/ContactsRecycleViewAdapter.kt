package sk.stuba.fei.uim.mobv_project.ui.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import sk.stuba.fei.uim.mobv_project.data.entities.Contact
import sk.stuba.fei.uim.mobv_project.databinding.FragmentContactsCardBinding

class ContactsRecycleViewAdapter(
    var contacts: List<Contact>,
    private val listener: OnContactClickListener

) :
    RecyclerView.Adapter<ContactsRecycleViewAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            FragmentContactsCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(contacts[position])

    // return the number of the items in the list
    override fun getItemCount() =
        contacts.size

    fun setData(newContacts: List<Contact>) {
        val diffUtil = ContactListDiffUtil(contacts, newContacts)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        contacts = newContacts
        diffResults.dispatchUpdatesTo(this)
    }

    // Holds the views for adding it to image and text
    inner class ViewHolder(private val binding: FragmentContactsCardBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(contact: Contact) {
            binding.apply {
                contactCardName.text = contact.name
                contactCardAccountId.text = contact.contactId
            }
        }

        override fun onClick(view: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onContactClick(contacts[adapterPosition])
                notifyItemChanged(adapterPosition)
            }
        }
    }

    interface OnContactClickListener {
        fun onContactClick(contact: Contact)
    }
}