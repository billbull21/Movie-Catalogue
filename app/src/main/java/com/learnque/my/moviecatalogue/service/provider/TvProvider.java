package com.learnque.my.moviecatalogue.service.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.learnque.my.moviecatalogue.service.db.TvHelper;

import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.AUTHORITY;
import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.AUTHORITY_TV;
import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.TvColumns.CONTENT_URI_TV;
import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.TvColumns.TABLE_TV;

public class TvProvider extends ContentProvider {

    private static final int TV = 6;
    private static final int TV_ID = 7;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private TvHelper tvHelper;

    static {
        // content://com.learnque.my.moviecatalogue/tv
        sUriMatcher.addURI(AUTHORITY_TV, TABLE_TV, TV);
        // content://com.learnque.my.moviecatalogue/tv/id
        sUriMatcher.addURI(AUTHORITY_TV, TABLE_TV + "/#", TV_ID);
    }

    @Override
    public boolean onCreate() {
        tvHelper = TvHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        tvHelper.open();
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case TV:
                cursor = tvHelper.queryProvider();
                break;
            case TV_ID:
                cursor = tvHelper.queryByIdProvider(uri.getLastPathSegment());
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
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        tvHelper.open();
        long added;
        switch (sUriMatcher.match(uri)) {
            case TV:
                added = tvHelper.insertProvider(values);
                break;
            default:
                added = 0;
                break;
        }
        return Uri.parse(CONTENT_URI_TV+"/"+added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        tvHelper.open();
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case TV_ID:
                deleted = tvHelper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        tvHelper.open();
        int updated;
        switch (sUriMatcher.match(uri)) {
            case TV_ID:
                updated = tvHelper.updateProvider(uri.getLastPathSegment(), values);
                break;
            default:
                updated = 0;
                break;
        }
        return updated;
    }
}
