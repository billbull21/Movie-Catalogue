package com.learnque.my.moviecatalogue.view.ui;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.database.Cursor;
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
import com.learnque.my.moviecatalogue.service.entity.FavoriteTv;
import com.learnque.my.moviecatalogue.service.helper.TvMappingHelper;
import com.learnque.my.moviecatalogue.view.adapter.FavoriteTvAdapter;
import com.learnque.my.moviecatalogue.viewmodel.FavoriteTvViewModel;

import java.util.ArrayList;

public class FavTvFragment extends Fragment {

    private FavoriteTvViewModel viewModel;
    private FavoriteTvAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView notice;

    public FavTvFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fav_tv, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //View Model
        viewModel = ViewModelProviders.of(this).get(FavoriteTvViewModel.class);
        viewModel.getData().observe(this, getListData);
        viewModel.setData(getActivity().getApplicationContext());

        adapter = new FavoriteTvAdapter(this);
        adapter.notifyDataSetChanged();
        recyclerView = view.findViewById(R.id.rv_fav_tv);
        showRecyclerAdapter();

        progressBar = view.findViewById(R.id.progressBar);
        notice = view.findViewById(R.id.tv_notice);
        showLoading(true);
    }

    private void showRecyclerAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private Observer<ArrayList<FavoriteTv>> getListData = new Observer<ArrayList<FavoriteTv>>() {
        @Override
        public void onChanged(@Nullable ArrayList<FavoriteTv> favoriteTvs) {
            if (favoriteTvs.size() > 0) {
                adapter.setData(favoriteTvs);
            } else {
                notice.setVisibility(View.VISIBLE);
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
