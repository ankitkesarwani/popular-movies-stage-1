package com.henriquenfaria.popularmovies;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements MoviesFragment.OnListFragmentInteractionListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            return;
        }

        MoviesFragment movieFragment;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            movieFragment = MoviesFragment.newInstance(Constants.PORTRAIT_COLUMN_COUNT);
        } else {
            movieFragment = MoviesFragment.newInstance(Constants.LANDSCAPE_COLUMN_COUNT);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.movie_fragment_container, movieFragment).commit();
    }

    @Override
    public void onListFragmentInteraction(Movie movieItem) {

        DetailsFragment detailFragment = DetailsFragment.newInstance(movieItem);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.movie_fragment_container, detailFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        //TODO: Temp toast
        //Toast.makeText(this, "Movie " + movieItem.getTitle() + " was clicked!", Toast.LENGTH_SHORT).show();

    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movies_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
