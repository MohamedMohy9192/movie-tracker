package com.era.www.movietracker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * {@link BoxOfficeAdapter} exposes a list of box office data to a
 * {@link android.support.v7.widget.RecyclerView}
 */
public class BoxOfficeAdapter extends RecyclerView.Adapter<BoxOfficeAdapter.BoxOfficeAdapterViewHolder> {

    private String[] mBoxOfficeData;

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
     * @param parent The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item  you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new BoxOfficeAdapterViewHolder that holds the View for each list item
     */
    @Override
    public BoxOfficeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.movie_list_item, parent, false);

        return new BoxOfficeAdapterViewHolder(view);

    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the Box office movie
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(BoxOfficeAdapterViewHolder holder, int position) {

        holder.mBoxOfficeTextView.setText(mBoxOfficeData[position]);
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

        return mBoxOfficeData.length;
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

            String boxOfficeMovie = mBoxOfficeData[indexPosition];

            mClickHandler.onClick(boxOfficeMovie);
        }
    }

}
