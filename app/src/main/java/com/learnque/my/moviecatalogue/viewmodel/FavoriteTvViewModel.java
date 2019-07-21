package com.learnque.my.moviecatalogue.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.learnque.my.moviecatalogue.service.entity.FavoriteTv;
import com.learnque.my.moviecatalogue.service.helper.TvMappingHelper;

import java.util.ArrayList;

import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.TvColumns.CONTENT_URI_TV;

public class FavoriteTvViewModel extends ViewModel {

    private MutableLiveData<ArrayList<FavoriteTv>> data = new MutableLiveData<>();

    public LiveData<ArrayList<FavoriteTv>> getData() {
        return data;
    }

    public void setData(Context context) {
        try {
            Cursor cursor = context.getContentResolver().query(CONTENT_URI_TV, null, null, null, null);
            ArrayList<FavoriteTv> favoriteTvs = TvMappingHelper.mapCursorToArrayList(cursor);
            data.postValue(favoriteTvs);
        } catch (Exception e) {
            Log.e("FAIL FETCH DB", e.getMessage());
        }
    }
}
