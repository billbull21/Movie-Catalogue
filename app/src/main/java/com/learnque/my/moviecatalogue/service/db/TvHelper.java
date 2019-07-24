package com.learnque.my.moviecatalogue.service.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.TvColumns.TABLE_TV;

public class TvHelper {
    private static final String DATABASE_TABLE_TV = TABLE_TV;
    private static DatabaseHelper dataBaseHelper;
    private static TvHelper INSTANCE;

    private static SQLiteDatabase database;

    private TvHelper(Context context) {
        dataBaseHelper = new DatabaseHelper(context);
    }

    // single-toon pattern
    public static TvHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TvHelper(context);
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

    public ArrayList<FavoriteTv> getAllMovie() {
        ArrayList<FavoriteTv> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE_TV,
                null,
                null,
                null,
                null,
                null,
                _ID+" DESC",
                null);
        cursor.moveToFirst();
        FavoriteTv favoriteTV;
        if (cursor.getCount() > 0) {
            do {
                favoriteTV = new FavoriteTv();
                favoriteTV.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                favoriteTV.setTvId(cursor.getInt(cursor.getColumnIndexOrThrow(ID_TV)));
                favoriteTV.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                favoriteTV.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                favoriteTV.setPoster(cursor.getString(cursor.getColumnIndexOrThrow(POSTER)));
                favoriteTV.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE)));
                favoriteTV.setPopularity(cursor.getString(cursor.getColumnIndexOrThrow(POPULARITY)));
                favoriteTV.setRating(cursor.getString(cursor.getColumnIndexOrThrow(RATING)));

                arrayList.add(favoriteTV);
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
        String selection = "ID_TV=?";
        String[] selectionArgs = {
                String.valueOf(id)
        };

        Cursor cursor = database.query(DATABASE_TABLE_TV,
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

    public long addFavorit(FavoriteTv favoriteTV) {
        ContentValues args = new ContentValues();
        args.put(ID_TV, favoriteTV.getTvId());
        args.put(TITLE, favoriteTV.getTitle());
        args.put(OVERVIEW, favoriteTV.getOverview());
        args.put(POSTER, favoriteTV.getPoster());
        args.put(RELEASE, favoriteTV.getReleaseDate());
        args.put(POPULARITY, favoriteTV.getPopularity());
        args.put(RATING, favoriteTV.getRating());
        return database.insert(DATABASE_TABLE_TV, null, args);
    }

    public int getId(int id) {
        String[] col = {
                _ID
        };
        String selection = "ID_TV=?";
        String[] selectionArgs = {
                String.valueOf(id)
        };

        Cursor cursor = database.query(DATABASE_TABLE_TV,
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
        return database.delete(TABLE_TV, _ID+" = '"+id+"'", null);
    }

    public Cursor queryByIdProvider(String id) {
        return database.query(DATABASE_TABLE_TV, null
                , _ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public Cursor queryProvider() {
        return database.query(DATABASE_TABLE_TV
                , null
                , null
                , null
                , null
                , null
                , _ID + " DESC");
    }

    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE_TV, null, values);
    }

    public int updateProvider(String id, ContentValues values) {
        return database.update(DATABASE_TABLE_TV, values, _ID + " = ?", new String[]{id});
    }

    public int deleteProvider(String id) {
        return database.delete(DATABASE_TABLE_TV, _ID + " = ?", new String[]{id});
    }
}
