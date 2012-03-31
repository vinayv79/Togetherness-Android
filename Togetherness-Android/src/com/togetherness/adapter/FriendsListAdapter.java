package com.togetherness.adapter;

import java.util.ArrayList;
import java.util.zip.Inflater;


import com.togetherness.R;
import com.togetherness.entity.FacebookProfile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FriendsListAdapter extends ArrayAdapter<FacebookProfile> {

	LayoutInflater mInflater;
	
	public FriendsListAdapter(Context context, int resourceId, ArrayList<FacebookProfile> objects) {
		super(context, resourceId, objects);
		
		mInflater = LayoutInflater.from(context);
		
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		
		FacebookProfile friend = getItem(position);
		
		if (view == null) {
			view = mInflater.inflate(R.layout.friendslist_row, null);
		}
		
		TextView nameTv =(TextView) view.findViewById(R.id.friendslist_row_name_textview);
		nameTv.setText(friend.getName());
		
		return view;
	}

	
	
}
