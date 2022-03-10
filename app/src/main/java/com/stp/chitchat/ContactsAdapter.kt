package com.stp.chitchat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stp.chitchat.databinding.ContactListItemBinding

class ContactsAdapter(private val listener: ContactClicked,
                      private val appContacts: ArrayList<UserModel>): RecyclerView.Adapter<ContactsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val adapterLayout = ContactListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactsViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val currentItem = appContacts[position]
        holder.binding.contactsUserModel = currentItem

        holder.itemView.setOnClickListener{
            listener.onContactClicked(currentItem)

        }

    }

    override fun getItemCount() = appContacts.size
}

interface ContactClicked {
    fun onContactClicked(item: UserModel)
}

class ContactsViewHolder(val binding: ContactListItemBinding): RecyclerView.ViewHolder(binding.root) {

}
