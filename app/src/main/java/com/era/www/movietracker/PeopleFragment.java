package com.era.www.movietracker;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleFragment extends Fragment {


    private static final String LOG_TAG = PeopleFragment.class.getSimpleName();

    public PeopleFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.app_bar);

        TabLayout layout = (TabLayout) getActivity().findViewById(R.id.main_tab_layout);
        layout.setVisibility(View.GONE);

        CollapsingToolbarLayout.LayoutParams params = (CollapsingToolbarLayout.LayoutParams) layout.getLayoutParams();

        if (params.getCollapseMode() == CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN){

            params.setCollapseMode(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_OFF);
            layout.setLayoutParams(params);
        }

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        CollapsingToolbarLayout.LayoutParams toolbarParams = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();

        if (toolbarParams.getCollapseMode() == CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX){

            toolbarParams.setCollapseMode(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN);
            toolbar.setLayoutParams(toolbarParams);
        }



        appBarLayout.setExpanded(false);


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_people, container, false);



    }

}
