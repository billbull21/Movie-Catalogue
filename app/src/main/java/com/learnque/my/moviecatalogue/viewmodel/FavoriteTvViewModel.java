package com.learnque.my.moviecatalogue.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import com.learnque.my.moviecatalogue.service.db.TvHelper;
import com.learnque.my.moviecatalogue.service.entity.FavoriteTv;

import java.util.ArrayList;

public class FavoriteTvViewModel extends ViewModel {
    private MutableLiveData<ArrayList<FavoriteTv>> data = new MutableLiveData<>();

    public LiveData<ArrayList<FavoriteTv>> getData() {
        return data;
    }

    public void setData(Context context) {
        try {
            TvHelper tvHelper = TvHelper.getInstance(context);
            tvHelper.open();
            data.postValue(tvHelper.getAllMovie());
            tvHelper.close();
        } catch (Exception e) {
            Log.d("FAIL FETCH DB", e.getMessage());
        }
    }
}
