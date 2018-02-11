package com.example.sarias.challengemovies;

/**
 * Created by Sarias on 10/02/2018.
 */

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("select * from movie")
    LiveData<List<Movie>> getMovies();

    @Insert
    void addMovie(Movie movie);

    @Insert
    void addMovies(List<Movie> movies);

    @Delete
    void deleteMovie(Movie movie);

    @Query("DELETE FROM movie")
    void deleteMovies();
}

