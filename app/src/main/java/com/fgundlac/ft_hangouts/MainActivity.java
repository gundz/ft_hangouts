package com.fgundlac.ft_hangouts;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fgundlac.ft_hangouts.Contacts.AddEditContactActivity;
import com.fgundlac.ft_hangouts.Contacts.Contact;
import com.fgundlac.ft_hangouts.Contacts.ContactsDataSource;
import com.fgundlac.ft_hangouts.Contacts.ContactsListAdapter;
import com.fgundlac.ft_hangouts.Contacts.ShowContactActivity;

import java.util.ArrayList;

public class MainActivity extends BaseClass
{
	ArrayList<Contact>          contactList = new ArrayList<Contact>();
	ContactsListAdapter         contactListAdapter;
	ListView                    listView;
	FloatingActionButton        addContactButton;

	private void initViews()
	{
		listView = (ListView) findViewById(R.id.contactListView);
		addContactButton = (FloatingActionButton) findViewById(R.id.addContactButton);
		addContactButton.setOnClickListener(addContactListener);
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

		ContactsDataSource              db = new ContactsDataSource(this);

		db.open();
		contactList = db.getAllContacts();
		db.close();

		contactListAdapter = new ContactsListAdapter(this, contactList);
		listView.setAdapter(contactListAdapter);
		listView.setOnItemClickListener(contactClickListener);
		contactListAdapter.notifyDataSetChanged();
	}

	public AdapterView.OnItemClickListener contactClickListener = new AdapterView.OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			Intent intent = new Intent(MainActivity.this, ShowContactActivity.class);
			intent.putExtra("com.fgundlac.ft_hangouts.contact.show", (int)contactList.get(position).getId());
			startActivity(intent);
		}
	};

	public View.OnClickListener addContactListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			Intent intent = new Intent(MainActivity.this, AddEditContactActivity.class);
			startActivity(intent);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
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
