package com.fgundlac.ft_hangouts.Contacts;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fgundlac.ft_hangouts.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by flogu on 11/02/2016.
 */
public class SMSListAdapter extends BaseAdapter
{
	private Activity  context;
	private List<SMS> smsList;
	private LayoutInflater layoutInflater = null;

	class SMSListViewHolder
	{
		public TextView textViewDate;
		public TextView textViewContent;
		public RelativeLayout SMSRelativeLayout;

		public SMSListViewHolder(View base)
		{
			textViewDate = (TextView) base.findViewById(R.id.dateTextView);
			textViewContent = (TextView) base.findViewById(R.id.contentTextView);
			SMSRelativeLayout = (RelativeLayout) base.findViewById(R.id.SMSLayout);
		}
	}

	public SMSListAdapter(Activity context, ArrayList<SMS> smsList)
	{
		this.context = context;
		this.smsList = smsList;
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount()
	{
		return smsList.size();
	}

	@Override
	public Object getItem(int position)
	{
		return smsList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View                v = convertView;
		SMSListViewHolder   viewHolder;
		SMS                 sms;

		if (convertView == null)
		{
			LayoutInflater      li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			v = li.inflate(R.layout.sms_list_item, null);
			viewHolder = new SMSListViewHolder(v);
			v.setTag(viewHolder);
		}
		else
		{
			viewHolder = (SMSListViewHolder) v.getTag();
		}

		sms = smsList.get(position);

		if (sms.getType() == SMS.Type.RECEIVED)
		{
			viewHolder.SMSRelativeLayout.setGravity(Gravity.RIGHT);
		}
		else
		{
			viewHolder.SMSRelativeLayout.setGravity(Gravity.LEFT);
		}

		viewHolder.textViewDate.setText(sms.getDate().get(Calendar.HOUR) + ":" + sms.getDate().get(Calendar.MINUTE));
		viewHolder.textViewContent.setText(sms.getContent());

		return v;
	}
}
