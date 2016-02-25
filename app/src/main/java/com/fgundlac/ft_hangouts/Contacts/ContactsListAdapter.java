package com.fgundlac.ft_hangouts.Contacts;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fgundlac.ft_hangouts.R;

import java.util.ArrayList;
import java.util.List;

public class ContactsListAdapter extends BaseAdapter
{
	private Activity context;
	private List<Contact> contactList;
	private LayoutInflater layoutInflater = null;

	public ContactsListAdapter(Activity context, ArrayList<Contact> contactList)
	{
		this.context = context;
		this.contactList = contactList;
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount()
	{
		return contactList.size();
	}

	@Override
	public Object getItem(int position)
	{
		return contactList.get(position);
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
		ContactListViewHolder viewHolder;
		Contact contact;

		if (convertView == null)
		{
			v = layoutInflater.inflate(R.layout.contact_list_item, null);
			viewHolder = new ContactListViewHolder(v);
			v.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ContactListViewHolder) v.getTag();
		}
		contact = contactList.get(position);
		viewHolder.textViewName.setText(contact.getFullName());
		viewHolder.textViewNumber.setText(contact.getNumber());
		viewHolder.imageViewContactPhoto.setImageBitmap(ContactPhoto.loadImageFromStorage(context, contact));

		return v;
	}

	class ContactListViewHolder
	{
		public TextView textViewName;
		public TextView textViewNumber;
		public ImageView imageViewContactPhoto;

		public ContactListViewHolder(View base)
		{
			textViewName = (TextView) base.findViewById(R.id.nameTextView);
			textViewNumber = (TextView) base.findViewById(R.id.numberTextView);
			imageViewContactPhoto = (ImageView) base.findViewById(R.id.contactPhoto);
		}
	}
}
