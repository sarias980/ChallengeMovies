package com.example.sarias.challengemovies;

/**
 * Created by Sarias on 10/02/2018.
 */

import android.app.Application;
import android.app.ProgressDialog;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MoviesViewModel extends AndroidViewModel {
    private final Application app;
    private final AppDatabase appDatabase;
    private final MovieDao movieDao;
    private MutableLiveData<Boolean> loading;
    private int page = 1;

    public void setPage(int page) {
        this.page = page;
    }

    public MoviesViewModel(Application application) {
        super(application);

        this.app = application;
        this.appDatabase = AppDatabase.getDatabase(this.getApplication());
        this.movieDao = appDatabase.getMovieDao();
    }

    public LiveData<List<Movie>> getMovies() {
        return movieDao.getMovies();
    }

    public void reload() {
        // do async operation to fetch users
        RefreshDataTask task = new RefreshDataTask();
        task.execute();
    }

    public MutableLiveData<Boolean> getLoading() {
        if(loading == null){
            loading = new MutableLiveData<>();
        }
        return loading;
    }

    private class RefreshDataTask extends AsyncTask<Void, Void, ArrayList<Movie>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setValue(true);
        }

        @Override
        protected ArrayList<Movie> doInBackground(Void... voids) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(
                    app.getApplicationContext()
            );
            String pais = preferences.getString("pais", "es");

            MovieAPI api = new MovieAPI();
            ArrayList<Movie> result;

            result = api.getPeliculesMesVistes(pais, 1);

            Log.d("DEBUG", result != null ? result.toString() : null);

            movieDao.deleteMovies();
            movieDao.addMovies(result);

            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            loading.setValue(false);
        }

    }

    public void more() {
       MoreDataTask task = new MoreDataTask();
       task.execute();
    }

    private class MoreDataTask extends AsyncTask<Void, Void, ArrayList<Movie>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setValue(true);
        }

        @Override
        protected ArrayList<Movie> doInBackground(Void... voids) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(
                    app.getApplicationContext()
            );
            String pais = preferences.getString("pais", "es");

            MovieAPI api = new MovieAPI();
            ArrayList<Movie> result;

            result = api.getPeliculesMesVistes(pais, page);

            Log.d("DEBUG", result != null ? result.toString() : null);

            movieDao.addMovies(result);

            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            loading.setValue(false);
        }

    }
}