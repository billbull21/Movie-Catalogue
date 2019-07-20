package com.learnque.my.moviecatalogue.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.learnque.my.moviecatalogue.R;
import com.learnque.my.moviecatalogue.service.model.MovieTv;
import com.learnque.my.moviecatalogue.view.ui.DetailActivity;

import java.util.ArrayList;

public class MovieTvAdapter extends RecyclerView.Adapter<MovieTvAdapter.MovieTvViewHolder> {

    private Context context;
    private ArrayList<MovieTv> data = new ArrayList<>();
    private String type;

    public MovieTvAdapter(Context context, String type) {
        this.context = context;
        this.type = type;
    }

    public ArrayList<MovieTv> getData() {
        return data;
    }

    public void clearData() {
        this.data.clear();
        notifyDataSetChanged();
    }

    public void setData(ArrayList<MovieTv> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieTvViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_card_view, viewGroup, false);
        return new MovieTvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieTvViewHolder holder, int i) {
        final int POSITION = i;
        holder.bind(getData().get(i));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("data", getData().get(POSITION));
                intent.putExtra("type", type);
                intent.putExtra("index", POSITION);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getData().size();
    }

    class MovieTvViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView title, overview;
        ImageView poster;

        MovieTvViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            title = itemView.findViewById(R.id.el_title);
            overview = itemView.findViewById(R.id.el_desc);
            poster = itemView.findViewById(R.id.el_poster);
        }

        void bind(MovieTv movieTv){
            title.setText(movieTv.getTitle());
            overview.setText(movieTv.getOverview());

            Glide.with(itemView.getContext())
                    .load(movieTv.getPoster())
                    .apply(new RequestOptions().override(100,150))
                    .into(poster);
        }
    }
}
