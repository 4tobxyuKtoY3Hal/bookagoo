package com.itech.bookagoo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Artem on 10.06.14.
 */

public class StartTutorialActivity extends BaseFragmentActivity implements View.OnClickListener {

    private int mN = 0;
    private View[] mArrWindow;
    private View mBtSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_tutorial);

        getSupportActionBar().hide();

        FragmentPagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager) findViewById(R.id.activityStartTutorial_ViewPager_pager);
        pager.setAdapter(adapter);

        mBtSkip = findViewById(R.id.activityStartTutorial_skip);
        mBtSkip.setOnClickListener(this);

//        http://developer.alexanderklimov.ru/android/animation/translateanimation.php

//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
//                               RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//
//                           layoutParams.setMargins(150, 150, 0, 0);
//                       img.setLayoutParams(layoutParams);

/*
        getSupportActionBar().hide();
        mArrWindow = new View[]{
                findViewById(R.id.actyvityStartTutorial_t1),
                findViewById(R.id.actyvityStartTutorial_t2),
                findViewById(R.id.actyvityStartTutorial_t3),
                findViewById(R.id.actyvityStartTutorial_t4),
                findViewById(R.id.actyvityStartTutorial_t5),
                findViewById(R.id.actyvityStartTutorial_t6),
                findViewById(R.id.actyvityStartTutorial_t7),
                findViewById(R.id.actyvityStartTutorial_t8),
                findViewById(R.id.actyvityStartTutorial_t9)
        };


        findViewById(R.id.activityStartTutorial_skip).setOnClickListener(this);
        findViewById(R.id.activityStartTutorial_gotIt).setOnClickListener(this);
*/
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activityStartTutorial_skip:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            break;
            case R.id.fragmentTutorialPage_View_gotIt:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        private Page[] mPage = new Page[]{
                new Page(R.layout.fragment_tutorial_page_1),
                new Page(R.layout.fragment_tutorial_page_2),
                new Page(R.layout.fragment_tutorial_page_3),
                new Page(R.layout.fragment_tutorial_page_4),
                new Page(R.layout.fragment_tutorial_page_5),
                new Page(R.layout.fragment_tutorial_page_6),
                new Page(R.layout.fragment_tutorial_page_7),
                new Page(R.layout.fragment_tutorial_page_8),
                new Page9()
        };

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return mPage[position % mPage.length];
        }

        @Override
        public int getCount() {
            return mPage.length;
        }

    }

    public class Page extends Fragment {

        private int mViewId = 0;

        public Page(int viewId) {
            mViewId = viewId;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(mViewId, null);
            onCV(v);
            return v;
        }

        protected void onCV(View view) {

        }
    }

    public class Page9 extends Page {
        public Page9() {
            super(R.layout.fragment_tutorial_page_9);
        }

        @Override
        protected void onCV(View v) {
            v.findViewById(R.id.fragmentTutorialPage_View_gotIt).setOnClickListener(StartTutorialActivity.this);
        }

        @Override
        public void onResume(){
            super.onResume();
            mBtSkip.setVisibility(View.GONE);
        }

        @Override
        public void onPause(){
            super.onPause();
            mBtSkip.setVisibility(View.VISIBLE);
        }



    }

}
