package com.fgundlac.ft_hangouts.Contacts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.util.Calendar;

public class SMSBroadcastReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
		{
			Bundle bundle = intent.getExtras();
			if (bundle != null)
			{
				Object[] pdus = (Object[]) bundle.get("pdus");
				SmsMessage[] messages = new SmsMessage[pdus.length];
				for (int i = 0; i < pdus.length; i++)
				{
					if (Build.VERSION.SDK_INT >= 23)
					{
						messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], "3gpp");
					}
					else
					{
						messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
					}
				}
				for (SmsMessage message : messages)
				{
					String strMessageFrom = message.getDisplayOriginatingAddress();
					String strMessageBody = message.getDisplayMessageBody();

					SMSDataSource smsDatabase = new SMSDataSource(context);
					smsDatabase.open();
					SMS sms = smsDatabase.insert(new SMS(strMessageFrom, strMessageBody, SMS.Type.RECEIVED, Calendar.getInstance()));
					smsDatabase.close();

					Intent i = new Intent("com.fgundlac.ft_hangouts.sms.received");
					i.putExtra("com.fgundlac.ft_hangouts.sms.received.sms", sms);
					context.sendBroadcast(i);

					ContactsDataSource contactsDataSource = new ContactsDataSource(context);
					if (!contactsDataSource.checkIfNumberExits(sms.getNumber()))
					{
						contactsDataSource.open();
						Contact c = contactsDataSource.insert(new Contact(sms.getNumber(), sms.getNumber()));
						contactsDataSource.close();
						i = new Intent("com.fgundlac.ft_hangouts.contact.added");
						i.putExtra("com.fgundlac.ft_hangouts.contact.added.contact", c);
						context.sendBroadcast(i);
					}
				}
			}
		}
	}
}
