package com.fgundlac.ft_hangouts.Contacts;

import java.util.Calendar;

/**
 * Created by flogu on 11/02/2016.
 */
public class SMS
{
	public enum Type
	{
		SENDED, RECEIVED
	};

	String number;
	String content;
	Type type;
	Calendar date;

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

	public void setDate(Calendar date)
	{
		this.date = date;
	}
}
