package com.fgundlac.ft_hangouts;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.fgundlac.ft_hangouts.Contacts.Contact;
import com.fgundlac.ft_hangouts.Contacts.ContactsListAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseClass
{
	ArrayList<Contact>          contactList = new ArrayList<Contact>();
	ContactsListAdapter         contactListAdapter;
	ListView                    listView;

	private void initViews()
	{
		listView = (ListView) findViewById(R.id.contactListView);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		for (int i = 0; i < 10; i++)
		{
			Contact c = new Contact("Name " + String.valueOf(i), "0651358408");
			c.setLastName("last name");
			contactList.add(c);
		}

		contactListAdapter = new ContactsListAdapter(this, contactList);
		listView.setAdapter(contactListAdapter);
		contactListAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater    inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.action_settings:
				showSettings();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void showSettings()
	{
		Intent intent = new Intent(this, PreferenceActivity.class);
		startActivity(intent);
	}
}
