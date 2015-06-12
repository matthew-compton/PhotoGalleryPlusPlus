package com.ambergleam.android.photogallery.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.UUID;

@ParseClassName("Favorite")
public class Favorite extends ParseObject {

    public static final String FAVORITE_UUID = "favorite_uuid";
    public static final String FAVORITE_PHOTO_ID = "favorite_photo_id";
    public static final String FAVORITE_PHOTO_CAPTION = "favorite_photo_caption";
    public static final String FAVORITE_PHOTO_URL_SMALL = "favorite_photo_url_small";
    public static final String FAVORITE_PHOTO_URL_LARGE = "favorite_photo_url_large";
    public static final String FAVORITE_PHOTO_URL_LARGE_WIDTH = "favorite_photo_url_large_width";
    public static final String FAVORITE_PHOTO_URL_LARGE_HEIGHT = "favorite_photo_url_large_height";
    public static final String FAVORITE_PHOTO_OWNER = "favorite_photo_owner";

    public Photo getPhoto() {
        Photo photo = new Photo();
        photo.setId((String) get(FAVORITE_PHOTO_ID));
        photo.setCaption((String) get(FAVORITE_PHOTO_CAPTION));
        photo.setSmallUrl((String) get(FAVORITE_PHOTO_URL_SMALL));
        photo.setLargeUrl((String) get(FAVORITE_PHOTO_URL_LARGE));
        photo.setLargeUrlWidth((Integer) get(FAVORITE_PHOTO_URL_LARGE_WIDTH));
        photo.setLargeUrlHeight((Integer) get(FAVORITE_PHOTO_URL_LARGE_HEIGHT));
        photo.setOwner((String) get(FAVORITE_PHOTO_OWNER));
        return photo;
    }

    public void setPhoto(Photo photo) {
        put(FAVORITE_PHOTO_ID, photo.getId());
        put(FAVORITE_PHOTO_CAPTION, photo.getCaption());
        put(FAVORITE_PHOTO_URL_SMALL, photo.getSmallUrl());
        put(FAVORITE_PHOTO_URL_LARGE, photo.getLargeUrl());
        put(FAVORITE_PHOTO_URL_LARGE_WIDTH, photo.getLargeUrlWidth());
        put(FAVORITE_PHOTO_URL_LARGE_HEIGHT, photo.getLargeUrlHeight());
        put(FAVORITE_PHOTO_OWNER, photo.getOwner());
    }

    public void setUuidString() {
        UUID uuid = UUID.randomUUID();
        put(FAVORITE_UUID, uuid.toString());
    }

    public String getUuidString() {
        return getString(FAVORITE_UUID);
    }

    public static ParseQuery<Favorite> getQuery() {
        return ParseQuery.getQuery(Favorite.class);
    }

}