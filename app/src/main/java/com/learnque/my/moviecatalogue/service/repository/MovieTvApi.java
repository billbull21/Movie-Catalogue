package com.learnque.my.moviecatalogue.service.repository;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.learnque.my.moviecatalogue.service.model.MovieTv;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieTvApi {

//    private static MovieTvApi movieTvApi;
//
//    public static MovieTvApi getInstance() {
//        if (movieTvApi == null) {
//            movieTvApi = new MovieTvApi();
//        }
//        return movieTvApi;
//    }

    public static ArrayList<MovieTv> getApiData(String type, String API_KEY) {
        final MutableLiveData<ArrayList<MovieTv>> mutableLiveData = new MutableLiveData<>();
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<MovieTv> listData = new ArrayList<>();
        final String category = type;

        String url = "https://api.themoviedb.org/3/discover/"+category+"?api_key="+API_KEY+"&language=en-US";
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject resps = new JSONObject(result);
                    JSONArray list = resps.getJSONArray("results");
                    for (int i=0; i<list.length(); i++) {
                        JSONObject data = list.getJSONObject(i);
                        MovieTv model = new MovieTv();
                        model.setId(data.getInt("id"));
                        if (category.equals("movie"))
                            model.setTitle(data.getString("title"));
                        else if (category.equals("tv"))
                            model.setTitle(data.getString("name"));
                        model.setOverview(data.getString("overview"));
                        model.setPoster(data.getString("poster_path"));
                        model.setPopularity(String.valueOf(data.getDouble("popularity")));
                        model.setRating(String.valueOf(data.getDouble("vote_average")));
                        listData.add(model);
                    }
                } catch (Exception e) {
                    Log.d("REPOSITORY", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("REPOSITORY", "GAGALLLLLLL!!!");
            }
        });
        return listData;
    }
}
