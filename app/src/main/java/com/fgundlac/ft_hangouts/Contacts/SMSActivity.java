package com.fgundlac.ft_hangouts.Contacts;

import android.content.Intent;
import android.media.Image;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
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

	ArrayList<SMS>          smsList = new ArrayList<SMS>();
	SMSListAdapter          smsListAdapter;
	ListView                listView;

	ContactsDataSource      contactDatabase;
	Contact                 contact;

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

		smsList.add(new SMS("0651358408", "sms de ouf !", SMS.Type.RECEIVED, Calendar.getInstance()));
		smsList.add(new SMS("0651358408", "sms de ouf !", SMS.Type.SENDED, Calendar.getInstance()));
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

			Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
			if (content.length() > 0 && content.length() < 160)
				manager.sendTextMessage(number, null, content, null, null);
		}
	};
}
