# PhotoGalleryPlusPlus
[![Build Status](https://travis-ci.org/matthew-compton/PhotoGalleryPlusPlus.svg?branch=master)](https://travis-ci.org/matthew-compton/PhotoGalleryPlusPlus)

### About
A PhotoGallery implementation that includes Facebook Messenger and Parse integration.

### Directions for Sharing Images with Facebook Messenger
This is a step-by-step guide of adding a share-to-Facebook-Messenger functionality into PhotoGallery.

#### Step 1)
Go to to the [Facebook Developer portal](https://developers.facebook.com/quickstarts/?platform=android) and create a new Android project.

#### Step 2) Add App Id to Strings.xml
`<string name="facebook_app_id">XXXXXXXXXXXXXXX</string>`

#### Step 3) Add App Id to AndroidManifest.xml
```
...

<application
    android:icon="@drawable/ic_launcher"
    android:label="@string/app_name"
    android:theme="@style/AppTheme">

    <meta-data
        android:name="com.facebook.sdk.ApplicationId"
        android:value="@string/facebook_app_id"/>

        ...
```

#### Step 4) Add Facebook SDK Dependency
`compile 'com.facebook.android:facebook-android-sdk:4.0.0'`

#### Step 5) Add New Permission to AndroidManifest.xml
`<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>`

#### Step 6) Add Share Functionality
```
Drawable mDrawable = mImageView.getDrawable();
Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();
String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), mBitmap, "Image Description", null);
Uri uri = Uri.parse(path);

ShareToMessengerParams shareToMessengerParams = ShareToMessengerParams.newBuilder(
                uri,
                "image/*"
).build();

MessengerUtils.shareToMessenger(getActivity(), REQUEST_CODE_SHARE_TO_MESSENGER, shareToMessengerParams);
```

### Directions for Saving Recent Searches with Parse
Go to the [Parse Application Portal](https://www.parse.com/apps), signup, and then add a new app.

#### Step 1) Add Parse Dependency
The top-level `build.gradle` file should have a section like this:
```
allprojects {
    repositories {
        jcenter()
        maven {
            url "https://jitpack.io"
        }
    }
}
```

While the app `build.gradle` should include these depencencies:
```
compile 'com.github.yongjhih:parse:1.9.2'
compile 'com.parse.bolts:bolts-android:1.2.0'
```

#### Step 2) Create Search Model Object
This class will represent a single search query that we want to save.

```
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
```

#### Step 3) Setup Parse in Application
```
public class PhotoGalleryApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Search.class);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "YOUR_APP_ID", "YOUR_CLIENT_KEY");
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);
    }

}
```

#### Step 4) Use Parse
Here are some basic operations to start with:

##### Save a Single Search
```
Search search = new Search();
search.setUuidString();
search.setText(query);
search.setAuthor(ParseUser.getCurrentUser());
search.pinInBackground("ALL_SEARCHES");
```

##### Load All Searches
```
ParseQuery<Search> query = Search.getQuery();
query.fromLocalDatastore();
query.findInBackground((list, e) -> {
    // Do something with list
});
```

##### Delete All Searches
```
ParseObject.unpinAllInBackground("ALL_SEARCHES");
```

### License
Copyright 2015 Matthew Compton

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.