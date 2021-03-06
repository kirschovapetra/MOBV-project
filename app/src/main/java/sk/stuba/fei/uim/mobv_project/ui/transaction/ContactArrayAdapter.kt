package sk.stuba.fei.uim.mobv_project.ui.transaction

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import sk.stuba.fei.uim.mobv_project.data.entities.Contact

// https://www.youtube.com/watch?v=z1gPVH7PspE
class ContactArrayAdapter(
    context: Context,
    contactsList: List<Contact>
) : ArrayAdapter<Contact>(context, 0, contactsList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createOrBindView(position, convertView, parent, android.R.layout.simple_spinner_item)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createOrBindView(position, convertView, parent, android.R.layout.simple_spinner_dropdown_item)
    }

    private fun createOrBindView(position: Int, convertView: View?, parent: ViewGroup, viewResource: Int): View {
        val contact = getItem(position)

        return if (convertView == null) {
            createView(contact, parent, viewResource)
        } else {
            bindView(contact, convertView)
        }
    }

    private fun createView(contact: Contact?, parent: ViewGroup, viewResource: Int): View {
        val view: TextView = LayoutInflater.from(context)
                                 .inflate(viewResource, parent, false)
                                 as TextView
        return bindView(contact, view)
    }

    private fun bindView(contact: Contact?, view: View): View {
        (view as TextView).text = contact?.name.orEmpty()

        return view
    }
}