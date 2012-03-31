package com.togetherness.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.togetherness.R;
import com.togetherness.entity.Friends;
import com.togetherness.util.TogethernessConstants;

import java.util.ArrayList;


/**
 * Created by IntelliJ IDEA.
 * User: Prashanth
 * Date: 3/31/12
 * Time: 4:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class StatusUpdateActivity extends Activity implements ViewSwitcher.ViewFactory {

    private ImageSwitcher mSwitcher;

    private ArrayList<Friends> selectedFriends;
    
    private Integer[] mThumbIds = {
            R.drawable.sample_thumb_0, R.drawable.sample_thumb_1};

     public void onCreate(Bundle savedInstanceState){
         super.onCreate(savedInstanceState);

         setContentView(R.layout.status_update);

         Parcelable[] selectedFriends =
                 getIntent().getExtras().getParcelableArray(TogethernessConstants.SELECTED_FRIEND_LIST);
         
         Gallery g = (Gallery) findViewById(R.id.gallery);
         g.setAdapter(new ImageAdapter(this));

     }

    public View makeView() {
        ImageView i = new ImageView(this);
        i.setBackgroundColor(0xFF000000);
        i.setScaleType(ImageView.ScaleType.FIT_CENTER);
        i.setLayoutParams(new ImageSwitcher.LayoutParams(Gallery.LayoutParams.MATCH_PARENT,
                Gallery.LayoutParams.MATCH_PARENT));
        return i;
    }

    public class ImageAdapter extends BaseAdapter {
        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mThumbIds.length;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView i = new ImageView(mContext);

          //  i.setImageBitmap();
            i.setImageResource(mThumbIds[position]);
            i.setAdjustViewBounds(true);
            i.setLayoutParams(new Gallery.LayoutParams(
                    Gallery.LayoutParams.WRAP_CONTENT, Gallery.LayoutParams.WRAP_CONTENT));
           // i.setBackgroundResource(R.drawable.picture_frame);
            return i;
        }

        private Context mContext;

    }
}
