package com.itech.bookagoo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.viewpagerindicator.TabPageIndicator;

/**
 * Created by Artem on 02.03.14.
 */
public class ProfileFragment extends Fragment implements MainActivity.IContentFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        FragmentPagerAdapter adapter = new PagerAdapter(getActivity().getSupportFragmentManager());

                ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
                pager.setAdapter(adapter);

                TabPageIndicator indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
                        indicator.setViewPager(pager);

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        ((MainActivity) getActivity()).visibleButLogaut();
    }

    @Override
    public int getIdTitle() {
        return R.string.title_profile;
    }

    @Override
    public String getNameTitle() {
        return "Arsen Pogosian";
    }

    @Override
    public int getIdIco() {
        return 0;
    }

    @Override
    public int getIdIcoTop() {
        return 0;
    }

//    @Override
//    public int getIdIcoBar() {
//        return 0;
//    }

    @Override
    public String getUrlIco() {
        return "http://yastatic.net/www/1.859/yaru/i/logo.png";
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        private ITabContentFragment[] mFragment = new ITabContentFragment[]{
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



}