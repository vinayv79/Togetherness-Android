package com.togetherness.lazylist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.togetherness.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoader {

	MemoryCache memCache = new MemoryCache();
	FileCache fileCache;
	protected Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
	ExecutorService executorService;
	private String TAG = "ImageLoader";
	private int mSize = 70;
	private boolean mEnforceSize;
	
	public ImageLoader(Context context, int size, boolean enforceSize)
	{
		mSize = size;
		mEnforceSize = enforceSize;
		
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
	}
	
	final int stub_id = R.drawable.facebook_default;
	public void DisplayImage(String url, ImageView imageView)
	{
		imageViews.put(imageView, url);
		Bitmap bitmap = memCache.get(url);
		if(bitmap != null)
		{
			imageView.setImageBitmap(bitmap);
		}
		else {
			queuePhoto(url, imageView);
			imageView.setImageResource(stub_id);
		}
	}
	
	
	
	protected void queuePhoto(String url, ImageView imageView)
	{
		if(url != null)
		{
			PhotoToLoad p = new PhotoToLoad(url, imageView);
			executorService.submit(new PhotoLoader(p));
		}
	}
	
	
	protected Bitmap getBitmap(String url)
	{
		//try get from file
		File f = fileCache.getFile(url);
		
		Bitmap b = decodeFile(f);
		if(b!= null)
		{
			return b;
		}
		
		//get from web
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection connection = (HttpURLConnection)imageUrl.openConnection();
			connection.setConnectTimeout(30000);
			connection.setReadTimeout(30000);
			connection.setInstanceFollowRedirects(true);
			InputStream is = connection.getInputStream();
			OutputStream os = new FileOutputStream(f);
			Utils.CopyStream(is, os);
			os.close();
			is.close();
			bitmap = decodeFile(f);
			
			return bitmap;
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	
	private Bitmap decodeFile(File f)
	{
		Bitmap bmp = null;
		
		try {
			//decode image size
//			BitmapFactory.Options o = new BitmapFactory.Options();
//			o.inJustDecodeBounds = true;
//			o.inScaled = false;
			
			//FileInputStream is = new FileInputStream(f);
			
//			Bitmap hold = BitmapFactory.decodeStream(is, null , o);
//			is.close();
			
			//find correct scale value, power of 2.
			int scale = 1;
			
			
			
//			if(o.outHeight > REQUIRED_SIZE || o.outWidth > REQUIRED_SIZE)
//			{
//				scale = (int)Math.pow(2, (int) Math.round(Math.log(REQUIRED_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
//			}
			
			//decode with in sample size
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			
			
			
			FileInputStream is = new FileInputStream(f);
			bmp = BitmapFactory.decodeStream(is, new Rect(10, 10, 10, 10), o2);
			is.close();
			
			if(mEnforceSize)
			{
				bmp = Bitmap.createScaledBitmap(bmp, mSize, mSize, true);
			}
			
			return bmp;
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bmp;
	}
	
	
	
	protected class PhotoToLoad
	{
		private String url;
		private ImageView imageView;
		
		public String getUrl()
		{
			return url;
		}
		
		public ImageView getImageView()
		{
			return imageView;
		}
		
		public PhotoToLoad(String u, ImageView i)
		{
			this.url = u;
			this.imageView = i;
		}
	}
	
	
	class PhotoLoader implements Runnable
	{
		protected PhotoToLoad photoToLoad;
		public PhotoLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}
		
		@Override
		public void run() {
			
			if(imageViewResued(photoToLoad))
			{
				return;
			}
			
			Bitmap bmp = getBitmap(photoToLoad.url);
			memCache.put(photoToLoad.url, bmp);
			
			if(imageViewResued(photoToLoad))
			{
				return;
			}
			
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			Activity a = (Activity)photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);
			
		}
		
		
	}
	
	
	boolean imageViewResued(PhotoToLoad photoToLoad)
	{
		String tag = imageViews.get(photoToLoad.imageView);
		
		if(tag == null || !tag.equals(photoToLoad.url))
		{
			return true;
		}
		else {
			return false;
		}
	}
	
	
	
	class BitmapDisplayer implements Runnable
	{
		Bitmap bitmap;
		PhotoToLoad photoToLoad;
		public BitmapDisplayer(Bitmap b, PhotoToLoad p)
		{
			bitmap = b;
			photoToLoad = p;
		}
		@Override
		public void run() {
			if(imageViewResued(photoToLoad))
			{
				return;
			}
			
			if(bitmap != null)
			{
				photoToLoad.imageView.setImageBitmap(bitmap);
			}
			else {
				photoToLoad.imageView.setImageResource(stub_id);
			}
			
		}
	}
	
	public void clearCache()
	{
		memCache.clear();
		fileCache.clear();
	}
}
