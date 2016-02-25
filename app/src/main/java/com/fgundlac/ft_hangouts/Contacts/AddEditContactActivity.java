package com.fgundlac.ft_hangouts.Contacts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.fgundlac.ft_hangouts.BaseClass;
import com.fgundlac.ft_hangouts.R;

public class AddEditContactActivity extends BaseClass
{
	public View.OnClickListener photoImageButtonOnClickListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			Intent i = new Intent(getApplicationContext(), ContactPhoto.class);
			startActivityForResult(i, ContactPhoto.CAMERA_REQUEST);
		}
	};
	EditText nameEditText;
	EditText lastNameEditText;
	EditText nicknameEditText;
	EditText numberEditText;
	EditText emailEditText;
	ImageButton photoImageButton;
	FloatingActionButton saveContactButton;
	Contact contact;
	int id;
	ContactsDataSource database;
	public View.OnClickListener saveContactButtonListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			saveContact();
			finish();
		}
	};

	private void initViews()
	{
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		nameEditText = (EditText) findViewById(R.id.nameEditText);
		lastNameEditText = (EditText) findViewById(R.id.lastNameEditText);
		nicknameEditText = (EditText) findViewById(R.id.nicknameEditText);
		numberEditText = (EditText) findViewById(R.id.numberEditText);
		emailEditText = (EditText) findViewById(R.id.emailEditText);
		photoImageButton = (ImageButton) findViewById(R.id.takePhoto);
		photoImageButton.setOnClickListener(photoImageButtonOnClickListener);
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
		{
			contact = new Contact();
		}
	}

	private void setContactInfos(Contact c)
	{
		getSupportActionBar().setTitle(c.getFullName());
		nameEditText.setText(c.getName());
		lastNameEditText.setText(c.getLastName());
		nicknameEditText.setText(c.getNickname());
		numberEditText.setText(c.getNumber());
		emailEditText.setText(c.getEmail());
		photoImageButton.setImageBitmap(ContactPhoto.loadImageFromStorage(this, contact.getPhotoName()));
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
		{
			database.insert(contact);
		}
		else
		{
			database.update(contact);
		}
		database.close();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == ContactPhoto.CAMERA_REQUEST && resultCode == RESULT_OK)
		{
			String photoName = data.getStringExtra("com.fgundlac.ft_hangouts.camera.photoName");
			contact.setPhotoName(photoName);

			Bitmap photo = ContactPhoto.loadImageFromStorage(this, contact.getPhotoName());
			if (photo != null)
			{
				photoImageButton.setImageBitmap(photo);
			}
		}
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
