package com.example.sarias.challengemovies;

import android.app.ProgressDialog;
import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.sarias.challengemovies.databinding.FragmentDetailBinding;


public class DetailActivityFragment extends LifecycleFragment {

    private View view;

    private FragmentDetailBinding binding;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(inflater);
        view = binding.getRoot();

        Intent i = getActivity().getIntent();

        if (i != null) {
            Movie movie = (Movie) i.getSerializableExtra("movie");

            if (movie != null) {
                updateUi(movie);
            }
        }

        SharedViewModel sharedModel = ViewModelProviders.of(
                getActivity()
        ).get(SharedViewModel.class);
        sharedModel.getSelected().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                updateUi(movie);
            }
        });


        return view;
    }

    private void updateUi(Movie movie) {
        Log.d("MOVIE", movie.toString());

        binding.tvTitle.setText(movie.getTitle());
        String[] parts = movie.getReleaseDate().split("-");
        binding.tvReleaseDate.setText(parts[0]);
        binding.tvSynopsis.setText(Html.fromHtml("<b>Synopsis:</b> " + movie.getOverview()));
        Glide.with(getContext()).load(
                "https://image.tmdb.org/t/p/w500/" + movie.getPosterPath()
        ).into(binding.ivPosterImage);
    }
}

