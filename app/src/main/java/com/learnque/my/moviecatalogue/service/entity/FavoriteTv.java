package com.learnque.my.moviecatalogue.service.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class FavoriteTv implements Parcelable {
    private int id, tvId;
    private String title, overview, poster, rating, popularity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTvId() {
        return tvId;
    }

    public void setTvId(int tvId) {
        this.tvId = tvId;
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

    public FavoriteTv() {
    }

    protected FavoriteTv(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.overview = in.readString();
        this.poster = in.readString();
        this.rating = in.readString();
        this.popularity = in.readString();
    }

    public static final Parcelable.Creator<FavoriteTv> CREATOR = new Parcelable.Creator<FavoriteTv>() {
        @Override
        public FavoriteTv createFromParcel(Parcel source) {
            return new FavoriteTv(source);
        }

        @Override
        public FavoriteTv[] newArray(int size) {
            return new FavoriteTv[size];
        }
    };
}
