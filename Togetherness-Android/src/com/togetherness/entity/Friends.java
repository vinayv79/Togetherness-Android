package com.togetherness.entity;

import android.graphics.Bitmap;
import android.os.Parcelable;
import android.os.Parcel;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.types.ByteArrayType;
import com.togetherness.util.TogethernessConstants;

/**
 * Created by IntelliJ IDEA.
 * User: Prashanth
 * Date: 3/31/12
 * Time: 4:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class Friends implements Parcelable{

    @DatabaseField(id = true)
    private String _id;

    public String loggedInUserId;

    @DatabaseField
    public String friendsFBID;
    public String checkedInStatus;

    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    public byte[] friendPhoto;

    public String currentLocation;
    
    public Friends(){
        
    }
    
    public Friends(Parcel parcel){
       readFromParcel(parcel);
    }

    public String getLoggedInUserId() {
        return loggedInUserId;
    }

    public void setLoggedInUserId(String loggedInUserId) {
        this.loggedInUserId = loggedInUserId;
    }

    public String getFriendsFBID() {
        return friendsFBID;
    }

    public void setFriendsFBID(String friendsFBID) {
        this.friendsFBID = friendsFBID;
        this._id = ""+friendsFBID;
    }

    public String getCheckedInStatus() {
        return checkedInStatus;
    }

    public void setCheckedInStatus(String checkedInStatus) {
        this.checkedInStatus = checkedInStatus;
    }

    public byte[] getFriendPhoto() {
        return friendPhoto;
    }

    public void setFriendPhoto(byte[] friendPhoto) {
        this.friendPhoto = friendPhoto;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public static final Parcelable.Creator<Friends> CREATOR = new Parcelable.Creator<Friends>() {
        public Friends createFromParcel(Parcel source) {
            return new Friends(source);
        }

        public Friends[] newArray(int size) {
            throw new UnsupportedOperationException();
        }

    };

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int ignored) {
        dest.writeString(getFriendsFBID());
        dest.writeString(getLoggedInUserId());
        dest.writeString(getCheckedInStatus());
        dest.writeString(getCurrentLocation());
        dest.writeByteArray(getFriendPhoto());
    }


    public void readFromParcel(Parcel parcel){
        setFriendsFBID(parcel.readString());
        setLoggedInUserId(parcel.readString());
        setCheckedInStatus(parcel.readString());
        setCurrentLocation(parcel.readString());
        int size = parcel.readInt();
        this.friendPhoto = new byte[size];
        parcel.readByteArray(friendPhoto);
    }

}
