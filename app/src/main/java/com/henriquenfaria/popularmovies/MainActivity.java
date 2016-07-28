package com.henriquenfaria.popularmovies;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MoviesFragment.OnListFragmentInteractionListener {

    //TODO: Move to Consants class

    private static final int COLUMN_COUNT  = 2;

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

        //TODO: Temp code
        Toast.makeText(this, movieItem.getName() + " was clicked!", Toast.LENGTH_SHORT).show();


    }
}
