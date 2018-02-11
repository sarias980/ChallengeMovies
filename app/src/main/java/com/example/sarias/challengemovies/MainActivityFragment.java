package com.example.sarias.challengemovies;

import android.app.ProgressDialog;
import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.sarias.challengemovies.databinding.FragmentMainBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends LifecycleFragment {

    private ArrayList<Movie> items;
    private MoviesAdapter adapter;
    private FragmentMainBinding binding;
    private SharedPreferences preferences;
    private MoviesViewModel model;
    private SharedViewModel sharedModel;
    private ProgressDialog dialog;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater);
        View view = binding.getRoot();

        items = new ArrayList<>();
        adapter = new MoviesAdapter(
                getContext(),
                R.layout.lv_pelis_row,
                items
        );

        sharedModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        binding.lvPelis.setAdapter(adapter);
        binding.lvPelis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie movie = (Movie) adapterView.getItemAtPosition(i);
                if (!esTablet()) {
                    Intent intent = new Intent(getContext(), DetailActivity.class);
                    intent.putExtra("movie", movie);
                    startActivity(intent);
                } else {
                    sharedModel.select(movie);
                }
            }
        });
        binding.lvPelis.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                loadNextDataFromApi(page);
                return true;
            }
        });
        binding.tvKey.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0){
                    Log.d("String",binding.tvKey.getText().toString());
                    model.setName(binding.tvKey.getText().toString());
                    model.reload();
                }
            }
        });


        model = ViewModelProviders.of(this).get(MoviesViewModel.class);
        model.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                adapter.clear();
                adapter.addAll(movies);
            }
        });

        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading...");

        model.getLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean mostrat) {
                if(mostrat)
                    dialog.show();
                else
                    dialog.dismiss();
            }
        });

        return view;
    }

    public void loadNextDataFromApi(int page) {
        Log.d("Page", Integer.toString(page));
        model.setPage(page+1);
        model.more();
    }

    boolean esTablet() {
        return getResources().getBoolean(R.bool.tablet);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_pelis_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            refresh();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        refresh();
    }

    private void refresh() {
        model.reload();
    }
}
