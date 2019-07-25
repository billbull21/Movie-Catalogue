package com.learnque.my.moviecatalogue.view.ui;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.support.v7.widget.SearchView;

import com.learnque.my.moviecatalogue.R;
import com.learnque.my.moviecatalogue.service.model.MovieTv;
import com.learnque.my.moviecatalogue.view.adapter.MovieTvAdapter;
import com.learnque.my.moviecatalogue.viewmodel.MainViewModel;

import java.util.ArrayList;

public class MovieFragment extends Fragment {

    private RecyclerView recyclerView;
    private MovieTvAdapter adapter;
    private ProgressBar progressBar;
    private MainViewModel mainViewModel;
    private SearchView searchView;

    private String filterString = null;
    private boolean inLoading = false;

    private static final String KEYWORD = "";

    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //view model
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getData().observe(this, getListData);

        if (savedInstanceState != null) {
            filterString = savedInstanceState.getString(KEYWORD);
        } else {
            mainViewModel.setData("movie", null);
        }

        adapter = new MovieTvAdapter(this, "movie");
        adapter.notifyDataSetChanged();
        recyclerView = view.findViewById(R.id.rv_movie);
        showRecylerAdapter();

        progressBar = view.findViewById(R.id.progressBar);
        showLoading(true);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (getUserVisibleHint()) {
            if (searchView.getQuery() != null) {
                filterString = searchView.getQuery().toString();
                outState.putString(KEYWORD, filterString);
            }
        }
        super.onSaveInstanceState(outState);
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
            } else {
                Log.d("GAGALL!!!", "KOSONG!!!");
            }
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        searchView = (SearchView) menuItem.getActionView();
        if (filterString != null && !filterString.trim().isEmpty()) {
            menuItem.expandActionView();
            searchView.setQuery(filterString, true);
        }

        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                filterString = null;
                return true;
            }
        });

        search(searchView);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void search(final SearchView searchView) {
        searchView.setQueryHint("Find some film...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    //view model
                    mainViewModel.getData().removeObserver(getListData);
                    mainViewModel.setData("movie", query);
                    mainViewModel.getData().observe(getActivity(), getListData);
                    inLoading = true; //secara default nilai nya false, jadi setelah selesai maka akan menjadi false
                    if (inLoading) {
                        adapter.clearData();
                        showLoading(true);
                    }
                } else if (query.trim().isEmpty()) {
                    mainViewModel.setData("movie" , null);
                    showLoading(true);
                    inLoading = true; //secara default nilai nya false, jadi setelah selesai maka akan menjadi false
                    if (inLoading) {
                        adapter.clearData();
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().isEmpty()) {
                    mainViewModel.setData("movie", null);
                    showLoading(true);
                    inLoading = true; //secara default nilai nya false, jadi setelah selesai maka akan menjadi false
                    if (inLoading) {
                        adapter.clearData();
                    }
                }
                return true;
            }
        });
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
