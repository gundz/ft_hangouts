package com.fgundlac.ft_hangouts.Contacts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.fgundlac.ft_hangouts.BaseClass;
import com.fgundlac.ft_hangouts.R;

import java.util.ArrayList;
import java.util.Calendar;

public class SMSActivity extends BaseClass
{
	public static final int MENU_DELETE = 1;
	EditText SMSContentEditText;
	ImageButton SMSSendButton;
	ArrayList<SMS> smsList;
	SMSListAdapter smsListAdapter;
	ListView smsListView;
	public BroadcastReceiver receiveSMSBroadcast = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			if (intent.getAction().equals("com.fgundlac.ft_hangouts.sms.received"))
			{
				SMS sms = intent.getParcelableExtra("com.fgundlac.ft_hangouts.sms.received.sms");
				if (sms != null)
				{
					smsList.add(sms);
					smsListAdapter.notifyDataSetChanged();
					listViewMoveToLast();
				}
			}
		}
	};
	ContactsDataSource contactDatabase;
	Contact contact;
	SMSDataSource smsDatabase;
	public View.OnClickListener sendSMSOnClickListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			SmsManager manager = SmsManager.getDefault();

			String number = contact.getNumber();
			String content = SMSContentEditText.getText().toString();

			if (content.length() > 0 && content.length() < 160)
			{
				manager.sendTextMessage(number, null, content, null, null);
			}
			else
			{
				return;
			}

			SMSContentEditText.setText("");

			smsDatabase.open();
			SMS sms = smsDatabase.insert(new SMS(number, content, SMS.Type.SENDED, Calendar.getInstance()));
			smsDatabase.close();
			smsList.add(sms);
			smsListAdapter.notifyDataSetChanged();
			listViewMoveToLast();
		}
	};

	private void initViews()
	{
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		SMSContentEditText = (EditText) findViewById(R.id.SMSEditText);
		SMSSendButton = (ImageButton) findViewById(R.id.SMSSend);
		SMSSendButton.setOnClickListener(sendSMSOnClickListener);
		smsListView = (ListView) findViewById(R.id.smsListView);
	}

	private void init()
	{
		int id;
		if ((id = getIntent().getIntExtra("com.fgundlac.ft_hangouts.contact.sms", -1)) == -1)
		{
			finish();
		}

		contactDatabase = new ContactsDataSource(this);
		contactDatabase.open();
		contact = contactDatabase.getContact((long) id);
		contactDatabase.close();

		getSupportActionBar().setTitle(contact.getFullName());

		smsDatabase = new SMSDataSource(this);

		IntentFilter intFilt = new IntentFilter("com.fgundlac.ft_hangouts.sms.received");
		registerReceiver(receiveSMSBroadcast, intFilt);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sms);
		initViews();
		init();
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		smsList = getSMSList();
		smsListAdapter = new SMSListAdapter(this, smsList, contact);
		smsListView.setAdapter(smsListAdapter);
		registerForContextMenu(smsListView);
		listViewMoveToLast();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		unregisterReceiver(receiveSMSBroadcast);
	}

	public ArrayList<SMS> getSMSList()
	{
		ArrayList<SMS> smsList;

		smsDatabase.open();
		smsList = smsDatabase.getSMSFromContact(contact);
		smsDatabase.close();
		return smsList;
	}

	public void listViewMoveToLast()
	{
		smsListView.setSelection(smsListAdapter.getCount() - 1);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.setHeaderTitle(getResources().getString(R.string.choose_action));
		menu.add(Menu.NONE, MENU_DELETE, 0, getResources().getString(R.string.delete));
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		final AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		final SMS sms = smsList.get((int) menuInfo.id);

		if (item.getItemId() == MENU_DELETE)
		{
			new AlertDialog.Builder(SMSActivity.this)
					.setTitle(getResources().getString(R.string.contact_delete_entry))
					.setMessage(getResources().getString(R.string.contact_delete_warning))
					.setPositiveButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							sms.deleteOnBDD(getApplicationContext());
							smsList.remove((int) menuInfo.id);
							smsListAdapter.notifyDataSetChanged();
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			// Respond to the action bar's Up/Home button
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
