package com.era.www.movietracker;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class MoviesFragment extends Fragment {

    private static final String LOG_TAG = MoviesFragment.class.getSimpleName();

    public MoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.app_bar);

        TabLayout layout = (TabLayout) getActivity().findViewById(R.id.main_tab_layout);

        if (layout.getVisibility() == View.GONE) {
            layout.setVisibility(View.VISIBLE);
        }
        CollapsingToolbarLayout.LayoutParams params = (CollapsingToolbarLayout.LayoutParams) layout.getLayoutParams();

        if (params.getCollapseMode() == CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_OFF) {

            params.setCollapseMode(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN);
            layout.setLayoutParams(params);
        }

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        CollapsingToolbarLayout.LayoutParams toolbarParams = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();

        Log.i(LOG_TAG, "toolbarParams before " + toolbarParams.getCollapseMode());

        if (toolbarParams.getCollapseMode() == CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN) {

            toolbarParams.setCollapseMode(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX);
            toolbar.setLayoutParams(toolbarParams);
            Log.i(LOG_TAG, "toolbarParams after " + toolbarParams.getCollapseMode());
        }


        appBarLayout.setExpanded(true);


        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);

        setUpViewPager(viewPager);

        layout.setupWithViewPager(viewPager);


        return view;
    }

    private void setUpViewPager(ViewPager viewPager) {
        //getChildFragmentManager
        //Return a private FragmentManager for placing and managing Fragments inside of this Fragment.
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(new BoxOfficeFragment(), "Box Office");
        viewPagerAdapter.addFragment(new ComingSoonFragment(), "Coming Soon");
        viewPagerAdapter.addFragment(new PopularFragment(), "Popular");
        viewPager.setAdapter(viewPagerAdapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitlesList = new ArrayList<>();


        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitlesList.get(position);
        }

        void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitlesList.add(title);
        }
    }

}
