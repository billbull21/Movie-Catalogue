package com.learnque.my.moviecatalogue.view.ui;

import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.learnque.my.moviecatalogue.R;
import com.learnque.my.moviecatalogue.service.model.MovieTv;

public class DetailActivity extends AppCompatActivity {

    private String dataTitle, dataOver, dataPoster, dataPopularity, dataRating;
    private Toolbar toolbar;
    private TextView title, desc, popularity, rating;
    private ImageView poster;
    private ProgressBar progressBar;
    private Button button;

    private MovieTv dataparcel;

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

        dataparcel = getIntent().getParcelableExtra("data");
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
