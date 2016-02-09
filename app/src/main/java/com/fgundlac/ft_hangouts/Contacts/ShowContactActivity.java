package com.fgundlac.ft_hangouts.Contacts;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
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
	int id;
	ContactsDataSource database;
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
			callButton.setVisibility(ImageButton.GONE);
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

		if ((id = getIntent().getIntExtra("com.fgundlac.ft_hangouts.contact.show", -1)) == -1)
			finish();

		database = new ContactsDataSource(this);
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		database.open();
		contact = database.getContact(Long.valueOf(id));
		database.close();

		setContactInfos(contact);
	}

	private void setContactInfos(Contact c)
	{
		String name = c.getName() + " " + c.getLastName();

		setTitle(name);
		nameTextView.setText(name);
		nicknameTextView.setText(c.getNickname());

		LinearLayout l;

		l = (LinearLayout) findViewById(R.id.numberLayout);
		if (c.getNumber() == null || c.getNumber().length() == 0)
		{
			l.setVisibility(LinearLayout.GONE);
		}
		else
		{
			numberTextView.setText(c.getNumber());
			l.setVisibility(LinearLayout.VISIBLE);
		}

		l = (LinearLayout) findViewById(R.id.emailLayout);
		if (c.getEmail() == null || c.getEmail().length() == 0)
		{
			l.setVisibility(LinearLayout.GONE);
		}
		else
		{
			emailTextView.setText(c.getEmail());
			l.setVisibility(LinearLayout.VISIBLE);
		}
	}

	protected void editContact()
	{
		Intent intent = new Intent(this, AddEditContactActivity.class);
		intent.putExtra("com.fgundlac.ft_hangouts.contact.edit", (int)contact.getId());
		startActivity(intent);
	}

	protected void deleteContact()
	{
		new AlertDialog.Builder(ShowContactActivity.this)
				.setTitle(getResources().getString(R.string.contact_delete_entry))
				.setMessage(getResources().getString(R.string.contact_delete_warning))
				.setPositiveButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						database.open();
						database.deleteContact(contact);
						database.close();
						finish();
					}
				})
				.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{

					}
				})
				.setIcon(R.drawable.ic_action_warning)
				.show();
	}

	public View.OnClickListener callOnClickListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact.getNumber()));
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
			if (intent.resolveActivity(getPackageManager()) != null)
				startActivity(intent);
			else
			{
				Snackbar snackbar = Snackbar.make(v, "There is no email client on your device !", Snackbar.LENGTH_SHORT);
				snackbar.show();
			}
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
