package com.learnque.my.moviecatalogue.service.helper;

import android.database.Cursor;

import com.learnque.my.moviecatalogue.service.entity.FavoriteTv;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.TvColumns.ID_TV;
import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.TvColumns.OVERVIEW;
import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.TvColumns.POPULARITY;
import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.TvColumns.POSTER;
import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.TvColumns.RATING;
import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.TvColumns.RELEASE;
import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.TvColumns.TITLE;

public class TvMappingHelper {

    public static ArrayList<FavoriteTv> mapCursorToArrayList(Cursor tvCursor) {
        ArrayList<FavoriteTv> tvs = new ArrayList<>();

        while (tvCursor.moveToNext()) {
            int id = tvCursor.getInt(tvCursor.getColumnIndexOrThrow(_ID));
            int movieId = tvCursor.getInt(tvCursor.getColumnIndexOrThrow(ID_TV));
            String title = tvCursor.getString(tvCursor.getColumnIndexOrThrow(TITLE));
            String overview = tvCursor.getString(tvCursor.getColumnIndexOrThrow(OVERVIEW));
            String poster = tvCursor.getString(tvCursor.getColumnIndexOrThrow(POSTER));
            String release = tvCursor.getString(tvCursor.getColumnIndexOrThrow(RELEASE));
            String popularity = tvCursor.getString(tvCursor.getColumnIndexOrThrow(POPULARITY));
            String rating = tvCursor.getString(tvCursor.getColumnIndexOrThrow(RATING));
            tvs.add(new FavoriteTv(id, movieId, title, overview, poster, release, popularity, rating));
        }
        return tvs;
    }

}
