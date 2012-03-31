package com.togetherness.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.bump.api.IBumpAPI;
import com.bump.api.BumpAPIIntents;

public class KnuckleTouchAppActivity extends Activity {

	private IBumpAPI api;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.main);

		bindService(new Intent(IBumpAPI.class.getName()), connection, Context.BIND_AUTO_CREATE);

		IntentFilter filter = new IntentFilter();
		filter.addAction(BumpAPIIntents.CHANNEL_CONFIRMED);
		filter.addAction(BumpAPIIntents.DATA_RECEIVED);
		filter.addAction(BumpAPIIntents.NOT_MATCHED);
		filter.addAction(BumpAPIIntents.MATCHED);
		filter.addAction(BumpAPIIntents.CONNECTED);
		registerReceiver(receiver, filter);
	}

	private final ServiceConnection connection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className, IBinder binder) {
			api = IBumpAPI.Stub.asInterface(binder);
			try {
				api.configure("3d93055378b64b19b507bea593203a62", "Bump User");
			} catch (RemoteException e) {
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub

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
					api.confirm(intent.getLongExtra("proposedChannelID", 0), true);
				} else if (action.equals(BumpAPIIntents.CHANNEL_CONFIRMED)) {
					api.send(intent.getLongExtra("channelID", 0), "Hello, world!".getBytes());
				} else if (action.equals(BumpAPIIntents.CONNECTED)) {
					api.enableBumping();
				}
			} catch (RemoteException e) {
			}
		}
	};

}