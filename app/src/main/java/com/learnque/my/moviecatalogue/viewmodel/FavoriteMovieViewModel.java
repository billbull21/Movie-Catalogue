package com.learnque.my.moviecatalogue.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import com.learnque.my.moviecatalogue.service.db.MovieHelper;
import com.learnque.my.moviecatalogue.service.entity.FavoriteMovie;

import java.util.ArrayList;

public class FavoriteMovieViewModel extends ViewModel {
    private MutableLiveData<ArrayList<FavoriteMovie>> data = new MutableLiveData<>();

    public LiveData<ArrayList<FavoriteMovie>> getData() {
        return data;
    }

    public void setData(Context context) {
        try {
            MovieHelper movieHelper = MovieHelper.getInstance(context);
            movieHelper.open();
            data.postValue(movieHelper.getAllMovie());
            movieHelper.close();
        } catch (Exception e) {
            Log.d("FAIL FETCH DB", e.getMessage());
        }
    }
}
