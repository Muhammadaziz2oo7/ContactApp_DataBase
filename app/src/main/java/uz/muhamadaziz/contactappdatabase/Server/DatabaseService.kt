package uz.muhamadaziz.contactappdatabase.Server

import androidx.activity.result.contract.ActivityResultContracts
import uz.muhamadaziz.contactappdatabase.Server.models.Contact

interface DatabaseService {

    fun addContact(contact: Contact)

    fun getAllContactList(): List<Contact>

    fun updateContact(contact: Contact): Int

    fun deleteContact(contact: Contact)
}