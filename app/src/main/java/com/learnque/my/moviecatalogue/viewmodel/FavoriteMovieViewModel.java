package com.learnque.my.moviecatalogue.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.learnque.my.moviecatalogue.service.entity.FavoriteMovie;
import com.learnque.my.moviecatalogue.service.helper.MovieMappingHelper;

import java.util.ArrayList;

import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.MovieColumns.CONTENT_URI;

public class FavoriteMovieViewModel extends ViewModel {
    private MutableLiveData<ArrayList<FavoriteMovie>> data = new MutableLiveData<>();

    public LiveData<ArrayList<FavoriteMovie>> getData() {
        return data;
    }

    public void setData(Context context) {
        try {
            Cursor cursor = context.getContentResolver().query(CONTENT_URI, null, null, null, null);
            ArrayList<FavoriteMovie> favoriteMovies = MovieMappingHelper.mapCursorToArrayList(cursor);
            data.postValue(favoriteMovies);
        } catch (Exception e) {
            Log.e("FAIL FETCH DB", e.getMessage());
        }
    }
}
