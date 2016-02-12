package com.fgundlac.ft_hangouts.Contacts;

import android.os.Build;
import android.telephony.PhoneNumberUtils;

import java.util.Locale;

/**
 * Created by flogu on 04/02/2016.
 */
public class Contact
{
	protected static final int NUMBER_COMP = 9;

	protected long      id;
	protected String    name;
	protected String    lastName;
	protected String	fullName;
	protected String    nickname;
	protected String    number;
	protected String    email;

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
		return fullName;
	}

	public void setFullName(String fullName)
	{
		this.fullName = getName() + " " + getLastName();
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
}
