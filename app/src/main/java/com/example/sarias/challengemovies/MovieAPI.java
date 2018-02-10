package com.example.sarias.challengemovies;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MovieAPI {
    private final String BASE_URL = "http://api.themoviedb.org/4";
    private final String API_KEY = "93aea0c77bc168d8bbce3918cefefa45";
    private final int PAGES = 5;

    ArrayList<Movie> getPeliculesMesVistes(String pais) {
        return doCall("discover", "movie", pais);
    }
    
    private String getUrlPage(String pais, String recurs, String tipus, int pagina) {
        Uri builtUri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath(recurs)
                .appendPath(tipus)
                .appendQueryParameter("region", pais)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("page", String.valueOf(pagina))
                .build();
        return builtUri.toString();
    }

    private ArrayList<Movie> doCall(String recurs, String tipus, String pais) {
        ArrayList<Movie> movies = new ArrayList<>();

        for (int i = 0; i < PAGES; i++) {
            try {
                String url = getUrlPage(pais, recurs, tipus, i);
                String JsonResponse = HttpUtils.get(url);
                ArrayList<Movie> list = processJson(JsonResponse);
                movies.addAll(list);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
