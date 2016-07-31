package com.henriquenfaria.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MoviesFragment extends Fragment {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 2;
    private OnListFragmentInteractionListener mListener;
    private MoviesRecyclerViewAdapter mMoviesRecyclerViewAdapter;
    private List<Movie> mMoviesList;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MoviesFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MoviesFragment newInstance(int columnCount) {
        MoviesFragment fragment = new MoviesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMoviesList();
    }

    private void updateMoviesList() {
        FetchMoviesTask moviesTask = new FetchMoviesTask();

        //TODO: Must implement logic to get query option by user settings (SharedPreferences)
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = prefs.getString(getString(R.string.pref_sort_order_key),
                getString(R.string.pref_popular_value));

        moviesTask.execute(sortOrder);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(linearLayoutManager);
            } else {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(context, mColumnCount);
                recyclerView.setLayoutManager(gridLayoutManager);
            }

            //TODO: Do i need to remove this?
            mMoviesList = new ArrayList<Movie>();

            mMoviesRecyclerViewAdapter = new MoviesRecyclerViewAdapter(mMoviesList, mListener);
            recyclerView.setAdapter(mMoviesRecyclerViewAdapter);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Movie item);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, Movie[]> {

        private Movie[] getMoviesDataFromJson(String moviesJsonStr)
                throws JSONException {

            //These are the names of the JSON objects that need to be extracted.
            final String TMD_LIST = "results";
            final String TMD_ID = "id";
            final String TMD_TITLE = "title";
            final String TMD_POSTER_PATH = "poster_path";

            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray jsonMoviesArray = moviesJson.getJSONArray(TMD_LIST);

            Movie[] moviesArray = new Movie[jsonMoviesArray.length()];

            for (int i = 0; i < jsonMoviesArray.length(); i++) {

                String id = jsonMoviesArray.getJSONObject(i).getString(TMD_ID);
                String title = jsonMoviesArray.getJSONObject(i).getString(TMD_TITLE);
                Uri posterUri = createPosterUri(jsonMoviesArray.getJSONObject(i).getString(TMD_POSTER_PATH));

                moviesArray[i] = new Movie(id, title, posterUri);
            }
            return moviesArray;
        }

        private Uri createMoviesUri(String sortOrder) {

            final String POPULAR_MOVIES_BASE_URL =
                    "https://api.themoviedb.org/3/movie/popular?";
            final String TOP_RATED_MOVIES_BASE_URL =
                    "https://api.themoviedb.org/3/movie/top_rated?";
            final String POSTER_MOVIES_BASE_URL = "http://image.tmdb.org/t/p/";
            final String POSTER_SIZE = "w185/";
            final String API_KEY_PARAM = "api_key";

            Uri builtUri = null;

            if (sortOrder.equals(getString(R.string.pref_popular_value))) {
                builtUri = Uri.parse(POPULAR_MOVIES_BASE_URL);
            } else if (sortOrder.equals(getString(R.string.pref_top_rated_value))) {
                builtUri = Uri.parse(TOP_RATED_MOVIES_BASE_URL);
            } else {
                builtUri = Uri.parse(POPULAR_MOVIES_BASE_URL);

            }

            return builtUri.buildUpon()
                    .appendQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_MAP_API_KEY)
                    .build();
        }

        private Uri createPosterUri(String posterPath) {

            final String POSTER_MOVIES_BASE_URL = "http://image.tmdb.org/t/p/";
            final String POSTER_SIZE = "w185/";

            Uri builtUri = Uri.parse(POSTER_MOVIES_BASE_URL).buildUpon().appendEncodedPath(POSTER_SIZE).appendEncodedPath(posterPath).build();
            return builtUri;
        }


        @Override
        protected Movie[] doInBackground(String... params) {

            // If there's no zip code, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;

            String format = "json";
            String units = "metric";
            int numDays = 7;

            try {
                Uri moviesUri = createMoviesUri(params[0]);
                URL url = new URL(moviesUri.toString());

                // Create the request to The Movide DB, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMoviesDataFromJson(moviesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the movies.
            return null;
        }

        @Override
        protected void onPostExecute(Movie[] result) {
            if (result != null) {
                mMoviesRecyclerViewAdapter.clearRecyclerViewData();
                for (Movie movieObj : result) {
                    mMoviesList.add(movieObj);

                }
                //mMoviesRecyclerViewAdapter.notifyItemInserted(mMoviesList.size()-1);
                mMoviesRecyclerViewAdapter.notifyItemRangeInserted(0, result.length);

            }
        }
    }
}
