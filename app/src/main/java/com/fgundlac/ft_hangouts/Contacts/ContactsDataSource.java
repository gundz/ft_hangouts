package com.fgundlac.ft_hangouts.Contacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.fgundlac.ft_hangouts.MySQLiteHelper;

import java.util.ArrayList;

/**
 * Created by flogu on 08/02/2016.
 */
public class ContactsDataSource
{
	private SQLiteDatabase          database;
	private MySQLiteHelper          dbHelper;

	public ContactsDataSource(Context context)
	{
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLiteException
	{
		database = dbHelper.getWritableDatabase();
	}

	public void close()
	{
		dbHelper.close();
	}

	public ContentValues contactToContentValues(Contact contact)
	{
		ContentValues               value = new ContentValues();

		value.put(MySQLiteHelper.CONTACT_NAME, contact.getName());
		value.put(MySQLiteHelper.CONTACT_LASTNAME, contact.getLastName());
		value.put(MySQLiteHelper.CONTACT_NICKNAME, contact.getNickname());
		value.put(MySQLiteHelper.CONTACT_NUMBER, contact.getNumber());
		value.put(MySQLiteHelper.CONTACT_EMAIL, contact.getEmail());
		return value;
	}

	public Contact insert(Contact contact)
	{
		ContentValues               value = contactToContentValues(contact);
		long id = database.insert(MySQLiteHelper.CONTACT_TABLE, null, value);
		return getContact(id);
	}

	public void update(Contact contact)
	{
		ContentValues               value = contactToContentValues(contact);

		database.update(MySQLiteHelper.CONTACT_TABLE, value, MySQLiteHelper.CONTACTS_KEY + " = ?", new String[]{String.valueOf(contact.getId())});
	}

	public void deleteContact(Contact contact)
	{
		database.delete(MySQLiteHelper.CONTACT_TABLE, MySQLiteHelper.CONTACTS_KEY + " = " + contact.getId(), null);
	}

	public Contact getContact(long id)
	{
		Contact                     contact = new Contact();
		Cursor                      cursor = database.rawQuery("SELECT * FROM " + MySQLiteHelper.CONTACT_TABLE + " WHERE _id = ?", new String[]{String.valueOf(id)});

		cursor.moveToFirst();
		if (cursor.getCount() == 0)
			return null;
		return cursorToContact(cursor);
	}

	public boolean checkIfNumberExits(String number)
	{
        ArrayList<Contact>          contactList = new ArrayList<Contact>();

        this.open();
        contactList = this.getAllContacts();
        this.close();
        for (int i = 0; i < contactList.size(); i++)
        {
            if (Contact.compareNumber(number, contactList.get(i).getNumber()))
                return true;
        }
        return false;
    }

	public ArrayList<Contact> getAllContacts()
	{
		ArrayList<Contact>          contacts = new ArrayList<Contact>();
		Cursor                      cursor = database.rawQuery("SELECT * FROM " + MySQLiteHelper.CONTACT_TABLE, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast())
		{
			Contact contact = cursorToContact(cursor);
			contacts.add(contact);
			cursor.moveToNext();
		}
		cursor.close();
		return contacts;
	}

	private Contact cursorToContact(Cursor cursor)
	{
		Contact                     contact = new Contact();
		int                         i = 0;

		contact.setId(cursor.getLong(i++));
		contact.setName(cursor.getString(i++));
		contact.setLastName(cursor.getString(i++));
		contact.setNickname(cursor.getString(i++));
		contact.setNumber(cursor.getString(i++));
		contact.setEmail(cursor.getString(i++));
		return contact;
	}
}
