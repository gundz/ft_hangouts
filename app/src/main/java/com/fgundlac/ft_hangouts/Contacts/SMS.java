package com.fgundlac.ft_hangouts.Contacts;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by flogu on 11/02/2016.
 */
public class SMS
{

	public enum Type
	{
		RECEIVED(0), SENDED(1);

		private int _value;

		Type(int value)
		{
			this._value = value;
		}

		public int getValue()
		{
			return _value;
		}

		public static Type fromInt(int i)
		{
			for (Type b : Type.values())
			{
				if (b.getValue() == i)
					return b;
			}
			return null;
		}
	}

	long id;
	String number;
	String content;
	Type type;
	Calendar date;

	public SMS()
	{

	}

	public SMS(String number, String content, Type type, Calendar date)
	{
		this.number = number;
		this.content = content;
		this.type = type;
		this.date = date;
	}

	public String getNumber()
	{
		return number;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public void setNumber(String number)
	{
		this.number = number;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public Type getType()
	{
		return type;
	}

	public void setType(Type type)
	{
		this.type = type;
	}

	public Calendar getDate()
	{
		return date;
	}

	public String getDateFormated()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
		return dateFormat.format(getDate().getTime());
	}

	public void setDateFormated(String date)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());

		Calendar c = Calendar.getInstance();
		try
		{
			c.setTime(dateFormat.parse(date));
		}
		catch (ParseException e)
		{
		}
		setDate(c);
	}

	public void setDate(Calendar date)
	{
		this.date = date;
	}
}
