package sk.stuba.fei.uim.mobv_project.ui.contacts

import androidx.recyclerview.widget.DiffUtil
import sk.stuba.fei.uim.mobv_project.data.entities.Contact

class ContactListDiffUtil(
    private val oldContacts: List<Contact>,
    private val newContacts: List<Contact>

): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldContacts.size
    }

    override fun getNewListSize(): Int {
        return newContacts.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldContacts[oldItemPosition].contactId == newContacts[newItemPosition].contactId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldContacts[oldItemPosition].contactId != newContacts[newItemPosition].contactId -> {
                false
            }
            oldContacts[oldItemPosition].name != newContacts[newItemPosition].name -> {
                false
            }
            oldContacts[oldItemPosition].sourceAccount != newContacts[newItemPosition].sourceAccount -> {
                false
            }
            else -> true
        }
    }
}