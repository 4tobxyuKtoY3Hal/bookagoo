package com.itech.bookagoo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.itech.bookagoo.service.ApiService;
import com.itech.bookagoo.tool.Log;
import com.itech.bookagoo.work.Profile;
import com.viewpagerindicator.TabPageIndicator;

/**
 * Created by Artem on 02.03.14.
 */
public class ProfileFragment extends Fragment implements MainActivity.IContentFragment {

    private static final String LOG_TAG = "ProfileFragment";

    private BroadcastReceiver mQueryReceiver = null;
    private FragmentPagerAdapter mAdapter = null;
    private ViewPager mPager = null;
    private FragmentTransaction mFragmentTransaction;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        if (!App.getInstance().isTablet()) {

            mAdapter = new PagerAdapter(getActivity().getSupportFragmentManager());

            mPager = (ViewPager) view.findViewById(R.id.pager);
            mPager.setAdapter(mAdapter);

            TabPageIndicator indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
            indicator.setViewPager(mPager);
        }

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mQueryReceiver != null) {
            App.getContext().unregisterReceiver(mQueryReceiver);
            mQueryReceiver = null;
        }

    }

    @Override
    public void onStart() {
        super.onStart();


        ((MainActivity) getActivity()).visibleButLogaut();


        if (mQueryReceiver != null) {
            App.getContext().unregisterReceiver(mQueryReceiver);
            mQueryReceiver = null;
        }

        mQueryReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(LOG_TAG, "onReceive " + intent.getAction());
                ((ITabProfileContentFragment) mAdapter.getItem(mPager.getCurrentItem())).updateUi();
            }
        };
        App.getContext().registerReceiver(mQueryReceiver, new IntentFilter(ApiService.ACTION_RESULT));

        ApiService.queryGetUserData();


    }

    @Override
    public int getIdTitle() {
        return R.string.title_profile;
    }

    @Override
    public String getNameTitle() {
//        return Profile.getInstance().getFirstName();
        return App.getContext().getString(R.string.title_profile);
    }

    @Override
    public int getIdIco() {
        return R.drawable.ic_menu0;
    }

    @Override
    public int getIdIcoTop() {
        return R.drawable.ic_menu0_tap;
    }

    @Override
    public String getUrlIco() {
        return null;
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        private ITabProfileContentFragment[] mFragment = new ITabProfileContentFragment[]{
                new ProfileContentGeneralFragment(),
                new ProfileContentBabyFragment(),
                new ProfileContentFamilyFragment()
        };

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return (Fragment) mFragment[position % mFragment.length];
        }

        @Override
        public int getCount() {
            return mFragment.length;
        }

        public CharSequence getPageTitle(int position) {
            return getString(mFragment[position % mFragment.length].getIdTitle());
        }
    }

    public interface ITabProfileContentFragment {

        public void updateUi();

        public int getIdTitle();

    }


}