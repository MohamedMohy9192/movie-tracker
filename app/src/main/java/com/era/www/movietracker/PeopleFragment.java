package com.era.www.movietracker;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


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

        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.main_tab_layout);
        setTabLayoutCollapseMode(tabLayout);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        setToolbarCollapseMode(toolbar);

        //Set the AppBar expand to false to wrap the toolbar
        appBarLayout.setExpanded(false);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_people, container, false);
    }

    /**
     *
     * @param tabLayout reference to TabLayout widget in app_bar_main.xml file.
     * Use this method to change the TabLayout Collapse Mode to none if it's Pin
     * and it's Visibility to Gone.
     */
    private void setTabLayoutCollapseMode(TabLayout tabLayout){
        tabLayout.setVisibility(View.GONE);

        CollapsingToolbarLayout.LayoutParams params =
                (CollapsingToolbarLayout.LayoutParams) tabLayout.getLayoutParams();

        if (params.getCollapseMode() == CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN){
            params.setCollapseMode(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_OFF);
            tabLayout.setLayoutParams(params);
        }
    }

    /**
     *
     * @param toolbar reference to Toolbar widget in app_bar_main.xml file.
     * Use this method to change the Toolbar Collapse Mode to Pin if it's PARALLAX
     */
    private void setToolbarCollapseMode(Toolbar toolbar){
        CollapsingToolbarLayout.LayoutParams toolbarParams =
                (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();

        if (toolbarParams.getCollapseMode() ==
                CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX){
            toolbarParams.setCollapseMode(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN);
            toolbar.setLayoutParams(toolbarParams);
        }
    }

}
