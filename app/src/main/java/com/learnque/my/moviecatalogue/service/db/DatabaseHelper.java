package com.learnque.my.moviecatalogue.service.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "dbMovieTv.db";

    private static final int DATABASE_VERSION = 1;

    /**
     * MOVIE TABLE
     */

    private static final String SQL_CREATE_TABLE_MOVIE = String.format("CREATE TABLE %s"
                    +" (%s INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    " %s INTEGER DEFAULT 0,"+
                    " %s TEXT NOT NULL,"+
                    " %s TEXT NOT NULL,"+
                    " %s TEXT NOT NULL,"+
                    " %s TEXT NOT NULL,"+
                    " %s TEXT NOT NULL,"+
                    " %s TEXT NOT NULL)",
            DatabaseContract.MovieColumns.TABLE_MOVIE,
            DatabaseContract.MovieColumns._ID,
            DatabaseContract.MovieColumns.ID_MOVIE,
            DatabaseContract.MovieColumns.TITLE,
            DatabaseContract.MovieColumns.OVERVIEW,
            DatabaseContract.MovieColumns.POSTER,
            DatabaseContract.MovieColumns.RELEASE,
            DatabaseContract.MovieColumns.POPULARITY,
            DatabaseContract.MovieColumns.RATING);

    /**
     * TV TABLE
     */

    private static final String SQL_CREATE_TABLE_TV = String.format("CREATE TABLE %s"
                    +" (%s INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    " %s INTEGER DEFAULT 0,"+
                    " %s TEXT NOT NULL,"+
                    " %s TEXT NOT NULL,"+
                    " %s TEXT NOT NULL,"+
                    " %s TEXT NOT NULL,"+
                    " %s TEXT NOT NULL,"+
                    " %s TEXT NOT NULL)",
            DatabaseContract.TvColumns.TABLE_TV,
            DatabaseContract.TvColumns._ID,
            DatabaseContract.TvColumns.ID_TV,
            DatabaseContract.TvColumns.TITLE,
            DatabaseContract.TvColumns.OVERVIEW,
            DatabaseContract.TvColumns.POSTER,
            DatabaseContract.TvColumns.RELEASE,
            DatabaseContract.TvColumns.POPULARITY,
            DatabaseContract.TvColumns.RATING);

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_MOVIE);
        db.execSQL(SQL_CREATE_TABLE_TV);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+DatabaseContract.MovieColumns.TABLE_MOVIE);
        db.execSQL("DROP TABLE IF EXISTS "+DatabaseContract.TvColumns.TABLE_TV);
        onCreate(db);
    }
}
