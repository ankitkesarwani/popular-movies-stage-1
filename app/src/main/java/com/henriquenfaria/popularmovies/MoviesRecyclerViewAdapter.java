package com.henriquenfaria.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Movie} and makes a call to the
 * specified {@link MoviesFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MoviesRecyclerViewAdapter extends RecyclerView.Adapter<MoviesRecyclerViewAdapter.ViewHolder> {

    private static final String LOG_TAG = MoviesRecyclerViewAdapter.class.getSimpleName();

    private final List<Movie> mMoviesList;
    private final MoviesFragment.OnListFragmentInteractionListener mListener;

    public MoviesRecyclerViewAdapter(List<Movie> moviesList, MoviesFragment.OnListFragmentInteractionListener listener) {
        mMoviesList = moviesList;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


        holder.mItem = mMoviesList.get(position);

        // Glide.clear(holder.mPosterView);

        Glide.with(holder.mPosterView.getContext()).load(holder.mItem.getPosterUri()).dontTransform().into(holder.mPosterView);
        holder.mTitle.setText(holder.mItem.getTitle());


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mMoviesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public Movie mItem;

        public final View mView;
        public final ImageView mPosterView;
        public final TextView mTitle;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPosterView = (ImageView) view.findViewById(R.id.poster);
            mTitle = (TextView) view.findViewById(R.id.title);
        }

        @Override
        public String toString() {
            //TODO: Temp code
            return super.toString() /*+ " '" + mMovieIdView.getText() + "'"*/;
        }
    }


    @Override
    public void onViewRecycled(ViewHolder holder) {
        Glide.clear(holder.mPosterView);
    }


    // Method implementation based on http://stackoverflow.com/questions/29978695/remove-all-items-from-recyclerview
    // It resets the list and notifies the adapter
    public void clearRecyclerViewData() {
        int size = mMoviesList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                mMoviesList.remove(0);
            }
            notifyItemRangeRemoved(0, size);
        }
    }
}
