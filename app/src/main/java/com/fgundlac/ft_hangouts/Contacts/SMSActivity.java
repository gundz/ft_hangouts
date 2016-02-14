package com.fgundlac.ft_hangouts.Contacts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.fgundlac.ft_hangouts.BaseClass;
import com.fgundlac.ft_hangouts.R;

import java.util.ArrayList;
import java.util.Calendar;

public class SMSActivity extends BaseClass
{
	EditText                SMSContentEditText;
	ImageButton             SMSSendButton;

	ArrayList<SMS>          smsList;
	SMSListAdapter          smsListAdapter;
	ListView                listView;

	ContactsDataSource      contactDatabase;
	Contact                 contact;
	SMSDataSource           smsDatabase;

	private void initViews()
	{
		SMSContentEditText = (EditText) findViewById(R.id.SMSEditText);
		SMSSendButton = (ImageButton) findViewById(R.id.SMSSend);
		SMSSendButton.setOnClickListener(sendSMSOnClickListener);

		listView = (ListView) findViewById(R.id.smsListView);
	}

	private void init()
	{
		int id;
		if ((id = getIntent().getIntExtra("com.fgundlac.ft_hangouts.contact.sms", -1)) == -1)
			finish();

		contactDatabase = new ContactsDataSource(this);
		contactDatabase.open();
		contact = contactDatabase.getContact(Long.valueOf(id));
		contactDatabase.close();

		setTitle(contact.getFullName());

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
		smsListAdapter = new SMSListAdapter(this, smsList);
		listView.setAdapter(smsListAdapter);
		listView.setSelection(smsListAdapter.getCount() - 1);
	}

	public ArrayList<SMS> getSMSList()
	{
		ArrayList<SMS>      smsList;

		smsDatabase.open();
		smsList = smsDatabase.getSMSFromContact(contact);
		smsDatabase.close();
		return smsList;
	}

	public BroadcastReceiver receiveSMSBroadcast = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			if (intent.getAction().equals("com.fgundlac.ft_hangouts.sms.received"))
			{
				//smsList.add(sms);

				smsDatabase.open();
				smsList = smsDatabase.getSMSFromContact(contact);
				smsDatabase.close();
				smsListAdapter = new SMSListAdapter(SMSActivity.this, smsList);
				listView.setAdapter(smsListAdapter);
				smsListAdapter.notifyDataSetChanged();

				smsListAdapter.notifyDataSetChanged();
				listView.setSelection(smsListAdapter.getCount() - 1);
				/*
				ContactsDataSource c = new ContactsDataSource(context);
				if (c.checkIfNumberExits(sms.getNumber()) == false)
				{
					contactDatabase.open();
					contactDatabase.insert(new Contact(sms.getNumber(), sms.getNumber()));
					contactDatabase.close();
				}*/
			}
		}
	};

	public View.OnClickListener sendSMSOnClickListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			SmsManager manager = SmsManager.getDefault();

			String          number = contact.getNumber();
			String          content = SMSContentEditText.getText().toString();

			if (content.length() > 0 && content.length() < 160)
				manager.sendTextMessage(number, null, content, null, null);
			else
				return ;

			SMSContentEditText.setText("");

			smsDatabase.open();
			SMS sms = smsDatabase.insert(new SMS(number, content, SMS.Type.SENDED, Calendar.getInstance()));
			smsDatabase.close();
			smsList.add(sms);
			smsListAdapter.notifyDataSetChanged();
			listView.setSelection(smsListAdapter.getCount() - 1);
		}
	};
}
