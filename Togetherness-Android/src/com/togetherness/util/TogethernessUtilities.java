package com.togetherness.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class TogethernessUtilities {

	public static String StreamToString(final InputStream stream) {
		String output = new String();
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(stream));
			StringBuilder sb = new StringBuilder();
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			output = sb.toString();
			stream.close();

		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		return output;
	}
	
	
	public static String getFacebookId(Context context)
	{
		SharedPreferences settings = context.getSharedPreferences("TogethernessSetting", 0);
		String user_id = settings.getString("facebook_id", "0");
		
		return user_id;
	}
	
}
