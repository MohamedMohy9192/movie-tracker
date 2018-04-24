package com.era.www.movietracker.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.era.www.movietracker.R;
import com.era.www.movietracker.data.MoviesContract.BoxOfficeEntry;
import com.era.www.movietracker.movies.BoxOfficeFragment;
import com.era.www.movietracker.utilities.FormatUtils;

import java.text.DecimalFormat;

/**
 * {@link BoxOfficeAdapter} exposes a list of box office data to a
 * {@link android.support.v7.widget.RecyclerView}
 */
public class BoxOfficeAdapter extends RecyclerView.Adapter<BoxOfficeAdapter.BoxOfficeAdapterViewHolder> {

    private final static String LOG_TAG = BoxOfficeAdapter.class.getSimpleName();

    private Cursor mCursor;

    /**
     * An on-click handler that we've defined to make it easy for BoxOfficeFragment to interface with
     * our RecyclerView
     */
    private final BoxOfficeAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface BoxOfficeAdapterOnClickHandler {

        void onClick(int movieTraktId);
    }

    /**
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public BoxOfficeAdapter(BoxOfficeAdapterOnClickHandler clickHandler) {

        this.mClickHandler = clickHandler;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param parent   The ViewGroup that these ViewHolders are contained within.
     * @param viewType If your RecyclerView has more than one type of item  you
     *                 can use this viewType integer to provide a different layout. See
     *                 {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                 for more details.
     * @return A new BoxOfficeAdapterViewHolder that holds the View for each list item
     */
    @Override
    public BoxOfficeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.box_office_movie_list_item, parent, false);
        BoxOfficeAdapterViewHolder viewHolder = new BoxOfficeAdapterViewHolder(view);

        return viewHolder;
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the Box office movie
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder   The ViewHolder which should be updated to represent the
     *                 contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(BoxOfficeAdapterViewHolder holder, int position) {

        mCursor.moveToPosition(position);

        holder.mMovieRevenueTextView.setText(
                FormatUtils.formatMovieRevenue(mCursor.getInt(BoxOfficeFragment.INDEX_MOVIE_REVENUE)));

        holder.mMovieTitleTextView.setText(
                mCursor.getString(BoxOfficeFragment.INDEX_MOVIE_TITLE));

        holder.mMovieRankTextView.setText(
                String.valueOf(mCursor.getInt(BoxOfficeFragment.INDEX_MOVIE_RANK)));
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our mBoxOfficeData
     */
    @Override
    public int getItemCount() {

        if (mCursor == null) return 0;

        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        // After the new Cursor is set, call notifyDataSetChanged
        notifyDataSetChanged();
    }

    /**
     * Cache of the children views for a BoxOffice list item.
     */
    public class BoxOfficeAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public final TextView mMovieTitleTextView;

        public final TextView mMovieRevenueTextView;

        public final TextView mMovieRankTextView;

        /**
         * This gets called by the child views during a click.
         *
         * @param itemView The View that was clicked
         */
        public BoxOfficeAdapterViewHolder(View itemView) {
            super(itemView);

            mMovieTitleTextView = (TextView) itemView.findViewById(R.id.tv_movie_title);

            mMovieRevenueTextView = (TextView) itemView.findViewById(R.id.tv_movie_revenue);

            mMovieRankTextView = (TextView) itemView.findViewById(R.id.tv_movie_rank);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int indexPosition = getAdapterPosition();
            mCursor.moveToPosition(indexPosition);
            mClickHandler.onClick(mCursor.getInt(BoxOfficeFragment.INDEX_MOVIE_TRAKT_ID));
        }
    }
}
