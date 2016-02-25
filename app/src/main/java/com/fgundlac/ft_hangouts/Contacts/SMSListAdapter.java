package com.fgundlac.ft_hangouts.Contacts;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fgundlac.ft_hangouts.R;

import java.util.ArrayList;
import java.util.List;

public class SMSListAdapter extends BaseAdapter
{
	private Activity context;
	private List<SMS> smsList;
	private LayoutInflater layoutInflater = null;
	private Contact contact;
	private Bitmap contactPhoto;

	public SMSListAdapter(Activity context, ArrayList<SMS> smsList, Contact contact)
	{
		this.context = context;
		this.smsList = smsList;
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.contact = contact;
		contactPhoto = ContactPhoto.loadImageFromStorage(context, contact);
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
		View v = convertView;
		SMSListViewHolder viewHolder;
		SMS sms;

		if (convertView == null)
		{
			v = layoutInflater.inflate(R.layout.sms_list_item, null);
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
			viewHolder.SMSRelativeLayout.setGravity(Gravity.END);
			viewHolder.SMSRelativeLayout.setPadding(context.getResources().getDimensionPixelSize(R.dimen.sms_padding), 5, 5, 5);
			viewHolder.imageViewContactPhoto1.setVisibility(ImageView.GONE);
			viewHolder.imageViewContactPhoto2.setVisibility(ImageView.VISIBLE);
		}
		else if (sms.getType() == SMS.Type.SENDED)
		{
			viewHolder.SMSRelativeLayout.setGravity(Gravity.START);
			viewHolder.SMSRelativeLayout.setPadding(5, 5, context.getResources().getDimensionPixelSize(R.dimen.sms_padding), 5);
			viewHolder.imageViewContactPhoto2.setVisibility(ImageView.GONE);
			viewHolder.imageViewContactPhoto1.setVisibility(ImageView.VISIBLE);
		}
		viewHolder.imageViewContactPhoto2.setImageBitmap(contactPhoto);

		viewHolder.textViewDate.setText(sms.getDateFormated());
		viewHolder.textViewContent.setText(sms.getContent());

		return v;
	}

	class SMSListViewHolder
	{
		public TextView textViewDate;
		public TextView textViewContent;
		public RelativeLayout SMSRelativeLayout;
		public ImageView imageViewContactPhoto1;
		public ImageView imageViewContactPhoto2;

		public SMSListViewHolder(View base)
		{
			textViewDate = (TextView) base.findViewById(R.id.dateTextView);
			textViewContent = (TextView) base.findViewById(R.id.contentTextView);
			SMSRelativeLayout = (RelativeLayout) base.findViewById(R.id.SMSLayout);
			imageViewContactPhoto1 = (ImageView) base.findViewById(R.id.contactPhoto1);
			imageViewContactPhoto2 = (ImageView) base.findViewById(R.id.contactPhoto2);
		}
	}
}
