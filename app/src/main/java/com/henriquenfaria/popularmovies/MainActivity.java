package com.henriquenfaria.popularmovies;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MoviesFragment.OnListFragmentInteractionListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    //TODO: Move to Consants class
    private static final int COLUMN_COUNT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            return;
        }

        MoviesFragment movieFragment = MoviesFragment.newInstance(COLUMN_COUNT);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.movie_fragment_container, movieFragment).commit();
    }

    @Override
    public void onListFragmentInteraction(Movie movieItem) {

        //TODO: Temp toast
        Toast.makeText(this, "Movie " + movieItem.getTitle() + " was clicked!", Toast.LENGTH_SHORT).show();


    }


}
