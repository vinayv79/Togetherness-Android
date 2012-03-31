package com.togetherness.activity;

import java.util.ArrayList;

import com.togetherness.R;
import com.togetherness.adapter.FriendsListAdapter;
import com.togetherness.entity.FacebookProfile;
import com.togetherness.util.FacebookUtilities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class FriendsListActivity extends Activity {

	private Handler handler = new Handler();
	private static final int LOADING_FRIENDS_ID = 12;
	
	//layout object
	private ListView mFriendsListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.friendslist);
		
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
	
	
	
	
}
