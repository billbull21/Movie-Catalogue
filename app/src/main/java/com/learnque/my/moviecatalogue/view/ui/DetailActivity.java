package com.learnque.my.moviecatalogue.view.ui;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.learnque.my.moviecatalogue.R;
import com.learnque.my.moviecatalogue.service.db.MovieHelper;
import com.learnque.my.moviecatalogue.service.db.TvHelper;
import com.learnque.my.moviecatalogue.service.entity.FavoriteMovie;
import com.learnque.my.moviecatalogue.service.entity.FavoriteTv;
import com.learnque.my.moviecatalogue.service.model.MovieTv;

public class DetailActivity extends AppCompatActivity {

    private String dataTitle, dataOver, dataPoster, dataPopularity, dataRating, type;
    private Toolbar toolbar;
    private TextView title, desc, popularity, rating;
    private ImageView poster;
    private int dataId;
    private ProgressBar progressBar;
    private Button button;

    private MovieTv dataparcel;

    private MovieHelper movieHelper;
    private FavoriteMovie favoriteMovie;
    private TvHelper tvHelper;
    private FavoriteTv favoriteTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //connect to xml view
        poster = findViewById(R.id.posterView);
        title = findViewById(R.id.titleView);
        desc = findViewById(R.id.descView);
        popularity = findViewById(R.id.popularity);
        rating = findViewById(R.id.rating);
        button = findViewById(R.id.btnBuy);

        //get type
        type = getIntent().getStringExtra("type");

        //don't forget to do this :)
        if (type.equals("movie")) {
            movieHelper = MovieHelper.getInstance(getApplicationContext());
            favoriteMovie = new FavoriteMovie();
        } else if (type.equals("tv")) {
            tvHelper = TvHelper.getInstance(getApplicationContext());
            favoriteTv = new FavoriteTv();
        }

        dataparcel = getIntent().getParcelableExtra("data");

        dataId = dataparcel.getId();
        dataTitle = dataparcel.getTitle();
        dataOver = dataparcel.getOverview();
        dataPoster = dataparcel.getPoster();
        dataPopularity = dataparcel.getPopularity();
        dataRating = dataparcel.getRating();
        bindParcelableData();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("dataPoster", dataPoster);

                FragmentManager fragmentManager = getSupportFragmentManager();
                ShowImageDialogFragment fragment = new ShowImageDialogFragment();

