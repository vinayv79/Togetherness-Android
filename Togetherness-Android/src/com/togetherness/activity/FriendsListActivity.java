package com.togetherness.activity;

import java.util.ArrayList;

import com.bump.api.BumpAPIIntents;
import com.bump.api.IBumpAPI;
import com.togetherness.R;
import com.togetherness.adapter.FriendsListAdapter;
import com.togetherness.entity.FacebookProfile;
import com.togetherness.util.FacebookUtilities;
import com.togetherness.util.TogethernessUtilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class FriendsListActivity extends Activity {

	// private ImageView testImage;

	private Handler handler = new Handler();
	private static final int LOADING_FRIENDS_ID = 12;
	private static final int NOTICE_ID = 432;
	private IBumpAPI api;

	// layout object
	private ListView mFriendsListView;

	private final ServiceConnection connection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className, IBinder binder) {
			Log.i("BumpTest", "onServiceConnected");
			api = IBumpAPI.Stub.asInterface(binder);
			try {
				SharedPreferences settings = getSharedPreferences("TogethernessSetting", 0);
				String user_name = settings.getString("facebook_name", "0");
				api.configure("f2afcb53533d4609a215994a3db23936", user_name);
			} catch (RemoteException e) {
				Log.w("BumpTest", e);
			}
			Log.d("Bump Test", "Service connected");
		}

		@Override
		public void onServiceDisconnected(ComponentName className) {
			Log.d("Bump Test", "Service disconnected");
		}
	};

	private final BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			try {
				if (action.equals(BumpAPIIntents.DATA_RECEIVED)) {
					Log.i("Bump Test", "Received data from: " + api.userIDForChannelID(intent.getLongExtra("channelID", 0)));
					Log.i("Bump Test", "Data: " + new String(intent.getByteArrayExtra("data")));
				} else if (action.equals(BumpAPIIntents.MATCHED)) {
					long channelID = intent.getLongExtra("proposedChannelID", 0);
					Log.i("Bump Test", "Matched with: " + api.userIDForChannelID(channelID));
					Toast.makeText(FriendsListActivity.this, "Matched with: " + api.userIDForChannelID(channelID), Toast.LENGTH_LONG).show();
					api.confirm(channelID, true);
					Log.i("Bump Test", "Confirm sent");
				} else if (action.equals(BumpAPIIntents.CHANNEL_CONFIRMED)) {
					long channelID = intent.getLongExtra("channelID", 0);
					Log.i("Bump Test", "Channel confirmed with " + api.userIDForChannelID(channelID));
					api.send(channelID, TogethernessUtilities.getFacebookId(FriendsListActivity.this).getBytes());
				} else if (action.equals(BumpAPIIntents.NOT_MATCHED)) {
					Log.i("Bump Test", "Not matched.");
				} else if (action.equals(BumpAPIIntents.CONNECTED)) {
					Log.i("Bump Test", "Connected to Bump...");
					api.enableBumping();
				}
			} catch (RemoteException e) {
			}
		}
	};

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.friendslist);

		bindService(new Intent(IBumpAPI.class.getName()), connection, Context.BIND_AUTO_CREATE);
		Log.i("BumpTest", "boot");

		IntentFilter filter = new IntentFilter();
		filter.addAction(BumpAPIIntents.CHANNEL_CONFIRMED);
		filter.addAction(BumpAPIIntents.DATA_RECEIVED);
		filter.addAction(BumpAPIIntents.NOT_MATCHED);
		filter.addAction(BumpAPIIntents.MATCHED);
		filter.addAction(BumpAPIIntents.CONNECTED);
		registerReceiver(receiver, filter);

		// testImage = (ImageView)findViewById(R.id.friendslist_test_imageview);

		initializeLayoutObject();

		reloadFacebookFriends();

	}

	private void initializeLayoutObject() {

		mFriendsListView = (ListView) findViewById(R.id.friendslist_listview);

	}

	private void reloadFacebookFriends() {
		showDialog(LOADING_FRIENDS_ID);
		FacebookUtilities.attemptGetFacebookFriends(handler, this);
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		

		if (id == LOADING_FRIENDS_ID) {
			final ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage("Loading Friends..");
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			return dialog;
		}
		else if(id == NOTICE_ID)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.bump, null);
			
			
		}
		
		
		
		return null;
		
	}

	private void populateFriendsListView(ArrayList<FacebookProfile> friendsList) {
		FriendsListAdapter adapter = new FriendsListAdapter(this, 0, friendsList);
		mFriendsListView.setAdapter(adapter);

		String id = friendsList.get(0).getId();
		FacebookUtilities.attemptGetFriendPicture(id, handler, this);
	}

	public void finishGetFacebookFriends(ArrayList<FacebookProfile> friendsList) {

		removeDialog(LOADING_FRIENDS_ID);
		// dismissDialog(LOADING_FRIENDS_ID);

		Log.i(FriendsListActivity.class.getName(), "dismissDialog");

		if (friendsList == null) {
			Toast.makeText(this, "Failed to load friends, try again", Toast.LENGTH_LONG).show();
		} else {
			populateFriendsListView(friendsList);
		}

	}

	public void onDestroy() {
        Log.i("BumpTest", "onDestroy");
        unbindService(connection);
        unregisterReceiver(receiver);
        
        
        
        super.onDestroy();
     }

	public void finishGetFacebookFriendProfile(FacebookProfile friendProfile) {

		if (friendProfile == null) {
			Toast.makeText(this, "Unable to retrieve, try later", Toast.LENGTH_LONG).show();
		}

	}

	public void finishGetFacebookFriendPicture(BitmapDrawable fbPicture) {

		if (fbPicture != null) {
			// testImage.setImageDrawable(fbPicture);
		}

	}

}
