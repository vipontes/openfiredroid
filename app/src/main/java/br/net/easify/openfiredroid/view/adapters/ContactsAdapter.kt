package br.net.easify.openfiredroid.view.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import br.net.easify.openfiredroid.R
import br.net.easify.openfiredroid.database.model.Contact
import br.net.easify.openfiredroid.databinding.ContactHolderBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class ContactsAdapter(private var listener: OnItemClick,
                      private var contacts: ArrayList<Contact>,
                      private var parentActivity: Activity)
    : RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    class ContactViewHolder(var view: ContactHolderBinding) :
        RecyclerView.ViewHolder(view.root)

    interface OnItemClick {
        fun onItemClick(contact: Contact)
        fun onItemDelete(contactId: Long)
    }

    fun updateContacts(newData: List<Contact>) {
        contacts.clear()
        contacts.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ContactHolderBinding>(
            inflater,
            R.layout.contact_holder,
            parent,
            false
        )
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]

        holder.itemView.setOnClickListener {
            listener.onItemClick(contact)
        }

        holder.view.contact = contact
    }

    fun removeItem(position: Int) {
        Snackbar.make(
            parentActivity.findViewById(R.id.frame_layout),
            parentActivity.getString(R.string.are_you_sure),
            2500
        )
            .setAction(parentActivity.getString(R.string.delete)) {
                val contact = contacts[position]
                listener.onItemDelete(contact.contact_id!!)
            }.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar?>() {
                override fun onDismissed(
                    transientBottomBar: Snackbar?,
                    event: Int
                ) {
                    super.onDismissed(transientBottomBar, event)
                    notifyItemChanged(position)
                }
            }).show()
    }

    override fun getItemCount() = contacts.size
}