package com.fgundlac.ft_hangouts.Contacts;

/**
 * Created by flogu on 04/02/2016.
 */
public class Contact
{
	protected String    name;
	protected String    lastName;
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
