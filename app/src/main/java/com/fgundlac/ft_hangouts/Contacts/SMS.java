package com.fgundlac.ft_hangouts.Contacts;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.PhoneNumberUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by flogu on 11/02/2016.
 */
public class SMS implements Parcelable
{

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(number);
		dest.writeString(content);
		dest.writeInt(type.getValue());
		dest.writeString(getDateFormated());
	}

	public static final Parcelable.Creator<SMS> CREATOR = new Parcelable.Creator<SMS>()
	{
		@Override
		public SMS createFromParcel(Parcel source)
		{
			return new SMS(source);
		}

		@Override
		public SMS[] newArray(int size)
		{
			return new SMS[size];
		}
	};

	public SMS (Parcel in)
	{
		this.number = in.readString();
		this.content = in.readString();
		this.type = Type.fromInt(in.readInt());
		setDateFormated(in.readString());
	}

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

	protected long id;
	protected String number;
	protected String content;
	protected Type type;
	protected Calendar date;

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

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getNumber()
	{
		return number;
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
