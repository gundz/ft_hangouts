package com.fgundlac.ft_hangouts.Contacts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by flogu on 10/02/2016.
 */
public class SMSBroadcast extends BroadcastReceiver
{
	public static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

	@Override
	public void onReceive(Context context, Intent intent)
	{
		// TODO Auto-generated method stub
		if (intent.getAction().equals(ACTION))
		{
			Bundle bundle = intent.getExtras();
			if (bundle != null)
			{
				Object[] pdus = (Object[]) bundle.get("pdus");
				SmsMessage[] messages = new SmsMessage[pdus.length];
				for (int i = 0; i < pdus.length; i++)
				{
					if (Build.VERSION.SDK_INT >= 23)
						messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], "3gpp");
					else
						messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				}
				for (SmsMessage message : messages)
				{

					String strMessageFrom = message.getDisplayOriginatingAddress();
					String strMessageBody = message.getDisplayMessageBody();

					SMSDataSource smsDatabase = new SMSDataSource(context);
					smsDatabase.open();
					SMS sms = smsDatabase.insert(new SMS(strMessageFrom, strMessageBody, SMS.Type.RECEIVED, Calendar.getInstance()));
					smsDatabase.close();

					Toast.makeText(context, "SMS Message received from: " + strMessageFrom, Toast.LENGTH_LONG).show();
					Toast.makeText(context, "SMS Message content: " +strMessageBody, Toast.LENGTH_LONG).show();

				}
			}
		}
	}
}