                fragment.setArguments(bundle);
                fragment.show(fragmentManager, ShowImageDialogFragment.class.getSimpleName());
            }
        });

        // toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(dataTitle);
    }

    void bindParcelableData() {
        title.setText(dataTitle);
        desc.setText(dataOver);
        Glide.with(this)
                .load(dataPoster)
                .apply(new RequestOptions().override(100, 150))
                .into(poster);
        String popular = "Popularity : "+dataPopularity;
        String vote = "Overal Rating : "+dataRating;
        popularity.setText(popular);
        rating.setText(vote);

        //for sqlite
        if (type.equals("movie")) {
            favoriteMovie.setMovieId(dataId);
            favoriteMovie.setTitle(dataTitle);
            favoriteMovie.setOverview(dataOver);
            favoriteMovie.setPoster(dataPoster);
            favoriteMovie.setPopularity(dataPopularity);
            favoriteMovie.setRating(dataRating);
        } else if (type.equals("tv")) {
            favoriteTv.setTvId(dataId);
            favoriteTv.setTitle(dataTitle);
            favoriteTv.setOverview(dataOver);
            favoriteTv.setPoster(dataPoster);
            favoriteTv.setPopularity(dataPopularity);
            favoriteTv.setRating(dataRating);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (type.equals("movie")) {
            movieHelper.open();
            if (movieHelper.checkData(dataId)) {
                getMenuInflater().inflate(R.menu.fav_menu_true, menu);
            } else if (!movieHelper.checkData(dataId)) {
                getMenuInflater().inflate(R.menu.fav_menu_false, menu);
            }
            movieHelper.close();
        } else if (type.equals("tv")) {
            tvHelper.open();
            if (tvHelper.checkData(dataId)) {
                getMenuInflater().inflate(R.menu.fav_menu_true, menu);
            } else if (!tvHelper.checkData(dataId)){
                getMenuInflater().inflate(R.menu.fav_menu_false, menu);
            }
            tvHelper.close();
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.add_fav:
                if (type.equals("movie")) {
                    movieHelper.open();
                    if (movieHelper.checkData(dataId)) {
                        long delResult = movieHelper.deleteFavorit(favoriteMovie.getId());
                        if (delResult > 0) {
                            Toast.makeText(this, String.format(getResources().getString(R.string.fav_success_remove), dataTitle), Toast.LENGTH_LONG).show();
                            item.setIcon(R.drawable.ic_favorite_false);
                        }
                    } else {
                        long result = movieHelper.addFavorit(favoriteMovie);
                        if (result > 0) {
                            favoriteMovie.setId((int) result);
                            Toast.makeText(this, String.format(getResources().getString(R.string.fav_success_add)), Toast.LENGTH_LONG).show();
                            item.setIcon(R.drawable.ic_favorite_true);
                        }
                    }
                    movieHelper.close();
                } else if (type.equals("tv")){
                    tvHelper.open();
                    if (tvHelper.checkData(dataId)) {
                        long delResult = tvHelper.deleteFavorit(favoriteTv.getId());
                        if (delResult > 0) {
                            Toast.makeText(this, String.format(getResources().getString(R.string.fav_success_remove), dataTitle), Toast.LENGTH_LONG).show();
                            item.setIcon(R.drawable.ic_favorite_false);
                        }
                    } else {
                        long result = tvHelper.addFavorit(favoriteTv);
                        if (result > 0) {
                            favoriteTv.setId((int) result);
                            Toast.makeText(this, String.format(getResources().getString(R.string.fav_success_add)), Toast.LENGTH_LONG).show();
                            item.setIcon(R.drawable.ic_favorite_true);
                        }
                    }
                    tvHelper.close();
                }
                return true;
            case R.id.del_fav:
                if (type.equals("movie")) {
                    movieHelper.open();
                    if (movieHelper.checkData(dataId)) {
                        long delResult = movieHelper.deleteFavorit(movieHelper.getId(dataId));
                        if (delResult > 0) {
                            Toast.makeText(this, String.format(getResources().getString(R.string.fav_success_remove), dataTitle), Toast.LENGTH_LONG).show();
                            item.setIcon(R.drawable.ic_favorite_false);
                        }
                    } else {
                        long result = movieHelper.addFavorit(favoriteMovie);
                        if (result > 0) {
                            favoriteMovie.setId((int) result);
                            Toast.makeText(this, String.format(getResources().getString(R.string.fav_success_add)), Toast.LENGTH_LONG).show();
                            item.setIcon(R.drawable.ic_favorite_true);
                        }
                    }
                    movieHelper.close();
                } else if (type.equals("tv")) {
                    tvHelper.open();
                    if (tvHelper.checkData(dataId)) {
                        long delResult = tvHelper.deleteFavorit(tvHelper.getId(dataId));
                        if (delResult > 0) {
                            Toast.makeText(this, String.format(getResources().getString(R.string.fav_success_remove), dataTitle), Toast.LENGTH_LONG).show();
                            item.setIcon(R.drawable.ic_favorite_false);
                        }
                    } else {
                        long result = tvHelper.addFavorit(favoriteTv);
                        if (result > 0) {
                            favoriteTv.setId((int) result);
                            Toast.makeText(this, String.format(getResources().getString(R.string.fav_success_add)), Toast.LENGTH_LONG).show();
                            item.setIcon(R.drawable.ic_favorite_true);
                        }
                    }
                    tvHelper.close();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
