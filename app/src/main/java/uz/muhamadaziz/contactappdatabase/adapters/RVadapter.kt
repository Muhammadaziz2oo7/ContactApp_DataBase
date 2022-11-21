package uz.muhamadaziz.contactappdatabase.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import uz.muhamadaziz.contactappdatabase.Server.models.Contact
import uz.muhamadaziz.contactappdatabase.databinding.ContactItemBinding

class RVadapter(var onItemClick: OnItemClick, var contactList: ArrayList<Contact>) :
    RecyclerView.Adapter<RVadapter.VH>() {

    inner class VH(private val binding: ContactItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(contact: Contact, position: Int) {
            binding.name.text = contact.name
            binding.phoneNumber.text = contact.phoneNumber

            binding.root.setOnClickListener {
                onItemClick.onItemClick(contact, position)
            }

            binding.menu.setOnClickListener {
                onItemClick.onItemMenuClick(contact, position, binding.menu)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(contactList[position], position)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    interface OnItemClick {
        fun onItemClick(contact: Contact, position: Int)
        fun onItemMenuClick(contact: Contact, position: Int, imageView: ImageView)
    }
}