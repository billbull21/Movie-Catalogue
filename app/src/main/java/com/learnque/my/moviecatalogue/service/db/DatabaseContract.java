package com.learnque.my.moviecatalogue.service.db;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {

    public static final String AUTHORITY = "com.learnque.my.moviecatalogue";
    public static final String AUTHORITY_TV = "com.learnque.my.tvcatalogue";
    private static final String SCHEME = "content";

    private DatabaseContract() {}

    public static final class MovieColumns implements BaseColumns {
        public static final String TABLE_MOVIE = "movie";
        public static final String ID_MOVIE = "ID_MOVIE"; //id not primary key...
        public static final String TITLE = "TITLE";
        public static final String OVERVIEW = "OVERVIEW";
        public static final String POSTER = "POSTER";
        public static final String RELEASE = "RELEASE";
        public static final String POPULARITY = "POPULARITY";
        public static final String RATING = "RATING";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_MOVIE)
                .build();
    }

    public static final class TvColumns implements BaseColumns {
        public static final String TABLE_TV = "tv";
        public static final String ID_TV = "ID_TV"; //id not primary key...
        public static final String TITLE = "TITLE";
        public static final String OVERVIEW = "OVERVIEW";
        public static final String POSTER = "POSTER";
        public static final String RELEASE = "RELEASE";
        public static final String POPULARITY = "POPULARITY";
        public static final String RATING = "RATING";

        public static final Uri CONTENT_URI_TV = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY_TV)
                .appendPath(TABLE_TV)
                .build();
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }
    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }
    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }

}
