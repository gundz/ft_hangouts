package com.fgundlac.ft_hangouts.Contacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.fgundlac.ft_hangouts.MySQLiteHelper;

import java.util.ArrayList;

public class SMSDataSource
{
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;

	public SMSDataSource(Context context)
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

	public SMS insert(SMS sms)
	{
		ContentValues value = new ContentValues();

		value.put(MySQLiteHelper.SMS_NUMBER, sms.getNumber());
		value.put(MySQLiteHelper.SMS_CONTENT, sms.getContent());
		value.put(MySQLiteHelper.SMS_TYPE, sms.getType().getValue());
		value.put(MySQLiteHelper.SMS_DATE, sms.getDateFormated());
		long id = database.insert(MySQLiteHelper.SMS_TABLE, null, value);

		return getSMS(id);
	}

	public void deleteSMS(SMS sms)
	{
		database.delete(MySQLiteHelper.SMS_TABLE, MySQLiteHelper.SMS_KEY + " = " + sms.getId(), null);
	}

	public SMS getSMS(long id)
	{
		Cursor cursor = database.rawQuery("SELECT * FROM " + MySQLiteHelper.SMS_TABLE + " WHERE _id = ?", new String[]{String.valueOf(id)});

		cursor.moveToFirst();
		if (cursor.getCount() == 0)
		{
			return null;
		}
		return cursorToSMS(cursor);
	}

	public ArrayList<SMS> getSMSFromContact(Contact contact)
	{
		ArrayList<SMS> smsList = new ArrayList<>();
		Cursor cursor = database.rawQuery("SELECT * FROM " + MySQLiteHelper.SMS_TABLE, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast())
		{
			SMS sms = cursorToSMS(cursor);
			if (Contact.compareNumber(sms.getNumber(), contact.getNumber()))
			{
				smsList.add(sms);
			}
			cursor.moveToNext();
		}
		cursor.close();
		return smsList;
	}

	private SMS cursorToSMS(Cursor cursor)
	{
		SMS sms = new SMS();
		int i = 0;

		sms.setId(cursor.getLong(i++));
		sms.setNumber(cursor.getString(i++));
		sms.setContent(cursor.getString(i++));
		sms.setType(SMS.Type.fromInt(cursor.getInt(i++)));
		sms.setDateFormated(cursor.getString(i));
		return sms;
	}
}
