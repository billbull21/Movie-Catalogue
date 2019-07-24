package com.learnque.my.moviecatalogue.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.learnque.my.moviecatalogue.R;
import com.learnque.my.moviecatalogue.service.entity.FavoriteMovie;
import com.learnque.my.moviecatalogue.view.ui.DetailFavoritActivity;

import java.util.ArrayList;

import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.MovieColumns.CONTENT_URI;

public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.FavMovViewHolder> {

    private ArrayList<FavoriteMovie> data = new ArrayList<>();
    private Fragment fragment;

    public FavoriteMovieAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public ArrayList<FavoriteMovie> getData() {
        return data;
    }

    public void setData(ArrayList<FavoriteMovie> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        this.data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, data.size());
    }

    @NonNull
    @Override
    public FavMovViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_card_view, viewGroup, false);
        return new FavMovViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FavMovViewHolder holder, int i) {
        final int POSITION = i;
        holder.bind(getData().get(i));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fragment.getContext(), DetailFavoritActivity.class);
                intent.putExtra("type", "movie");
                intent.putExtra("position", POSITION);
                intent.putExtra("data", getData().get(POSITION));
                fragment.startActivityForResult(intent, DetailFavoritActivity.REQUEST_DELETE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getData().size();
    }

    public class FavMovViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView title, over;
        ImageView poster;

        public FavMovViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            title = itemView.findViewById(R.id.el_title);
            over = itemView.findViewById(R.id.el_desc);
            poster = itemView.findViewById(R.id.el_poster);
        }

        void bind(FavoriteMovie dataSet) {
            title.setText(dataSet.getTitle());
            over.setText(dataSet.getOverview());
            Glide.with(itemView.getContext())
                    .load(dataSet.getPoster())
                    .apply(new RequestOptions().override(100, 150))
                    .into(poster);
        }
    }
}
