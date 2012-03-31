package com.togetherness.lazylist;

import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

public class Utils {

	public static void CopyStream(InputStream is, OutputStream os) {
		final int bufferSize = 1024;

		try {
			byte[] bytes = new byte[bufferSize];

			while (true) {
				int count = is.read(bytes, 0, bufferSize);
				if (count == -1) {
					break;
				}

				os.write(bytes, 0, count);
			}

		} catch (Exception e) {
			Log.e("MLazyList", e.getMessage());
		}
	}

}
