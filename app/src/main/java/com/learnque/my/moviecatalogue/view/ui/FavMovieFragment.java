package com.learnque.my.moviecatalogue.view.ui;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.learnque.my.moviecatalogue.R;
import com.learnque.my.moviecatalogue.service.db.MovieHelper;
import com.learnque.my.moviecatalogue.service.entity.FavoriteMovie;
import com.learnque.my.moviecatalogue.view.adapter.FavoriteMovieAdapter;
import com.learnque.my.moviecatalogue.viewmodel.FavoriteMovieViewModel;

import java.util.ArrayList;

public class FavMovieFragment extends Fragment {

    private FavoriteMovieViewModel viewModel;
    private FavoriteMovieAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView notice;

    public FavMovieFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fav_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //View Model
        viewModel = ViewModelProviders.of(this).get(FavoriteMovieViewModel.class);
        viewModel.getData().observe(this, getListData);
        viewModel.setData(getActivity().getApplicationContext());

        adapter = new FavoriteMovieAdapter(this);
        adapter.notifyDataSetChanged();
        recyclerView = view.findViewById(R.id.rv_fav_movie);
        showRecyclerAdapter();

        progressBar = view.findViewById(R.id.progressBar);
        notice = view.findViewById(R.id.movie_notice);
        showLoading(true);
    }

    private void showRecyclerAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private Observer<ArrayList<FavoriteMovie>> getListData = new Observer<ArrayList<FavoriteMovie>>() {
        @Override
        public void onChanged(@Nullable ArrayList<FavoriteMovie> favoriteMovies) {
            if (favoriteMovies.size() > 0) {
                adapter.setData(favoriteMovies);
            } else {
                notice.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity().getApplicationContext(), "Data kosong!", Toast.LENGTH_SHORT).show();
            }
            showLoading(false);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == DetailFavoritActivity.REQUEST_DELETE) {
                if (resultCode == DetailFavoritActivity.RESULT_DELETE) {
                    int position = data.getIntExtra("index", 0);
                    adapter.removeItem(position);
                    Snackbar.make(getView(), getResources().getString(R.string.delete), Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
