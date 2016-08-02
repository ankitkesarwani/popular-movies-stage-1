package com.henriquenfaria.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DetailsFragment extends Fragment {

    private static final String ARG_MOVIE = "arg_movie";
    private Movie mMovie;

    public DetailsFragment() {
        // Required empty public constructor
    }

    public static DetailsFragment newInstance(Movie movieSelected) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_MOVIE, movieSelected);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMovie = getArguments().getParcelable(ARG_MOVIE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details, container, false);

        if (mMovie != null) {
            //TODO: Temporary code just for testing
            TextView text1 = (TextView) view.findViewById(R.id.text_1);
            text1.setText(mMovie.getTitle());
            TextView text2 = (TextView) view.findViewById(R.id.text_2);
            text2.setText(mMovie.getPosterUri().toString());
        }

        return view;
    }
}
