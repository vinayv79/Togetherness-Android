package com.togetherness.lazylist;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.widget.ImageView;

public class FavoriteImageLoader extends ImageLoader {

	public FavoriteImageLoader(Context context, int size, boolean enforceSize) {
		super(context, size, enforceSize);

	}

	public void DisplayImage(String url, ImageView imageView, String itemName, String locationName, int count) {

		imageViews.put(imageView, url);
		Bitmap bitmap = memCache.get(url);
		if (bitmap != null) {
			setLayer(bitmap, itemName, locationName, count);
			imageView.setImageBitmap(bitmap);
		} else {
			queuePhoto(url, imageView, itemName, locationName, count);
			imageView.setImageResource(stub_id);
		}
	}

	private void queuePhoto(String url, ImageView imageView, String itemName, String locationName, int count) {
		if (url != null) {
			FavoritePhotoToLoad p = new FavoritePhotoToLoad(url, imageView, itemName, locationName, count);
			executorService.submit(new FavoritePhotoLoader(p));
		}
	}

	private void setLayer(Bitmap bitmap, String itemName, String locationName, int count) {
		float height = bitmap.getHeight();

		Canvas c = new Canvas(bitmap);
		Paint p = new Paint();
		p.setAntiAlias(true);

		// count
		int countSize = 25;
		p.setTextSize(countSize);
		p.setColor(0xFFFF7722);
		float countWidth = p.measureText(Integer.toString(count) + " enjoys");		
		c.drawRoundRect(new RectF(10, height - 9 - countSize, countWidth + 18, height - 6), 5, 5, p);
		p.setColor(Color.WHITE);
		c.drawText(Integer.toString(count) + " enjoys", 13, height - 13, p);
		height -= countSize + 3;

		// location
		int locationSize = 30;
		p.setTextSize(locationSize);
		p.setColor(Color.WHITE);
		c.drawText("@ " + locationName, 10, height - 13, p);
		height -= locationSize + 13;

		// item name
		int itemNameSize = 40;
		p.setTextSize(itemNameSize);
		p.setColor(Color.WHITE);
		p.setTypeface(Typeface.DEFAULT_BOLD);
		c.drawText(itemName, 10, height - 5, p);
	}

	class FavoritePhotoLoader implements Runnable {
		protected FavoritePhotoToLoad photoToLoad;

		public FavoritePhotoLoader(FavoritePhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			if (imageViewResued(photoToLoad)) {
				return;
			}

			Bitmap bmp = getBitmap(photoToLoad.getUrl());
			setLayer(bmp, photoToLoad.mItemName, photoToLoad.mLocationName, photoToLoad.getCount());

			memCache.put(photoToLoad.getUrl(), bmp);

			if (imageViewResued(photoToLoad)) {
				return;
			}

			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			Activity a = (Activity) photoToLoad.getImageView().getContext();
			a.runOnUiThread(bd);
			
			//bmp.recycle();
		}

	}

	class FavoritePhotoToLoad extends PhotoToLoad {
		private String mItemName;
		private String mLocationName;
		private int mCount;

		public String getItemName() {
			return mItemName;
		}

		public String getLocationName() {
			return mLocationName;
		}

		public int getCount() {
			return mCount;
		}

		public FavoritePhotoToLoad(String url, ImageView imageView, String itemName, String locationName, int count) {
			super(url, imageView);

			this.mItemName = itemName;
			this.mLocationName = locationName;
			this.mCount = count;
		}

	}

}
