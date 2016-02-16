package com.fgundlac.ft_hangouts;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class BaseClass extends AppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setActionBarColor();
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
