package com.era.www.movietracker;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowFragment extends Fragment {


    public TvShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TabLayout layout = (TabLayout) getActivity().findViewById(R.id.main_tab_layout);
        layout.setVisibility(View.GONE);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_show_framgent, container, false);
    }

}
