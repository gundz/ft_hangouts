package com.fgundlac.ft_hangouts;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BaseClass extends AppCompatActivity
{

	protected Calendar date = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setActionBarColor();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if (date != null)
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
			String formatedDate =  dateFormat.format(date.getTime());

			Toast.makeText(getApplicationContext(), formatedDate, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		date = Calendar.getInstance();
	}

	private void setActionBarColor()
	{
		SharedPreferences   SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		String colorPref = SP.getString("actionBarColorList", "colorPrimary");
		int colorID = getResources().getIdentifier(colorPref, "color", getPackageName());
		String color = getResources().getString(colorID);

		android.support.v7.app.ActionBar bar = getSupportActionBar();
		if (bar != null)
			bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
	}
}
