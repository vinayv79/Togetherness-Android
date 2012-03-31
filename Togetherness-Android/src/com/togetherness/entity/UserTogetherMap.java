package com.togetherness.entity;

import java.util.Date;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by IntelliJ IDEA.
 * User: mtpdevteam
 * Date: 3/31/12
 * Time: 4:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserTogetherMap {

    @DatabaseField(id = true)
    private String _id;

    @DatabaseField
    private String fbUserId;

    @DatabaseField
    private String togetherUserId;

    @DatabaseField
    private String togetherStatus;

    @DatabaseField
    private Date togetherTimestamp;

    @DatabaseField
    private String togetherMessage;

    @DatabaseField
    private Double togetherness;

    @DatabaseField
    private Double togetherDuration;

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getFbUserId() {
        return fbUserId;
    }

    public void setFbUserId(String fbUserId) {
        this.fbUserId = fbUserId;
    }

    public String getTogetherUserId() {
        return togetherUserId;
    }

    public void setTogetherUserId(String togetherUserId) {
        this.togetherUserId = togetherUserId;
        this._id = ""+togetherUserId;
    }

    public String getTogetherStatus() {
        return togetherStatus;
    }

    public void setTogetherStatus(String togetherStatus) {
        this.togetherStatus = togetherStatus;
    }

    public Date getTogetherTimestamp() {
        return togetherTimestamp;
    }

    public void setTogetherTimestamp(Date togetherTimestamp) {
        this.togetherTimestamp = togetherTimestamp;
    }

    public String getTogetherMessage() {
        return togetherMessage;
    }

    public void setTogetherMessage(String togetherMessage) {
        this.togetherMessage = togetherMessage;
    }

    public Double getTogetherness() {
        return togetherness;
    }

    public void setTogetherness(Double togetherness) {
        this.togetherness = togetherness;
    }

    public Double getTogetherDuration() {
        return togetherDuration;
    }

    public void setTogetherDuration(Double togetherDuration) {
        this.togetherDuration = togetherDuration;
    }
}
