package com.learnque.my.moviecatalogue.view.utility;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.learnque.my.moviecatalogue.R;
import com.learnque.my.moviecatalogue.service.db.MovieHelper;
import com.learnque.my.moviecatalogue.service.entity.FavoriteMovie;

import java.util.ArrayList;

public class StackRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private ArrayList<FavoriteMovie> mWidgetItems = new ArrayList<>();
    private final Context mContext;
    private MovieHelper helper;

    StackRemoteViewFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {
        helper = MovieHelper.getInstance(mContext);
    }

    @Override
    public void onDataSetChanged() {
        helper.open();
        mWidgetItems = helper.getAllMovie();
        helper.close();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        try {
            Bitmap bitmap = Glide.with(mContext)
                    .asBitmap()
                    .load(mWidgetItems.get(position).getPoster())
                    .submit(300, 350)
                    .get();

            rv.setImageViewBitmap(R.id.imageView, bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bundle extras = new Bundle();
        extras.putInt(StackViewWidget.EXTRA_ITEM, position);
        Intent fill = new Intent();
        fill.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imageView, fill);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
