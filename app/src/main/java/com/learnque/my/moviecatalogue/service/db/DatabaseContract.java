package com.learnque.my.moviecatalogue.service.db;

import android.provider.BaseColumns;

public class DatabaseContract {

    static String TABLE_MOVIE = "movie";
    static final class MovieColumns implements BaseColumns {
        final static String ID_MOVIE = "ID_MOVIE"; //id not primary key...
        final static String TITLE = "TITLE";
        final static String OVERVIEW = "OVERVIEW";
        final static String POSTER = "POSTER";
        final static String POPULARITY = "POPULARITY";
        final static String RATING = "RATING";
    }

    static String TABLE_TV = "tv";
    static final class TvColumns implements BaseColumns {
        final static String ID_TV = "ID_TV"; //id not primary key...
        final static String TITLE = "TITLE";
        final static String OVERVIEW = "OVERVIEW";
        final static String POSTER = "POSTER";
        final static String POPULARITY = "POPULARITY";
        final static String RATING = "RATING";
    }

}
