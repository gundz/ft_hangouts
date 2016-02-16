package com.fgundlac.ft_hangouts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
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
	ContactsDataSource database;
	ArrayList<Contact> contactList = new ArrayList<Contact>();
	ContactsListAdapter contactListAdapter;
	ListView contactListView;
	FloatingActionButton addContactButton;

	private void initViews()
	{
		contactListView = (ListView) findViewById(R.id.contactListView);
		addContactButton = (FloatingActionButton) findViewById(R.id.addContactButton);
		addContactButton.setOnClickListener(addContactListener);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
		database = new ContactsDataSource(this);
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		IntentFilter intFilt = new IntentFilter("com.fgundlac.ft_hangouts.contact.added");
		registerReceiver(addContactBroadcast, intFilt);

		database.open();
		contactList = database.getAllContacts();
		database.close();

		contactListAdapter = new ContactsListAdapter(this, contactList);
		contactListView.setAdapter(contactListAdapter);
		contactListView.setOnItemClickListener(contactClickListener);
		registerForContextMenu(contactListView);
		contactListAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		unregisterReceiver(addContactBroadcast);
	}

	public AdapterView.OnItemClickListener contactClickListener = new AdapterView.OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			Intent intent = new Intent(MainActivity.this, ShowContactActivity.class);
			intent.putExtra("com.fgundlac.ft_hangouts.contact.show", (int) contactList.get(position).getId());
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

	public BroadcastReceiver addContactBroadcast = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			if (intent.getAction().equals("com.fgundlac.ft_hangouts.contact.added"))
			{
				Contact c = intent.getParcelableExtra("com.fgundlac.ft_hangouts.contact.added.contact");
				if (c != null)
				{
					contactList.add(c);
					contactListAdapter.notifyDataSetChanged();
				}
			}
		}
	};

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.setHeaderTitle("Select The Action");
		menu.add(Menu.NONE, v.getId(), 0, "Edit");
		menu.add(Menu.NONE, v.getId(), 0, "Delete");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		final AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		final Contact c = contactList.get((int) menuInfo.id);

		if (item.getTitle() == "Edit")
		{
			Intent intent = new Intent(this, AddEditContactActivity.class);
			intent.putExtra("com.fgundlac.ft_hangouts.contact.edit", (int) c.getId());
			startActivity(intent);
		}
		else if(item.getTitle() == "Delete")
		{
			new AlertDialog.Builder(MainActivity.this)
					.setTitle(getResources().getString(R.string.contact_delete_entry))
					.setMessage(getResources().getString(R.string.contact_delete_warning))
					.setPositiveButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							database.open();
							database.deleteContact(c);
							database.close();
							contactList.remove(menuInfo.id);
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
		else
		{
			return false;
		}
		return true;
	}
}
