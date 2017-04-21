package com.era.www.movietracker;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * {@link BoxOfficeAdapter} exposes a list of box office data to a
 * {@link android.support.v7.widget.RecyclerView}
 */
public class BoxOfficeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static String LOG_TAG = BoxOfficeAdapter.class.getSimpleName();

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private String[] mBoxOfficeData;

    private String mHeaderString;

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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case TYPE_HEADER:
                View headerView =
                        layoutInflater.inflate(R.layout.box_office_header_list_item, parent, false);
                viewHolder = new BoxOfficeHeaderViewHolder(headerView);
                break;
            case TYPE_ITEM:
                View itemView =
                        layoutInflater.inflate(R.layout.box_office_movie_list_item, parent, false);
                viewHolder = new BoxOfficeAdapterViewHolder(itemView);
                break;

        }
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {

            case TYPE_HEADER:
                BoxOfficeHeaderViewHolder headerViewHolder = (BoxOfficeHeaderViewHolder) holder;
                headerViewHolder.mHeaderTextView.setText(mHeaderString);
                break;

            case TYPE_ITEM:
                BoxOfficeAdapterViewHolder boxOfficeAdapterViewHolder = (BoxOfficeAdapterViewHolder) holder;
                boxOfficeAdapterViewHolder.mBoxOfficeTextView.setText(mBoxOfficeData[position - 1]);
                break;
        }

    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our mBoxOfficeData
     */
    @Override
    public int getItemCount() {

        if (mBoxOfficeData == null) return 0;

        return mBoxOfficeData.length + 1;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return TYPE_HEADER;
        }

        return TYPE_ITEM;
    }

    /**
     * This method is used to set the box office data on a BoxOfficeAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new BoxOfficeAdapter to display it.
     *
     * @param boxOfficeData The new weather data to be displayed.
     */
    public void setBoxOfficeData(String[] boxOfficeData) {
        this.mBoxOfficeData = boxOfficeData;
        notifyDataSetChanged();
    }

    public void setHeaderString(String headerString) {
        this.mHeaderString = headerString;
        notifyDataSetChanged();
    }

    /**
     * Cache of the children views for a BoxOffice list item.
     */
    public class BoxOfficeAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public final TextView mBoxOfficeTextView;

        /**
         * This gets called by the child views during a click.
         *
         * @param itemView The View that was clicked
         */
        public BoxOfficeAdapterViewHolder(View itemView) {
            super(itemView);

            mBoxOfficeTextView = (TextView) itemView.findViewById(R.id.tv_box_office_data);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int indexPosition = getAdapterPosition();

            String boxOfficeMovie = mBoxOfficeData[indexPosition - 1];

            Log.i(LOG_TAG, "" + indexPosition);

            mClickHandler.onClick(boxOfficeMovie);
        }
    }

    public class BoxOfficeHeaderViewHolder extends RecyclerView.ViewHolder {

        public TextView mHeaderTextView;

        public BoxOfficeHeaderViewHolder(View itemView) {
            super(itemView);

            mHeaderTextView = (TextView) itemView.findViewById(R.id.tv_header);
        }
    }

}
