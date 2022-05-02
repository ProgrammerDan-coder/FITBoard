package com.example.fitboard;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import static com.example.fitboard.DBHelper.KEY_ID;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyContentProvider extends ContentProvider {


    static final Uri CONTENT_URI = Uri.parse("content://com.example.fitboard.mycontentprovider");
    //android:authorities=".MyContentProvider"
    //            android:name="com.example.fitboard.MyContentProvider">

    DBHelper dbHelper;
    static final String CONTACT_PATH = "auth";
    static final String AUTHORITY = "com.example.fitboard";
    static final int URI_CONTACTS = 1;

    static final String CONTACT_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + CONTACT_PATH;



    SQLiteDatabase db;
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreate() {
        Log.d(LOG_TAG, "onCreate");
        dbHelper = new DBHelper(getContext());
        return true;
    }


    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "auth", 100);

    }



    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor = db.query(DBHelper.TABLE_CONTACTS, new String[]{"_id", "name"},"_id =?",new String[]{"123"},
                null,null,null);
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {



        switch (uriMatcher.match(uri)) {
            case URI_CONTACTS:
                return CONTACT_CONTENT_TYPE;

        }


        return null;
    }

    @Nullable
    @Override
    public Uri insert( Uri uri,  ContentValues contentValues) {
        db = dbHelper.getWritableDatabase();
        //if(uriMatcher.match(uri) != UR)

        long rowID = db.insert(DBHelper.TABLE_CONTACTS, DBHelper.KEY_ID, contentValues);



            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;



    }

    // public Uri insert(Uri uri, ContentValues values) {
    //    Log.d(LOG_TAG, "insert, " + uri.toString());
    //    if (uriMatcher.match(uri) != URI_CONTACTS)
    //      throw new IllegalArgumentException("Wrong URI: " + uri);
    //
    //    db = dbHelper.getWritableDatabase();
    //    long rowID = db.insert(CONTACT_TABLE, null, values);
    //    Uri resultUri = ContentUris.withAppendedId(CONTACT_CONTENT_URI, rowID);
    //    // уведомляем ContentResolver, что данные по адресу resultUri изменились
    //    getContext().getContentResolver().notifyChange(resultUri, null);
    //    return resultUri;
    //  }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return db.delete(DBHelper.TABLE_CONTACTS,"_id = ?", new String[] {s});
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
