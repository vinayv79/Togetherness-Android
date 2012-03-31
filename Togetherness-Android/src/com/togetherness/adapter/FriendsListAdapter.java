package com.togetherness.adapter;

import java.util.ArrayList;
import java.util.zip.Inflater;


import com.togetherness.R;
import com.togetherness.activity.StatusUpdateActivity;
import com.togetherness.entity.FacebookProfile;
import com.togetherness.lazylist.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendsListAdapter extends ArrayAdapter<FacebookProfile> {

	LayoutInflater mInflater;
	ImageLoader mLoader;
	
	public FriendsListAdapter(Context context, int resourceId, ArrayList<FacebookProfile> objects) {
		super(context, resourceId, objects);
		
		mInflater = LayoutInflater.from(context);
		mLoader = new ImageLoader(context, 50, true);
		
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		
		FacebookProfile friend = getItem(position);
		
		if (view == null) {
			view = mInflater.inflate(R.layout.friendslist_row, null);
		}
		
		view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getContext(), StatusUpdateActivity.class);
				
			}
		});
		
		TextView nameTv =(TextView) view.findViewById(R.id.friendslist_row_name_textview);
		nameTv.setText(friend.getName());
		
		ImageView img = (ImageView)view.findViewById(R.id.friendslist_row_pic_imageview);
		
		mLoader.DisplayImage("https://graph.facebook.com/" + friend.getId() + "/picture?type=square", img);
		
		return view;
	}

	
	
}
