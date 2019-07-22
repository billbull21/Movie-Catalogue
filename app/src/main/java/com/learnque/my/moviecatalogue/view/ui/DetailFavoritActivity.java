package com.learnque.my.moviecatalogue.view.ui;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.learnque.my.moviecatalogue.R;
import com.learnque.my.moviecatalogue.service.db.MovieHelper;
import com.learnque.my.moviecatalogue.service.db.TvHelper;
import com.learnque.my.moviecatalogue.service.entity.FavoriteMovie;
import com.learnque.my.moviecatalogue.service.entity.FavoriteTv;

public class DetailFavoritActivity extends AppCompatActivity {

    private String dataTitle, dataOver, dataPoster, dataPopuler, dataRating, type;
    private TextView title, desc, popularity, rating;
    private boolean state = false;
    private ImageView poster;
    private Toolbar toolbar;
    private Button button;
    private int position, dataId;

    public static final int REQUEST_DELETE = 500;
    public static final int RESULT_DELETE = 404;

    private MovieHelper movieHelper;
    private FavoriteMovie favoriteMovie;
    private TvHelper tvHelper;
    private FavoriteTv favoriteTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_favorit);

        //connect to xml view
        poster = findViewById(R.id.posterView);
        title = findViewById(R.id.titleView);
        desc = findViewById(R.id.descView);
        popularity = findViewById(R.id.popularity);
        rating = findViewById(R.id.rating);

        //don't forget to do this :)
        movieHelper = MovieHelper.getInstance(getApplicationContext());
        tvHelper = TvHelper.getInstance(getApplicationContext());
        favoriteMovie = new FavoriteMovie();
        favoriteTv = new FavoriteTv();

        // get data from ...
        type = getIntent().getStringExtra("type");

        //parcelable
        if (type.equals("movie")) {
            FavoriteMovie dataParcel = getIntent().getParcelableExtra("data");
            //insert into variable
            //dataId = dataParcel.getMovieId(); // i don't know why this variable has 0 value
            dataId = getIntent().getIntExtra("movie_index", 0); //so, i used this to get movie_id.
            dataTitle = dataParcel.getTitle();
            dataOver = dataParcel.getOverview();
            dataPoster = dataParcel.getPoster();
            dataPopuler = dataParcel.getPopularity();
            dataRating = dataParcel.getRating();
        } else if (type.equals("tv")) {
            FavoriteTv dataParcel = getIntent().getParcelableExtra("data");
            //insert into variable
            //dataId = dataParcel.getTvId(); //also this.
            dataId = getIntent().getIntExtra("tv_index", 0); //dicoding reviewer. please, tell me the problem of this issue.
            dataTitle = dataParcel.getTitle();
            dataOver = dataParcel.getOverview();
            dataPoster = dataParcel.getPoster();
            dataPopuler = dataParcel.getPopularity();
            dataRating = dataParcel.getRating();
        }

        showParcelableData();

        // button to show poster
        button = findViewById(R.id.btnBuy);
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

        position = getIntent().getIntExtra("position", 0);
    }

    private void showParcelableData() {
        Glide.with(this)
                .load(dataPoster)
                .apply(new RequestOptions().override(100, 150))
                .into(poster);
        title.setText(dataTitle);
        desc.setText(dataOver);
        String pops = String.format(getResources().getString(R.string.pops), dataPopuler);
        String rat = String.format(getResources().getString(R.string.rating), dataRating);
        popularity.setText(pops);
        rating.setText(rat);

        //insert for SQLite
        if (type.equals("movie")) {
            favoriteMovie.setMovieId(dataId);
            favoriteMovie.setTitle(dataTitle);
            favoriteMovie.setOverview(dataOver);
            favoriteMovie.setPoster(dataPoster);
            favoriteMovie.setPopularity(dataPopuler);
            favoriteMovie.setRating(dataRating);
        } else if (type.equals("tv")) {
            favoriteTv.setTvId(dataId);
            favoriteTv.setTitle(dataTitle);
            favoriteTv.setOverview(dataOver);
            favoriteTv.setPoster(dataPoster);
            favoriteTv.setPopularity(dataPopuler);
            favoriteTv.setRating(dataRating);
        }
    }

    /**
     * Menu is right here
     */

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
        Intent intent = new Intent();
        intent.putExtra("index", position);
        switch (item.getItemId()) {
            case android.R.id.home:
                if (state)
                    setResult(RESULT_DELETE, intent);
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
                            state = true;
                        }
                    } else {
                        long result = movieHelper.addFavorit(favoriteMovie);
                        if (result > 0) {
                            favoriteMovie.setId((int) result);
                            Toast.makeText(this, String.format(getResources().getString(R.string.fav_success_add)), Toast.LENGTH_LONG).show();
                            item.setIcon(R.drawable.ic_favorite_true);
                            state = false;
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
                            state = true;
                        }
                    } else {
                        long result = tvHelper.addFavorit(favoriteTv);
                        if (result > 0) {
                            favoriteTv.setId((int) result);
                            Toast.makeText(this, String.format(getResources().getString(R.string.fav_success_add)), Toast.LENGTH_LONG).show();
                            item.setIcon(R.drawable.ic_favorite_true);
                            state = false;
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
                            state = true;
                        }
                    } else {
                        long result = movieHelper.addFavorit(favoriteMovie);
                        if (result > 0) {
                            favoriteMovie.setId((int) result);
                            Toast.makeText(this, String.format(getResources().getString(R.string.fav_success_add)), Toast.LENGTH_LONG).show();
                            item.setIcon(R.drawable.ic_favorite_true);
                            state = false;
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
                            state = true;
                        }
                    } else {
                        long result = tvHelper.addFavorit(favoriteTv);
                        if (result > 0) {
                            favoriteTv.setId((int) result);
                            Toast.makeText(this, String.format(getResources().getString(R.string.fav_success_add)), Toast.LENGTH_LONG).show();
                            item.setIcon(R.drawable.ic_favorite_true);
                            state = false;
                        }
                    }
                    tvHelper.close();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("index", position);
        if (state)
            setResult(RESULT_DELETE, intent);
        super.onBackPressed();
    }
}
