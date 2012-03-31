package com.togetherness.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.togetherness.entity.FacebookProfile;


public class TogethernessParser {
	
	public static FacebookProfile parseFacebookProfile(String jsonString)
	{
		try {
			JSONObject profile = new JSONObject(jsonString);
			
			String id = profile.getString("id");
			String name = profile.getString("name");
			Boolean isMale =(profile.getString("gender").equals("male") ? true : false);
			
			return new FacebookProfile(id, name, isMale);
			
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
		
	}

	public static ArrayList<FacebookProfile> parseFacebookFriends(String friendsString) {
		
		final ArrayList<FacebookProfile> friendsList = new ArrayList<FacebookProfile>();
		
		try {
			JSONObject fbObj = new JSONObject(friendsString);
			JSONArray data = fbObj.getJSONArray("data");
			
			for(int i = 0; i< data.length(); i++)
			{
				JSONObject friend = data.getJSONObject(i);
				String name = friend.getString("name");
				String id = friend.getString("id");
				friendsList.add(new FacebookProfile(id, name, null));
			}
			
			return friendsList;
			
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		
		return null;
		
	}

	public static FacebookProfile parseFacebookFriendProfile(String friendJsonString) {
		
		try {
			JSONObject fbFriend = new JSONObject(friendJsonString);
			
			String id = fbFriend.getString("id");
			String name = fbFriend.getString("name");
			Boolean isMale = fbFriend.getString("gender").equals("male")?true:false;
			
			return new FacebookProfile(id, name, isMale);
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
}
