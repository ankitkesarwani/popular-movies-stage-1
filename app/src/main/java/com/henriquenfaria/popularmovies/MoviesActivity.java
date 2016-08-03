package com.henriquenfaria.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MoviesActivity extends AppCompatActivity implements MoviesFragment.OnMoviesListInteractionListener, NoInternetFragment.OnRetryInteractionListener {

    private static final String LOG_TAG = MoviesActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (savedInstanceState == null) {
            if (isInternetConnected()) {
                MoviesFragment moviesFragment = getMoviesFragment(getResources().getConfiguration());
                fragmentTransaction.add(R.id.movies_fragment_container, moviesFragment).commit();
            } else {
                NoInternetFragment noInternetFragment = NoInternetFragment.newInstance();
                fragmentTransaction.add(R.id.movies_fragment_container, noInternetFragment).commit();
            }
        } else {
            Fragment currentFragment = fragmentManager.findFragmentById(R.id.movies_fragment_container);
            if (currentFragment instanceof MoviesFragment && !isInternetConnected()) {
                NoInternetFragment noInternetFragment = NoInternetFragment.newInstance();
                fragmentTransaction.replace(R.id.movies_fragment_container, noInternetFragment).commit();
            } else if (currentFragment instanceof NoInternetFragment && isInternetConnected()) {
                MoviesFragment moviesFragment = getMoviesFragment(getResources().getConfiguration());
                fragmentTransaction.replace(R.id.movies_fragment_container, moviesFragment).commit();
            }
        }
    }

    private MoviesFragment getMoviesFragment(Configuration config) {
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            return MoviesFragment.newInstance(Constants.PORTRAIT_COLUMN_COUNT);
        } else {
            return MoviesFragment.newInstance(Constants.LANDSCAPE_COLUMN_COUNT);
        }
    }


    @Override
    public void onMoviesListInteraction(Movie movieItem) {
        Intent intent = new Intent(this, DetailsActivity.class).putExtra(Constants.EXTRA_MOVIE, movieItem);
        startActivity(intent);
    }

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
    }


    /*
    * Method to check if internet connection is available or not.
    * Method from http://stackoverflow.com/questions/16481334/check-network-connection-in-fragment
     */
    public boolean isInternetConnected() {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.movies_fragment_container);
        if (currentFragment instanceof MoviesFragment && !isInternetConnected()) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            NoInternetFragment noInternetFragment = NoInternetFragment.newInstance();
            fragmentTransaction.replace(R.id.movies_fragment_container, noInternetFragment).commit();
        }
    }

    @Override
    public void onRetryInteraction() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.movies_fragment_container);
        if (currentFragment instanceof NoInternetFragment && isInternetConnected()) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            MoviesFragment moviesFragment = getMoviesFragment(getResources().getConfiguration());
            fragmentTransaction.replace(R.id.movies_fragment_container, moviesFragment).commit();
        } else if (!isInternetConnected()) {
            Toast.makeText(this, R.string.toast_no_internet_connection, Toast.LENGTH_SHORT).show();
        }
    }
}
