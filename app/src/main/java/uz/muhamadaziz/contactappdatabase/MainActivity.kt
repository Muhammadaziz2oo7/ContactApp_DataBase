package uz.muhamadaziz.contactappdatabase

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import uz.muhamadaziz.contactappdatabase.DataBase.MyDataBase
import uz.muhamadaziz.contactappdatabase.Server.models.Contact
import uz.muhamadaziz.contactappdatabase.adapters.RVadapter
import uz.muhamadaziz.contactappdatabase.databinding.ActivityMainBinding
import uz.muhamadaziz.contactappdatabase.databinding.AddLayoutBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var myDataBase: MyDataBase
    private lateinit var contactList: ArrayList<Contact>
    private lateinit var adapter: RVadapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myDataBase = MyDataBase(this)

        contactList = myDataBase.getAllContactList() as ArrayList<Contact>
        adapter = RVadapter(object : RVadapter.OnItemClick {
            override fun onItemClick(contact: Contact, position: Int) {
                if (ActivityCompat.checkSelfPermission(this@MainActivity,android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this@MainActivity,
                        arrayOf(android.Manifest.permission.CALL_PHONE), 101)
                    return
                }
                val intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:${contact.phoneNumber}")
                startActivity(intent)

            }

            override fun onItemMenuClick(contact: Contact, position: Int, imageView: ImageView) {
                val popupMenu = PopupMenu(this@MainActivity, imageView)
                popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener {

                    when (it.itemId) {
                        R.id.edit -> {
                            val alertDialog = AlertDialog.Builder(this@MainActivity)

                            val create = alertDialog.create()
                            val bindingDialog = AddLayoutBinding.inflate(
                                LayoutInflater.from(this@MainActivity),
                                null,
                                false
                            )

                            create.setView(bindingDialog.root)
                            bindingDialog.name.setText(contact.name)
                            bindingDialog.phoneNumber.setText(contact.phoneNumber)

                            bindingDialog.add.setOnClickListener {

                                val name = bindingDialog.name.text.toString()
                                val phoneNumber = bindingDialog.phoneNumber.text.toString()

                                contact.name = name
                                contact.phoneNumber = phoneNumber

                                myDataBase.updateContact(contact)
                                adapter.notifyItemChanged(contactList.size)
                                adapter.notifyItemRangeChanged(position, contactList.size)
                                create.dismiss()
                            }
                            create.show()
                        }
                        R.id.delete -> {

                            val alertDialog = AlertDialog.Builder(this@MainActivity)

                            alertDialog.setMessage("Rostdan ham o'chirmoqchimisiz?")
                            val create = alertDialog.create()
                            alertDialog.setPositiveButton("Ha") { _, _ ->
                                myDataBase.deleteContact(contact)
                                contactList.remove(contact)
                                adapter.notifyItemChanged(contactList.size)
                                adapter.notifyItemRemoved(contactList.size)
                                adapter.notifyItemRangeRemoved(position, contactList.size)
                            }
                            alertDialog.setNegativeButton("Yo'q") { _, _ ->
                                create.dismiss()
                            }
                            alertDialog.show()
                        }
                    }
                    true
                }

                popupMenu.show()
            }
        }, contactList)
        binding.recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add -> {
                val alertDialog = AlertDialog.Builder(this)
                val create = alertDialog.create()
                val bindingDialog = AddLayoutBinding.inflate(LayoutInflater.from(this), null, false)
                create.setView(bindingDialog.root)
                bindingDialog.add.setOnClickListener {
                    val name: String = bindingDialog.name.text.toString()
                    val phoneNumber: String = bindingDialog.phoneNumber.text.toString()
                    val contact = Contact(name, phoneNumber)
                    contactList.add(contact)
                    myDataBase.addContact(contact)
                    adapter.notifyItemInserted(contactList.size)
                    create.dismiss()
                }
                bindingDialog.cancel.setOnClickListener {
                    create.dismiss()
                }
                create.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}