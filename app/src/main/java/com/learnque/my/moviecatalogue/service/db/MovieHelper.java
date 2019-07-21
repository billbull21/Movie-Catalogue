package com.learnque.my.moviecatalogue.service.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.learnque.my.moviecatalogue.service.entity.FavoriteMovie;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.MovieColumns.ID_MOVIE;
import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.MovieColumns.OVERVIEW;
import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.MovieColumns.POPULARITY;
import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.MovieColumns.POSTER;
import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.MovieColumns.RATING;
import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.MovieColumns.TITLE;
import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.MovieColumns.TABLE_MOVIE;

public class MovieHelper {
    private static final String DATABASE_TABLE_MOVIE = TABLE_MOVIE;
    private static DatabaseHelper dataBaseHelper;
    private static MovieHelper INSTANCE;

    private static SQLiteDatabase database;

    private MovieHelper(Context context) {
        dataBaseHelper = new DatabaseHelper(context);
    }

    // single-toon pattern
    public static MovieHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MovieHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    //open connection
    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    //close connection
    public void close() {
        dataBaseHelper.close();
        if (database.isOpen())
            database.close();
    }

    /**
     * CRUD METHOD SQLite
     */

    public ArrayList<FavoriteMovie> getAllMovie() {
        ArrayList<FavoriteMovie> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE_MOVIE,
                null,
                null,
                null,
                null,
                null,
                _ID+" DESC",
                null);
        cursor.moveToFirst();
        FavoriteMovie favoriteMovie;
        if (cursor.getCount() > 0) {
            do {
                favoriteMovie = new FavoriteMovie();
                favoriteMovie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                favoriteMovie.setMovieId(cursor.getInt(cursor.getColumnIndexOrThrow(ID_MOVIE)));
                favoriteMovie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                favoriteMovie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                favoriteMovie.setPoster(cursor.getString(cursor.getColumnIndexOrThrow(POSTER)));
                favoriteMovie.setPopularity(cursor.getString(cursor.getColumnIndexOrThrow(POPULARITY)));
                favoriteMovie.setRating(cursor.getString(cursor.getColumnIndexOrThrow(RATING)));

                arrayList.add(favoriteMovie);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public boolean checkData(int id) {
        String[] col = {
                _ID
        };
        String selection = "ID_MOVIE=?";
        String[] selectionArgs = {
                String.valueOf(id)
        };

        Cursor cursor = database.query(DATABASE_TABLE_MOVIE,
                col,
                selection,
                selectionArgs,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        if (cursor.getCount() <= 0){
            cursor.close();
            return false;
        } else {
            cursor.close();
            return true;
        }
    }

    public long addFavorit(FavoriteMovie favoriteMovie) {
        ContentValues args = new ContentValues();
        args.put(ID_MOVIE, favoriteMovie.getMovieId());
        args.put(TITLE, favoriteMovie.getTitle());
        args.put(OVERVIEW, favoriteMovie.getOverview());
        args.put(POSTER, favoriteMovie.getPoster());
        args.put(POPULARITY, favoriteMovie.getPopularity());
        args.put(RATING, favoriteMovie.getRating());
        return database.insert(DATABASE_TABLE_MOVIE, null, args);
    }

    public int getId(int id) {
        String[] col = {
                _ID
        };
        String selection = "ID_MOVIE=?";
        String[] selectionArgs = {
                String.valueOf(id)
        };

        Cursor cursor = database.query(DATABASE_TABLE_MOVIE,
                col,
                selection,
                selectionArgs,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        if(cursor.getCount() <= 0){
            cursor.close();
            return -1;
        } else {
            int itemId = cursor.getInt(
                    cursor.getColumnIndexOrThrow(_ID)
            );
            cursor.close();
            return itemId;
        }
    }

    public int deleteFavorit(int id){
        return database.delete(TABLE_MOVIE, _ID+" = '"+id+"'", null);
    }

    public Cursor queryByIdProvider(String id) {
        return database.query(DATABASE_TABLE_MOVIE, null
                , _ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public Cursor queryProvider() {
        return database.query(DATABASE_TABLE_MOVIE
                , null
                , null
                , null
                , null
                , null
                , _ID + " DESC");
    }

    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE_MOVIE, null, values);
    }

    public int updateProvider(String id, ContentValues values) {
        return database.update(DATABASE_TABLE_MOVIE, values, _ID + " = ?", new String[]{id});
    }

    public int deleteProvider(String id) {
        return database.delete(DATABASE_TABLE_MOVIE, _ID + " = ?", new String[]{id});
    }
}
