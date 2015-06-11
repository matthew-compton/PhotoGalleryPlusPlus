package com.ambergleam.android.photogallery.model;

import java.io.Serializable;

public class Photo implements Serializable {

    private String mCaption;
    private String mId;
    private String mSmallUrl;
    private String mLargeUrl;
    private int mLargeUrlWidth;
    private int mLargeUrlHeight;
    private String mOwner;

    public String getCaption() {
        return mCaption;
    }

    public void setCaption(String caption) {
        mCaption = caption;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getSmallUrl() {
        return mSmallUrl;
    }

    public void setSmallUrl(String smallUrl) {
        mSmallUrl = smallUrl;
    }

    public String getLargeUrl() {
        return mLargeUrl;
    }

    public void setLargeUrl(String largeUrl) {
        mLargeUrl = largeUrl;
    }

    public int getLargeUrlWidth() {
        return mLargeUrlWidth;
    }

    public void setLargeUrlWidth(int largeUrlWidth) {
        mLargeUrlWidth = largeUrlWidth;
    }

    public int getLargeUrlHeight() {
        return mLargeUrlHeight;
    }

    public void setLargeUrlHeight(int largeUrlHeight) {
        mLargeUrlHeight = largeUrlHeight;
    }

    public int getSmallestSide() {
        return mLargeUrlWidth > mLargeUrlHeight ? mLargeUrlHeight : mLargeUrlWidth;
    }

    public String getOwner() {
        return mOwner;
    }

    public void setOwner(String owner) {
        mOwner = owner;
    }

    public String getPhotoPageUrl() {
        return "http://www.flickr.com/photos/" + mOwner + "/" + mId;
    }

    public String toString() {
        return mCaption;
    }

}