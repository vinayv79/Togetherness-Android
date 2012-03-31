package com.togetherness.activity;

import java.util.ArrayList;

import com.togetherness.R;
import com.togetherness.adapter.FriendsListAdapter;
import com.togetherness.entity.FacebookProfile;
import com.togetherness.util.FacebookUtilities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class FriendsListActivity extends Activity {

	//private ImageView testImage;
	
	private Handler handler = new Handler();
	private static final int LOADING_FRIENDS_ID = 12;
	
	//layout object
	private ListView mFriendsListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.friendslist);
		
		//testImage = (ImageView)findViewById(R.id.friendslist_test_imageview);
		
		initializeLayoutObject();
		
		reloadFacebookFriends();
		
	}
	
	
	private void initializeLayoutObject() {
		
		mFriendsListView = (ListView)findViewById(R.id.friendslist_listview);
		
	}


	private void reloadFacebookFriends()
	{
		showDialog(LOADING_FRIENDS_ID);
		FacebookUtilities.attemptGetFacebookFriends(handler, this);
	}
	
	
	

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		final ProgressDialog dialog = new ProgressDialog(this);
		
		if(id == LOADING_FRIENDS_ID)
		{
			dialog.setMessage("Loading Friends..");
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			
		}
		return dialog;
	}
	
	
	private void populateFriendsListView(ArrayList<FacebookProfile> friendsList)
	{
		FriendsListAdapter adapter = new FriendsListAdapter(this, 0, friendsList);
		mFriendsListView.setAdapter(adapter);
		
		String id = friendsList.get(0).getId();
		FacebookUtilities.attemptGetFriendPicture(id, handler, this);
	}




	public void finishGetFacebookFriends(ArrayList<FacebookProfile> friendsList) {
		
		removeDialog(LOADING_FRIENDS_ID);
		//dismissDialog(LOADING_FRIENDS_ID);
		
		Log.i(FriendsListActivity.class.getName(), "dismissDialog");
		
		if(friendsList == null)
		{
			Toast.makeText(this, "Failed to load friends, try again", Toast.LENGTH_LONG).show();
		}
		else {
			populateFriendsListView(friendsList);
		}
		
		
	}


	@Override
	protected void onPause() {
		
		super.onPause();
	}


	public void finishGetFacebookFriendProfile(FacebookProfile friendProfile) {
		
		if(friendProfile == null)
		{
			Toast.makeText(this, "Unable to retrieve, try later", Toast.LENGTH_LONG).show();
		}
		
		
		
	}


	public void finishGetFacebookFriendPicture(BitmapDrawable fbPicture) {
		
		if(fbPicture != null)
		{
			//testImage.setImageDrawable(fbPicture);
		}
		
		
	}
	
	
	
	
}
