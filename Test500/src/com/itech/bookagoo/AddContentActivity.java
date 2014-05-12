package com.itech.bookagoo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.viewpagerindicator.TabPageIndicator;

/**
 * Created by Artem on 05.04.14.
 */
public class AddContentActivity extends SherlockFragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_content);

        FragmentPagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());

        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);

        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);
                indicator.setViewPager(pager);

    }


    private class PagerAdapter extends FragmentPagerAdapter {

        private ITabContentFragment[] mFragment = new ITabContentFragment[]{
                new AddContentMediaFragment(),
                new AddContentSoundFragment(),
                new AddContentTextFragment()
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
