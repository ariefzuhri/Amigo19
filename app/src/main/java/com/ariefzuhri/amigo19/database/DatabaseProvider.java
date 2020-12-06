package com.ariefzuhri.amigo19.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.ariefzuhri.amigo19.database.DatabaseContract.AUTHORITY;
import static com.ariefzuhri.amigo19.database.DatabaseContract.MeetColumns.TABLE_MEET;

public class DatabaseProvider extends ContentProvider {
    private static final int MEET = 100;
    private static final int MEET_ID = 101;

    private MeetHelper meetHelper;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, TABLE_MEET, MEET);
        uriMatcher.addURI(AUTHORITY, TABLE_MEET + "/#", MEET_ID);
    }

    public DatabaseProvider(){}

    @Override
    public boolean onCreate() {
        meetHelper = MeetHelper.getInstance(getContext());
        meetHelper.open();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        switch (uriMatcher.match(uri)){
            case MEET:
                cursor = meetHelper.queryAll();
                break;

            case MEET_ID:
                cursor = meetHelper.query(uri.getLastPathSegment());
                break;

            default:
                cursor = null;
                break;
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long added;

        if (uriMatcher.match(uri) == MEET) added = meetHelper.insert(contentValues);
        else added = 0;

        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(uri + "/" + added);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        int updated;

        if (uriMatcher.match(uri) == MEET_ID) updated = meetHelper.update(uri.getLastPathSegment(), contentValues);
        else updated = 0;

        getContext().getContentResolver().notifyChange(uri, null);
        return updated;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int deleted;

        if (uriMatcher.match(uri) == MEET_ID) deleted = meetHelper.delete(uri.getLastPathSegment());
        else deleted = 0;

        getContext().getContentResolver().notifyChange(uri, null);
        return deleted;
    }
}