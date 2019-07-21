package com.learnque.my.moviecatalogue.service.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import static android.provider.BaseColumns._ID;
import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.MovieColumns.ID_MOVIE;
import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.MovieColumns.OVERVIEW;
import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.MovieColumns.POPULARITY;
import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.MovieColumns.POSTER;
import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.MovieColumns.RATING;
import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.MovieColumns.TITLE;
import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.getColumnInt;
import static com.learnque.my.moviecatalogue.service.db.DatabaseContract.getColumnString;

public class FavoriteMovie implements Parcelable {
    private int id, movieId;
    private String title, overview, poster, rating, popularity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.overview);
        dest.writeString(this.poster);
        dest.writeString(this.rating);
        dest.writeString(this.popularity);
    }

    public FavoriteMovie() {
    }

    public FavoriteMovie(int id, int movieId, String title, String overview, String poster, String popularity, String rating) {
        this.id = id;
        this.movieId = movieId;
        this.title = title;
        this.overview = overview;
        this.poster = poster;
        this.popularity = popularity;
        this.rating = rating;
    }

    public FavoriteMovie(Cursor cursor) {
        this.id = getColumnInt(cursor, _ID);
        this.movieId = getColumnInt(cursor, ID_MOVIE);
        this.title = getColumnString(cursor, TITLE);
        this.overview = getColumnString(cursor, OVERVIEW);
        this.poster = getColumnString(cursor, POSTER);
        this.popularity = getColumnString(cursor, POPULARITY);
        this.rating = getColumnString(cursor, RATING);
    }

    protected FavoriteMovie(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.overview = in.readString();
        this.poster = in.readString();
        this.rating = in.readString();
        this.popularity = in.readString();
    }

    public static final Parcelable.Creator<FavoriteMovie> CREATOR = new Parcelable.Creator<FavoriteMovie>() {
        @Override
        public FavoriteMovie createFromParcel(Parcel source) {
            return new FavoriteMovie(source);
        }

        @Override
        public FavoriteMovie[] newArray(int size) {
            return new FavoriteMovie[size];
        }
    };
}
