package com.era.www.movietracker;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MoviesFragment extends Fragment {

    public MoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movies, container, false);


        TabLayout layout = (TabLayout) getActivity().findViewById(R.id.main_tab_layout);

        if (layout.getVisibility() == View.GONE) {
            layout.setVisibility(View.VISIBLE);
        }

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);

        setUpViewPager(viewPager);

        layout.setupWithViewPager(viewPager);


//        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager);

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
