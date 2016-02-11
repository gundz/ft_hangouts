package com.fgundlac.ft_hangouts.Contacts;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.fgundlac.ft_hangouts.BaseClass;
import com.fgundlac.ft_hangouts.R;

import java.util.ArrayList;
import java.util.Calendar;

public class SMSActivity extends BaseClass
{
	EditText                SMSContentEditText;
	ImageButton             SMSSendButton;

	ArrayList<SMS>          smsList = new ArrayList<SMS>();
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

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sms);
		initViews();

		contactDatabase = new ContactsDataSource(this);
		int id;
		if ((id = getIntent().getIntExtra("com.fgundlac.ft_hangouts.contact.sms", -1)) == -1)
			finish();
		contactDatabase.open();
		contact = contactDatabase.getContact(Long.valueOf(id));
		contactDatabase.close();

		smsDatabase = new SMSDataSource(this);

		smsDatabase.open();
		smsList = smsDatabase.getSMSFromContact(contact);
		smsDatabase.close();

		smsListAdapter = new SMSListAdapter(this, smsList);
		listView.setAdapter(smsListAdapter);
	}

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

			smsDatabase.open();
			SMS sms = smsDatabase.insert(new SMS(number, content, SMS.Type.SENDED, Calendar.getInstance()));
			smsDatabase.close();
			smsList.add(sms);
			smsListAdapter.notifyDataSetChanged();
		}
	};
}
