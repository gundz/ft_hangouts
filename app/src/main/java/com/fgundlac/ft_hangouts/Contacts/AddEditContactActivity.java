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

	Contact                     contact = null;
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
		contact = new Contact();
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

	public void saveContact()
	{
		contact.setName(nameEditText.getText().toString());
		contact.setLastName(nameEditText.getText().toString());
		contact.setNickname(nicknameEditText.getText().toString());
		contact.setNumber(numberEditText.getText().toString());
		contact.setEmail(emailEditText.getText().toString());

		database.open();
		database.insert(contact);
		database.close();
	}

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
