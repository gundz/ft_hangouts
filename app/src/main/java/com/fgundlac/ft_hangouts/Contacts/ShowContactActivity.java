package com.fgundlac.ft_hangouts.Contacts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fgundlac.ft_hangouts.BaseClass;
import com.fgundlac.ft_hangouts.R;

public class ShowContactActivity extends BaseClass
{
	Contact contact;
	TextView nameTextView;
	TextView nicknameTextView;
	TextView numberTextView;
	TextView emailTextView;

	ImageButton callButton;
	ImageButton smsButton;
	ImageButton emailButton;

	private void initViews()
	{
		nameTextView = (TextView) findViewById(R.id.nameTextView);
		nicknameTextView = (TextView) findViewById(R.id.nicknameTextView);
		numberTextView = (TextView) findViewById(R.id.numberTextView);
		emailTextView = (TextView) findViewById(R.id.emailTextView);

		if (ActivityCompat.checkSelfPermission(ShowContactActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
		{
			smsButton.setVisibility(ImageButton.GONE);
		}
		else
		{
			callButton = (ImageButton) findViewById(R.id.callButton);
			callButton.setOnClickListener(callOnClickListener);
		}

		if (ActivityCompat.checkSelfPermission(ShowContactActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
		{
			smsButton.setVisibility(ImageButton.GONE);
		}
		else
		{
			smsButton = (ImageButton) findViewById(R.id.smsButton);
			smsButton.setOnClickListener(smsOnClickListener);
		}

		emailButton = (ImageButton) findViewById(R.id.emailButton);
		emailButton.setOnClickListener(emailOnClickListener);
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
		String name = c.getName() + " " + c.getLastName();

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

	protected void editContact()
	{

	}

	protected void deleteContact()
	{

	}

	public View.OnClickListener callOnClickListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(contact.getNumber()));
			if (ActivityCompat.checkSelfPermission(ShowContactActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
				return;
			startActivity(intent);
		}
	};

	public View.OnClickListener smsOnClickListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{

		}
	};

	public View.OnClickListener emailOnClickListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + contact.getEmail()));
			startActivity(intent);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.contact_show, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.action_contact_edit:
				editContact();
				return true;
			case R.id.action_contact_delete:
				deleteContact();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
