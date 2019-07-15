package com.learnque.my.moviecatalogue.view.ui;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.learnque.my.moviecatalogue.R;
import com.learnque.my.moviecatalogue.service.model.MovieTv;
import com.learnque.my.moviecatalogue.view.adapter.MovieTvAdapter;
import com.learnque.my.moviecatalogue.viewmodel.MainViewModel;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TVShowFragment extends Fragment {

    private RecyclerView recyclerView;
    private MovieTvAdapter adapter;
    private ProgressBar progressBar;
    private MainViewModel mainViewModel;

    public TVShowFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tvshow, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //view model
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getData().observe(this, getListData);
        mainViewModel.setData("tv");

        adapter = new MovieTvAdapter(getActivity().getApplicationContext());
        adapter.notifyDataSetChanged();
        recyclerView = view.findViewById(R.id.rv_tv);
        showRecylerAdapter();

        progressBar = view.findViewById(R.id.progressBar);
        showLoading(true);
    }

    private void showRecylerAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private Observer<ArrayList<MovieTv>> getListData = new Observer<ArrayList<MovieTv>>() {
        @Override
        public void onChanged(@Nullable ArrayList<MovieTv> movieTvs) {
            if (movieTvs != null) {
                adapter.setData(movieTvs);
                showLoading(false);
            }
        }
    };

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
