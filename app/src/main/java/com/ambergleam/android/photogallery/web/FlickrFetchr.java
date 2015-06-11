package com.ambergleam.android.photogallery.web;

import android.net.Uri;

import com.ambergleam.android.photogallery.BaseConstants;
import com.ambergleam.android.photogallery.model.Photo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import timber.log.Timber;

public class FlickrFetchr {

    public static final String PREF_SEARCH_QUERY = "searchQuery";
    public static final String PREF_LAST_RESULT_ID = "lastResultId";

    private static final String ENDPOINT = "https://api.flickr.com/services/rest/";
    private static final String METHOD_GET_RECENT = "flickr.photos.getRecent";
    private static final String METHOD_SEARCH = "flickr.photos.search";
    private static final String PARAM_EXTRAS = "extras";
    private static final String PARAM_TEXT = "text";
    private static final String EXTRA_SMALL_URL = "url_q";
    private static final String EXTRA_LARGE_URL = "url_z";
    private static final String XML_PHOTO = "photo";

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    private String getUrl(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public ArrayList<Photo> getPhotos() {
        String url = Uri.parse(ENDPOINT).buildUpon()
                .appendQueryParameter("method", METHOD_GET_RECENT)
                .appendQueryParameter("api_key", BaseConstants.FLICKR_API_KEY)
                .appendQueryParameter(PARAM_EXTRAS, EXTRA_SMALL_URL + "," + EXTRA_LARGE_URL)
                .build().toString();
        return downloadGalleryItems(url);
    }

    public ArrayList<Photo> getPhotos(String query) {
        String url = Uri.parse(ENDPOINT).buildUpon()
                .appendQueryParameter("method", METHOD_SEARCH)
                .appendQueryParameter("api_key", BaseConstants.FLICKR_API_KEY)
                .appendQueryParameter(PARAM_EXTRAS, EXTRA_SMALL_URL + "," + EXTRA_LARGE_URL)
                .appendQueryParameter(PARAM_TEXT, query)
                .build().toString();
        return downloadGalleryItems(url);
    }

    private ArrayList<Photo> downloadGalleryItems(String url) {
        ArrayList<Photo> items = new ArrayList<Photo>();
        try {
            String xmlString = getUrl(url);
            Timber.i("Received xml: " + xmlString);
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xmlString));
            parseItems(items, parser);
        } catch (IOException ioe) {
            Timber.e("Failed to fetch items", ioe);
        } catch (XmlPullParserException xppe) {
            Timber.e("Failed to parse items", xppe);
        }
        return items;
    }

    private void parseItems(ArrayList<Photo> items, XmlPullParser parser) throws XmlPullParserException, IOException {
        int eventType = parser.next();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && XML_PHOTO.equals(parser.getName())) {
                String id = parser.getAttributeValue(null, "id");
                String caption = parser.getAttributeValue(null, "title");
                String smallUrl = parser.getAttributeValue(null, EXTRA_SMALL_URL);
                String largeUrl = parser.getAttributeValue(null, EXTRA_LARGE_URL);
                String largeUrlWidth = parser.getAttributeValue(null, "width_z");
                String largeUrlHeight = parser.getAttributeValue(null, "height_z");
                String owner = parser.getAttributeValue(null, "owner");

                if (id != null &&
                        caption != null &&
                        smallUrl != null &&
                        largeUrl != null &&
                        largeUrlWidth != null &&
                        largeUrlHeight != null &&
                        owner != null
                        ) {
                    Photo item = new Photo();
                    item.setId(id);
                    item.setCaption(caption);
                    item.setSmallUrl(smallUrl);
                    item.setLargeUrl(largeUrl);
                    item.setLargeUrlWidth(Integer.parseInt(largeUrlWidth));
                    item.setLargeUrlHeight(Integer.parseInt(largeUrlHeight));
                    item.setOwner(owner);
                    items.add(item);
                }
            }

            eventType = parser.next();
        }
    }

}