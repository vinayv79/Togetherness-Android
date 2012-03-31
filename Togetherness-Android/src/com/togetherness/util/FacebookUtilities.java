package com.togetherness.util;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

import com.togetherness.activity.FriendsListActivity;
import com.togetherness.activity.LoginActivity;
import com.togetherness.entity.FacebookProfile;
import com.togetherness.parser.TogethernessParser;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

public class FacebookUtilities extends MTBaseNetworkUtilities {
	
	private static final String facebookBaseUrl = "https://graph.facebook.com/";
	
	public static Thread attemptGetFacebookProfile( final Handler handler, final Context context )
	{
		final Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				getFacebookProfile(handler, context);
				
			}
		};
		return performOnBackgroundThread(runnable);
	}

	private static void getFacebookProfile(Handler handler, Context context) {
		
		
		HttpGet getRequest = new HttpGet(facebookBaseUrl + "me?access_token=" + getFacebookAccessToken(context));
		
		maybeCreateHttpClient();
		
		try {
			HttpResponse response = mHttpClient.execute(getRequest);
			if(response.getStatusLine().getStatusCode() == 200)
			{
				String profileString = TogethernessUtilities.StreamToString(response.getEntity().getContent());
				FacebookProfile profile = TogethernessParser.parseFacebookProfile(profileString);
				
				sendGetFacebookProfile(profile, handler, context);
				return;
			}
			
		} catch (ClientProtocolException e) {			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		sendGetFacebookProfile(null, handler, context);
		
	}

	private static void sendGetFacebookProfile(final FacebookProfile profile, final Handler handler, final Context context) {
		
		if(handler == null || context == null)
		{
			return;
		}
		
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				((LoginActivity)context).finishGetFacebookProfile(profile);
				
			}
		}); 
		
	}

	
	public static Thread attemptGetFacebookFriends(final Handler handler, final Context context)
	{
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				getFacebookFriends(handler, context);
				
			}
		};
		
		return performOnBackgroundThread(runnable);
	}

	protected static void getFacebookFriends(Handler handler, Context context) {
		
		HttpGet getRequest = new HttpGet(facebookBaseUrl + "me/friends?access_token=" + getFacebookAccessToken(context));
		
		maybeCreateHttpClient();
		
		try {
			HttpResponse response = mHttpClient.execute(getRequest);
			
			if(response.getStatusLine().getStatusCode() == 200)
			{
				String friendsString = TogethernessUtilities.StreamToString(response.getEntity().getContent());
				
				ArrayList<FacebookProfile> friendsList = TogethernessParser.parseFacebookFriends(friendsString);
				
				sendGetFacebookFriends(friendsList, handler, context);
				
				return;
			}
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		sendGetFacebookFriends(null, handler, context);
	}

	private static void sendGetFacebookFriends(final ArrayList<FacebookProfile> friendsList, final Handler handler, final Context context) {
		
		if(handler == null || context == null)
		{
			return;
		}
		
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				((FriendsListActivity)context).finishGetFacebookFriends(friendsList);
				
			}
		});
		
	}
}
