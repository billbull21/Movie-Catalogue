package com.learnque.my.moviecatalogue.service.helper;

import android.database.Cursor;

import com.learnque.my.moviecatalogue.service.entity.FavoriteMovie;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.MovieColumns.ID_MOVIE;
import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.MovieColumns.OVERVIEW;
import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.MovieColumns.POPULARITY;
import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.MovieColumns.POSTER;
import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.MovieColumns.RATING;
import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.MovieColumns.TITLE;

public class MovieMappingHelper {

    public static ArrayList<FavoriteMovie> mapCursorToArrayList(Cursor movieCursor) {
        ArrayList<FavoriteMovie> movies = new ArrayList<>();

        while (movieCursor.moveToNext()) {
            int id = movieCursor.getInt(movieCursor.getColumnIndexOrThrow(_ID));
            int movieId = movieCursor.getInt(movieCursor.getColumnIndexOrThrow(ID_MOVIE));
            String title = movieCursor.getString(movieCursor.getColumnIndexOrThrow(TITLE));
            String overview = movieCursor.getString(movieCursor.getColumnIndexOrThrow(OVERVIEW));
            String poster = movieCursor.getString(movieCursor.getColumnIndexOrThrow(POSTER));
            String popularity = movieCursor.getString(movieCursor.getColumnIndexOrThrow(POPULARITY));
            String rating = movieCursor.getString(movieCursor.getColumnIndexOrThrow(RATING));
            movies.add(new FavoriteMovie(id, movieId, title, overview, poster, popularity, rating));
        }
        return movies;
    }

}
