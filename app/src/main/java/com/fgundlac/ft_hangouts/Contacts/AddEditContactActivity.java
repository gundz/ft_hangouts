package com.fgundlac.ft_hangouts.Contacts;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.fgundlac.ft_hangouts.BaseClass;
import com.fgundlac.ft_hangouts.R;

public class AddEditContactActivity extends BaseClass
{
	EditText                    nameEditText;
	EditText                    lastNameEditText;
	EditText                    nicknameEditText;
	EditText                    numberEditText;
	EditText                    emailEditText;
	FloatingActionButton        saveContactButton;

	Contact                     contact;
	int                         id;
	ContactsDataSource          database;

	private void initViews()
	{
		nameEditText = (EditText) findViewById(R.id.nameEditText);
		lastNameEditText = (EditText) findViewById(R.id.lastNameEditText);
		nicknameEditText = (EditText) findViewById(R.id.nicknameEditText);
		numberEditText = (EditText) findViewById(R.id.numberEditText);
		emailEditText = (EditText) findViewById(R.id.emailEditText);
		saveContactButton = (FloatingActionButton) findViewById(R.id.saveContactButton);
		saveContactButton.setOnClickListener(saveContactButtonListener);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_edit_contact);
		initViews();

		database = new ContactsDataSource(this);
		if ((id = getIntent().getIntExtra("com.fgundlac.ft_hangouts.contact.edit", -1)) != -1)
		{
			database.open();
			contact = database.getContact((long) id);
			database.close();
			setContactInfos(contact);
		}
		else
			contact = new Contact();
	}

	private void setContactInfos(Contact c)
	{
		nameEditText.setText(c.getName());
		lastNameEditText.setText(c.getLastName());
		nicknameEditText.setText(c.getNickname());
		numberEditText.setText(c.getNumber());
		emailEditText.setText(c.getEmail());
	}

	public void saveContact()
	{
		contact.setName(nameEditText.getText().toString());
		contact.setLastName(lastNameEditText.getText().toString());
		contact.setNickname(nicknameEditText.getText().toString());
		contact.setNumber(numberEditText.getText().toString());
		contact.setEmail(emailEditText.getText().toString());

		database.open();
		if (id == -1)
			database.insert(contact);
		else
			database.update(contact);
		database.close();
	}

	public View.OnClickListener saveContactButtonListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			saveContact();
			finish();
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.contact_add_edit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.action_contact_cancel:
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
