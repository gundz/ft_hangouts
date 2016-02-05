package com.fgundlac.ft_hangouts.Contacts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fgundlac.ft_hangouts.BaseClass;
import com.fgundlac.ft_hangouts.R;

import org.w3c.dom.Text;

public class ShowContactActivity extends BaseClass
{
	Contact             contact;
	TextView            nameTextView;
	TextView            nicknameTextView;
	TextView            numberTextView;
	TextView            emailTextView;

	private void initViews()
	{
		nameTextView = (TextView) findViewById(R.id.nameTextView);
		nicknameTextView = (TextView) findViewById(R.id.nicknameTextView);
		numberTextView = (TextView) findViewById(R.id.numberTextView);
		emailTextView = (TextView) findViewById(R.id.emailTextView);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_contact);
		initViews();

		contact = new Contact();
		contact.setName("Gundlack");
		contact.setLastName("Florian");
		contact.setNickname("Gundz");
		contact.setNumber("+33651358408");
		contact.setEmail("flogundlack@gmail.com");

		setContactInfos(contact);
	}

	private void setContactInfos(Contact c)
	{
		String          name = c.getName() + " " + c.getLastName();

		setTitle(name);
		nameTextView.setText(name);
		nicknameTextView.setText(c.getNickname());

		if (c.getNumber() == null || c.getNumber().length() == 0)
		{
			LinearLayout l = (LinearLayout) findViewById(R.id.numberLayout);
			l.setVisibility(LinearLayout.GONE);
		}
		else
			numberTextView.setText(c.getNumber());

		if (c.getEmail() == null || c.getNumber().length() == 0)
		{
			LinearLayout l = (LinearLayout) findViewById(R.id.emailLayout);
			l.setVisibility(LinearLayout.GONE);
		}
		else
			emailTextView.setText(c.getEmail());
	}
}
