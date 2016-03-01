package com.fgundlac.ft_hangouts.Contacts;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;

import com.fgundlac.ft_hangouts.BaseClass;
import com.fgundlac.ft_hangouts.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ContactPhoto extends BaseClass
{
	public static final int REQUEST_IMAGE_CAPTURE = 1;
	protected File file;

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
		return name + ".png";
	}

	static public Bitmap loadImageFromStorage(Context context, String photoName)
	{
		Bitmap bitmap = null;

		if (photoName == null)
		{
			return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_people);
		}

		try
		{
			File f = new File(getPath(context), photoName);
			bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
		} catch (FileNotFoundException e)
		{
			bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_people);
		}
		return bitmap;
	}

	private void saveToInternalStorage(Bitmap bitmapImage, String photoName)
	{
		File mypath = new File(getPath(getApplicationContext()), photoName);
		try (FileOutputStream fos = new FileOutputStream(mypath))
		{
			bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		takeImageFromCamera();
	}

	public void takeImageFromCamera()
	{
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getPackageManager()) != null)
		{
			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
		{
			Bitmap photo = (Bitmap) data.getExtras().get("data");
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", getResources().getConfiguration().locale).format(new Date());
			String photoName = getFileName(timeStamp);
			saveToInternalStorage(photo, getFileName(timeStamp));

			Intent databackIntent = new Intent();
			databackIntent.putExtra("com.fgundlac.ft_hangouts.camera.photoName", photoName);
			setResult(RESULT_OK, databackIntent);
		}
		else
		{
			setResult(RESULT_CANCELED);
		}
		finish();
	}
}
