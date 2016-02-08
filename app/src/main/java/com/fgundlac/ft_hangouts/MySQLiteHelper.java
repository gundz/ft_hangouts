package com.fgundlac.ft_hangouts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by flogu on 08/02/2016.
 */
public class MySQLiteHelper extends SQLiteOpenHelper
{
	public static final String DATABASE_NAME =      "ft_hangouts.db";
	public static final int DATABASE_VERSION =      1;

	public static final String CONTACT_TABLE =     "contacts";
	public static final String CONTACTS_KEY =       "_id";
	public static final String CONTACT_NAME =       "name";
	public static final String CONTACT_LASTNAME =   "lastName";
	public static final String CONTACT_NICKNAME =   "nickname";
	public static final String CONTACT_NUMBER =     "number";
	public static final String CONTACT_EMAIL =      "email";
	public static final String CONTACT_TABLE_CREATE =
			"CREATE TABLE " + CONTACT_TABLE + "(" +
					CONTACTS_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ CONTACT_NAME + " TEXT, "
					+ CONTACT_LASTNAME + " TEXT, "
					+ CONTACT_NICKNAME + " TEXT, "
					+ CONTACT_NUMBER + " TEXT, "
					+ CONTACT_EMAIL + " TEXT "
			+ (");");

	public MySQLiteHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(CONTACT_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXITS " + CONTACT_TABLE);
		onCreate(db);
	}
}
