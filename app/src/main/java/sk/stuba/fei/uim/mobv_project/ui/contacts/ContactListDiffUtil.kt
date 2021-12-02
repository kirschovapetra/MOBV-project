package sk.stuba.fei.uim.mobv_project.ui.contacts

import androidx.recyclerview.widget.DiffUtil
import sk.stuba.fei.uim.mobv_project.data.entities.Contact

class ContactListDiffUtil(
    private val oldContacts: List<Contact>,
    private val newContacts: List<Contact>

): DiffUtil.Callback() {
    override fun getOldListSize() =
        oldContacts.size

    override fun getNewListSize() =
        newContacts.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldContacts[oldItemPosition].contactId == newContacts[newItemPosition].contactId

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldContacts[oldItemPosition] == newContacts[newItemPosition]
}