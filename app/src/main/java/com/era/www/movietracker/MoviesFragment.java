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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.main_tab_layout);
        setTabLayoutCollapseMode(tabLayout);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        setToolbarCollapseMode(toolbar);

        //Set the AppBar expand to true to wrap the CollapsingToolbarLayout height.
        appBarLayout.setExpanded(true);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);

        setUpViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    /**
     *
     * @param tabLayout reference to TabLayout widget in app_bar_main.xml file.
     * Use this method to change the TabLayout Collapse Mode to PIN if it's none
     * and it's Visibility to Visible.
     */
    private void setTabLayoutCollapseMode(TabLayout tabLayout){
        if (tabLayout.getVisibility() == View.GONE) {
            tabLayout.setVisibility(View.VISIBLE);
        }

        CollapsingToolbarLayout.LayoutParams params =
                (CollapsingToolbarLayout.LayoutParams) tabLayout.getLayoutParams();

        if (params.getCollapseMode() ==
                CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_OFF) {
            params.setCollapseMode(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN);
            tabLayout.setLayoutParams(params);
        }
    }

    /**
     *
     * @param toolbar reference to Toolbar widget in app_bar_main.xml file.
     * Use this method to change the Toolbar Collapse Mode to PARALLAX if it's PIN
     */
    private void setToolbarCollapseMode(Toolbar toolbar){
        CollapsingToolbarLayout.LayoutParams toolbarParams =
                (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();

        if (toolbarParams.getCollapseMode() ==
                CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN) {
            toolbarParams.setCollapseMode(
                    CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX);
            toolbar.setLayoutParams(toolbarParams);
        }
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
