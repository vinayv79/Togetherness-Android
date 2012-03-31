package com.togetherness.activity;


import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import com.togetherness.R;
import com.togetherness.entity.FacebookProfile;
import com.togetherness.util.FacebookUtilities;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private static final int SUCCESS_AUTH = 321;
	private static final int CALL_TO_LANDING = 657;
	private Facebook facebook;
	private ImageButton mFacebookLoginBtn;
	private OnClickListener mFbLoginClicklistener;
	private SharedPreferences mSettings;
	private Handler mhandler = new Handler();
	//private DialogListener mFbListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.login);

		mSettings = getSharedPreferences("TogethernessSetting", 0);
		facebook = new Facebook("246838415412064");

		mFacebookLoginBtn = (ImageButton) findViewById(R.id.main_login_button);

		initializeListener(); 
		
		mFacebookLoginBtn.setOnClickListener(mFbLoginClicklistener);
		
		if(isFacebookSessionValid())
		{
			Intent toLandingActivity = new Intent(LoginActivity.this, FriendsListActivity.class);
			startActivityForResult(toLandingActivity, CALL_TO_LANDING);
		}
	}

	private void initializeListener() {
		

		mFbLoginClicklistener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				DialogListener mFbListener = new DialogListener() {

					@Override
					public void onFacebookError(FacebookError e) {
						Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG);

					}

					@Override
					public void onError(DialogError e) {
						Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG);

					}

					@Override
					public void onComplete(Bundle values) {

						SharedPreferences.Editor editor = mSettings.edit();
						editor.putString("fbAccessToken", facebook.getAccessToken());
						editor.putLong("fbAccessExpires", facebook.getAccessExpires());
						editor.commit();
						
						FacebookUtilities.attemptGetFacebookProfile( mhandler, LoginActivity.this);
						
						

					}

					@Override
					public void onCancel() {
						Toast.makeText(LoginActivity.this, "Cancelled", Toast.LENGTH_SHORT);

					}
				};

				if (!isFacebookSessionValid()) {
					String[] fbPermission = new String[] { "publish_stream", "offline_access", "email" };

					facebook.authorize(LoginActivity.this, fbPermission, mFbListener);
				}
			}
		};

	}
	
	
	private boolean isFacebookSessionValid()
	{
		String accessToken = mSettings.getString("fbAccessToken", null);
		long expires = mSettings.getLong("fbAccessExpires", 0);

		if (accessToken != null) {
			facebook.setAccessToken(accessToken);
		}

		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}
		
		return facebook.isSessionValid();
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//Log.d(TAG, "done authorize");

		if (requestCode == 32665) {
			facebook.authorizeCallback(requestCode, resultCode, data);
			
			//get facebook profile, and upload to Milotin
//			showDialog(REGISTERING_ID);
//			AuthenticateUtilities.attemptRegisterFacebookToMilotin(AuthenticateActivity.this, mHandler);
			
		}
		else if(requestCode == CALL_TO_LANDING){
			finish();
		}

	}

	public void finishGetFacebookProfile(FacebookProfile profile) {
		
		if(profile == null)
		{
			return;
		}
		
		//save into settings
		SharedPreferences.Editor editor = mSettings.edit();
		editor.putString("facebook_id", profile.getId());
		editor.putString("facebook_name", profile.getName());
		editor.putBoolean("facebook_gender", profile.isMale());
		editor.commit();
		
		Intent toLandingActivity = new Intent(LoginActivity.this, FriendsListActivity.class);
		startActivity(toLandingActivity);
	}

}
