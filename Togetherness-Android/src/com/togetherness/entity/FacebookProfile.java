package com.togetherness.entity;

public class FacebookProfile {
	
	String mId;
	String mName;
	Boolean mIsMale = null;
	
	public String getId()
	{
		return mId;
	}
	
	public String getName()
	{
		return mName;
	}
	
	public Boolean isMale()
	{
		return mIsMale;
	}
	
	public FacebookProfile(String id, String name, Boolean isMale)
	{
		this.mId = id;
		this.mName = name;
		this.mIsMale = isMale;
	}
	
}
