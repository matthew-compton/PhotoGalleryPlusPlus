package com.ambergleam.android.photogallery.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.UUID;

@ParseClassName("Search")
public class Search extends ParseObject {

    public static final String SEARCH_UUID = "uuid";
    public static final String SEARCH_TEXT = "text";

    public String getText() {
        return getString(SEARCH_TEXT);
    }

    public void setText(String text) {
        put(SEARCH_TEXT, text);
    }

    public void setUuidString() {
        UUID uuid = UUID.randomUUID();
        put(SEARCH_UUID, uuid.toString());
    }

    public String getUuidString() {
        return getString(SEARCH_UUID);
    }

    public static ParseQuery<Search> getQuery() {
        return ParseQuery.getQuery(Search.class);
    }

}