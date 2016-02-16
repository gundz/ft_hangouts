package com.fgundlac.ft_hangouts.Contacts;

import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.PhoneNumberUtils;

import java.util.Locale;

public class Contact implements Parcelable
{
	protected static final int NUMBER_COMP = 9;

	protected long      id;
	protected String    name = "";
	protected String    lastName = "";
	protected String    nickname = "";
	protected String    number = "";
	protected String    email = "";

	public Contact()
	{
	}

	public Contact(String name, String number)
	{
		this.name = name;
		this.number = number;
	}

	static public boolean compareNumber(String a, String b)
	{
		if (Build.VERSION.SDK_INT >= 21)
		{
			a = PhoneNumberUtils.formatNumberToE164(a, Locale.getDefault().getCountry());
			b = PhoneNumberUtils.formatNumberToE164(b, Locale.getDefault().getCountry());
			if (PhoneNumberUtils.compare(a, b))
				return true;
		}
		else
		{
			a = new StringBuilder(a).reverse().toString();
			b = new StringBuilder(b).reverse().toString();
			int i = 0;
			while (i < a.length() && i < b.length() && a.charAt(i) == b.charAt(i))
				i++;
			return (i >= NUMBER_COMP);
		}
		return false;
	}

	public void deleteOnBDD(Context context)
	{
		ContactsDataSource contactDatabase = new ContactsDataSource(context);

		contactDatabase.open();
		contactDatabase.deleteContact(this);
		contactDatabase.close();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeInt((int)id);
		dest.writeString(name);
		dest.writeString(lastName);
		dest.writeString(nickname);
		dest.writeString(number);
		dest.writeString(email);
	}

	public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>()
	{
		@Override
		public Contact createFromParcel(Parcel source)
		{
			return new Contact(source);
		}

		@Override
		public Contact[] newArray(int size)
		{
			return new Contact[size];
		}
	};

	public Contact (Parcel in)
	{
		this.id = in.readInt();
		this.name = in.readString();
		this.lastName = in.readString();
		this.nickname = in.readString();
		this.number = in.readString();
		this.email = in.readString();
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public String getFullName()
	{
		return name + " " + lastName;
	}

	public String getNumber()
	{
		return number;
	}

	public void setNumber(String number)
	{
		this.number = number;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}
}
