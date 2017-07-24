package com.era.www.movietracker.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.era.www.movietracker.R;
import com.era.www.movietracker.model.BoxOfficeMovie;

import java.text.DecimalFormat;
import java.util.List;

/**
 * {@link BoxOfficeAdapter} exposes a list of box office data to a
 * {@link android.support.v7.widget.RecyclerView}
 */
public class BoxOfficeAdapter extends RecyclerView.Adapter<BoxOfficeAdapter.BoxOfficeAdapterViewHolder> {

    private final static String LOG_TAG = BoxOfficeAdapter.class.getSimpleName();

    private List<BoxOfficeMovie> mBoxOfficeList;
    //boolean variable for show revenue check box preference.
    private boolean mShowRevenue;
    // integer variable to set the color on rank text view based on list preference value.
    private int mRankTextColor;
    // float variable to set the revenue text size based on edit text preference value.
    private float mRevenueTextSize;

    /**
     * An on-click handler that we've defined to make it easy for BoxOfficeFragment to interface with
     * our RecyclerView
     */
    private final BoxOfficeAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface BoxOfficeAdapterOnClickHandler {

        void onClick(String boxOfficeMovie);
    }

    /**
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public BoxOfficeAdapter(BoxOfficeAdapterOnClickHandler clickHandler) {

        mClickHandler = clickHandler;
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

        BoxOfficeMovie boxOfficeMovie = mBoxOfficeList.get(position);

        holder.mMovieTitleTextView.setText(boxOfficeMovie.getName());

        int movieRevenue = boxOfficeMovie.getRevenue();
        double v = movieRevenue / 1000000.0;
        DecimalFormat numberFormat = new DecimalFormat("$##.##M");
        String formRevenue = numberFormat.format(v);
        holder.mMovieRevenueTextView.setText(formRevenue);
        boxOfficeMovie.setFormattedNumber(formRevenue);
        // check if the mShowRevenue true or false, if true set revenue text view visible
        // if false set revenue text view to invisible.
        if (mShowRevenue) {
            holder.mMovieRevenueTextView.setVisibility(View.VISIBLE);
        } else {
            holder.mMovieRevenueTextView.setVisibility(View.GONE);
        }
        //set the revenue text size from user choice from revenue_text_size edit text preference
        // which passe the size from BoxOfficeFragment throw setRevenueTextSize method.
        holder.mMovieRevenueTextView.setTextSize(mRevenueTextSize);

        String movieRank = Byte.toString(boxOfficeMovie.getRank());
        holder.mMovieRankTextView.setText(movieRank);
        //set the rank text view text color depend on the color id recourse returned from setRankTextColor.
        holder.mMovieRankTextView.setTextColor(mRankTextColor);


    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our mBoxOfficeData
     */
    @Override
    public int getItemCount() {

        if (mBoxOfficeList == null) return 0;

        return mBoxOfficeList.size();
    }

    /**
     * This method is used to set the box office data on a BoxOfficeAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new BoxOfficeAdapter to display it.
     *
     * @param boxOfficeList The new Box Office  List to be displayed.
     */
    public void setBoxOfficeData(List<BoxOfficeMovie> boxOfficeList) {
        this.mBoxOfficeList = boxOfficeList;
        notifyDataSetChanged();
    }

    /**
     * setter method to control the show revenue boolean variable.
     */
    public void setShowRevenue(boolean showRevenue) {
        this.mShowRevenue = showRevenue;
    }

    /**
     * @param context the context of the caller activity or fragment.
     * @param s       the color string value which pass in form the caller activity.
     * @return the color id recourse returns depend on the the color string value.
     */
    public int setRankTextColor(Context context, String s) {
        if (s.equals(context.getString(R.string.pref_color_black_value))) {
            return mRankTextColor = ContextCompat.getColor(context, R.color.rank_text_color_black);
        } else if (s.equals(context.getString(R.string.pref_color_red_value))) {
            return mRankTextColor = ContextCompat.getColor(context, R.color.rank_text_color_red);
        } else if (s.equals(context.getString(R.string.pref_color_green_value))) {
            return mRankTextColor = ContextCompat.getColor(context, R.color.rank_text_color_green);
        }
        return mRankTextColor;
    }

    // setter method to set the revenue text size from the BoxOfficeFragment
    // depend on revenue_text_size edit text preference.
    public void setRevenueTextSize(float revenueTextSize) {
        this.mRevenueTextSize = revenueTextSize;
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

            BoxOfficeMovie boxOfficeMovie = mBoxOfficeList.get(indexPosition);

            String movie = boxOfficeMovie.getName() + " - " + boxOfficeMovie.getFormattedNumber();

            mClickHandler.onClick(movie);
        }
    }
}
