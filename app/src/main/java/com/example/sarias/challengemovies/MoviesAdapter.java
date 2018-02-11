package com.example.sarias.challengemovies;

/**
 * Created by Sarias on 10/02/2018.
 */

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.bumptech.glide.Glide;

import com.example.sarias.challengemovies.databinding.LvPelisRowBinding;

import java.util.List;

public class MoviesAdapter extends ArrayAdapter<Movie> {

    public MoviesAdapter(Context context, int resource, List<Movie> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtenim l'objecte en la possició corresponent
        Movie movie = getItem(position);
        Log.w("XXXX", movie.toString());

        LvPelisRowBinding binding = null;

        // Mirem a veure si la View s'està reusant, si no es així "inflem" la View
        // https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView#row-view-recycling
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            binding = DataBindingUtil.inflate(inflater, R.layout.lv_pelis_row, parent, false);
        } else {
            binding = DataBindingUtil.getBinding(convertView);
        }

        // Fiquem les dades dels objectes (provinents del JSON) en el layout
        String[] parts = movie.getReleaseDate().split("-");
        binding.tvTitle.setText(movie.getTitle() + " ( " + parts[0] + " )");
        Glide.with(getContext()).load(
                "https://image.tmdb.org/t/p/w500/" + movie.getPosterPath()
        ).into(binding.ivPosterImage);

        // Retornem la View replena per a mostrarla
        return binding.getRoot();
    }

}