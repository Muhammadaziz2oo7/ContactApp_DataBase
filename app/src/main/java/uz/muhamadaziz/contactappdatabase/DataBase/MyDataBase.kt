package uz.muhamadaziz.contactappdatabase.DataBase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import uz.muhamadaziz.contactappdatabase.Content.DB_NAME
import uz.muhamadaziz.contactappdatabase.Content.DB_VERSION
import uz.muhamadaziz.contactappdatabase.Content.ID
import uz.muhamadaziz.contactappdatabase.Content.NAME
import uz.muhamadaziz.contactappdatabase.Content.PHONE_NUMBER
import uz.muhamadaziz.contactappdatabase.Content.TABLE_NAME
import uz.muhamadaziz.contactappdatabase.Server.DatabaseService
import uz.muhamadaziz.contactappdatabase.Server.models.Contact

class MyDataBase(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION),
    DatabaseService {
    override fun onCreate(db: SQLiteDatabase?) {
        val query: String =
            "CREATE TABLE $TABLE_NAME($ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, $NAME TEXT NOT NULL, $PHONE_NUMBER NOT NULL)"
        db?.execSQL(query)

    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("drop table if exists $TABLE_NAME")
        onCreate(db)

    }

    override fun addContact(contact: Contact) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(NAME, contact.name)
        contentValues.put(PHONE_NUMBER, contact.phoneNumber)
        database.insert(TABLE_NAME, null, contentValues)
        database.close()
    }

    override fun getAllContactList(): List<Contact> {
        val contactList = ArrayList<Contact>()
        val query: String = "SELECT * FROM $TABLE_NAME"
        val database = this.readableDatabase
        val cursor = database.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val id: Int = cursor.getInt(0)
                val name: String = cursor.getString(1)
                val phoneNumber: String = cursor.getString(2)
                val contact = Contact(id, name, phoneNumber)
                contactList.add(contact)
            } while (cursor.moveToNext() )
        }
        return contactList
    }

    override fun updateContact(contact: Contact): Int {
        val database = this.writableDatabase
        val values = ContentValues()
        values.put(ID, contact.id)
        values.put(NAME, contact.name)
        values.put(PHONE_NUMBER, contact.phoneNumber)
        return database.update(TABLE_NAME, values, "$ID = ?", arrayOf("${contact.id}"))
    }

    override fun deleteContact(contact: Contact) {
        val database = this.writableDatabase
        database.delete(TABLE_NAME, "$ID = ?", arrayOf("${contact.id}"))
        database.close()
    }
}