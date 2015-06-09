package com.ambergleam.android.photogallery.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.UUID;

@ParseClassName("Search")
public class Search extends ParseObject {

    public String getText() {
        return getString("text");
    }

    public void setText(String text) {
        put("text", text);
    }

    public ParseUser getAuthor() {
        return getParseUser("author");
    }

    public void setAuthor(ParseUser currentUser) {
        put("author", currentUser);
    }

    public void setUuidString() {
        UUID uuid = UUID.randomUUID();
        put("uuid", uuid.toString());
    }

    public String getUuidString() {
        return getString("uuid");
    }

    public static ParseQuery<Search> getQuery() {
        return ParseQuery.getQuery(Search.class);
    }

}