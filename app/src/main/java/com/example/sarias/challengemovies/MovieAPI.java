package com.example.sarias.challengemovies;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MovieAPI {
    private final String BASE_URL = "http://api.themoviedb.org/3";
    private final String API_KEY = "93aea0c77bc168d8bbce3918cefefa45";

    ArrayList<Movie> getPeliculesMesVistes(int page) {
        return doCall("movie", "popular", page, "");
    }
    ArrayList<Movie> getPeliculesByTitele(int page, String name) {
        return doCall("search", "movie", page, name);
    }

    private String getUrlPage(String recurs, String tipus, int pagina, String name) {
        Uri builtUri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath(recurs)
                .appendPath(tipus)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("query", name)
                .appendQueryParameter("page", String.valueOf(pagina))
                .build();
        Log.d("Url", builtUri.toString());
        return builtUri.toString();
    }

    private ArrayList<Movie> doCall(String recurs, String tipus, int page, String name) {
        ArrayList<Movie> movies = new ArrayList<>();

        try {
            String url = getUrlPage(recurs, tipus, page, name);
            String JsonResponse = HttpUtils.get(url);
            ArrayList<Movie> list = processJson(JsonResponse);
            movies.addAll(list);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return movies;
    }

    private ArrayList<Movie> processJson(String jsonResponse) {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            JSONObject data = new JSONObject(jsonResponse);
            JSONArray jsonMovies = data.getJSONArray("results");
            for (int i = 0; i < jsonMovies.length(); i++) {
                JSONObject jsonMovie = jsonMovies.getJSONObject(i);

                Movie movie = new Movie();
                movie.setTitle(jsonMovie.getString("title"));
                movie.setReleaseDate(jsonMovie.getString("release_date"));
                movie.setOverview(jsonMovie.getString("overview"));
                movie.setPosterPath(jsonMovie.getString("poster_path"));

                movies.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movies;
    }
}
