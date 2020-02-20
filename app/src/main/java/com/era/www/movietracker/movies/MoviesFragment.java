package com.era.www.movietracker.movies;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.era.www.movietracker.R;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MoviesFragment extends Fragment {

    private static final String LOG_TAG = MoviesFragment.class.getSimpleName();

    private ViewPagerAdapter mViewPagerAdapter;
    private ViewPager2 mViewPager2;
    private TabLayout mTabLayout;

    private final String[] mFragmentsName = {"Box Office", "Coming Soon", "Popular"};

    public MoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewPagerAdapter = new ViewPagerAdapter(this);
        mViewPager2 = view.findViewById(R.id.view_pager);

        mViewPagerAdapter.addFragment(new BoxOfficeFragment());
        mViewPagerAdapter.addFragment(new ComingSoonFragment());
        mViewPagerAdapter.addFragment(new PopularFragment());

        mViewPager2.setAdapter(mViewPagerAdapter);

        mTabLayout = view.findViewById(R.id.main_tab_layout);
        new TabLayoutMediator(mTabLayout, mViewPager2,
                (tab, position) -> tab.setText(mFragmentsName[position])).attach();
    }

    private class ViewPagerAdapter extends FragmentStateAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();

        public ViewPagerAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }


        void addFragment(Fragment fragment) {
            fragmentList.add(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return fragmentList.size();
        }
    }
}
