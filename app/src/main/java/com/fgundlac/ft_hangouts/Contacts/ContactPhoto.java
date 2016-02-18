package com.fgundlac.ft_hangouts.Contacts;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.fgundlac.ft_hangouts.BaseClass;
import com.fgundlac.ft_hangouts.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ContactPhoto extends BaseClass
{
	public static final int RESULT = 1;
	public static final int CAMERA_REQUEST = 1888;
	protected Contact contact;
	protected File file;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		contact = getIntent().getParcelableExtra("com.fgundlac.ft_hangouts.contact.photo.contact");
		takeImageFromCamera();
	}

	public void takeImageFromCamera()
	{
		Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(cameraIntent, CAMERA_REQUEST);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK)
		{
			Bitmap photo = (Bitmap) data.getExtras().get("data");
			saveToInternalStorage(photo, contact);
			setResult(RESULT);
			finish();
		}
	}

	private void saveToInternalStorage(Bitmap bitmapImage, Contact contact)
	{
		File mypath = new File(getPath(getApplicationContext()), getFileName(String.valueOf(contact.getId())));
		Log.d("PATH", mypath.getAbsolutePath());

		try (FileOutputStream fos = new FileOutputStream(mypath))
		{
			bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	static public File getFile(Context context)
	{
		ContextWrapper cw = new ContextWrapper(context);
		return cw.getDir("imageDir", Context.MODE_PRIVATE);
	}

	static public String getPath(Context context)
	{
		return getFile(context).getAbsolutePath();
	}

	static public String getFileName(String name)
	{
		return name + ".jpg";
	}

	static public Bitmap loadImageFromStorage(Context context, Contact contact)
	{
		Bitmap bitmap = null;
		try
		{
			File f = new File(getPath(context), getFileName(String.valueOf(contact.getId())));
			bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			//getDefaultimage
		}
		return bitmap;
	}
}
