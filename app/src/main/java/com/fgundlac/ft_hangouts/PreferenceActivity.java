package com.fgundlac.ft_hangouts;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by flogu on 04/02/2016.
 */
public class PreferenceActivity extends BaseClass
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
	}

	public static class MyPreferenceFragment extends PreferenceFragment
	{
		@Override
		public void onCreate(final Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preference);
		}
	}
}
