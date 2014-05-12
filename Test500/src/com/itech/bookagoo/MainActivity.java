package com.itech.bookagoo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.itech.bookagoo.tool.Utils;
import com.itech.bookagoo.view.NavigationItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

public class MainActivity extends SherlockFragmentActivity implements AdapterView.OnItemClickListener {

    private NavigationAdapter mNavigationAdapter;
    private SlidingMenu mNavigationBox;
   // private View mContent;
    private ArrayList<NavigationItem> mArrNavigationItem = new ArrayList<NavigationItem>();

    private final IContentFragment[] mContensFragments = new IContentFragment[]{
            new ProfileFragment(),
            new WallFragment(),
            new BookFragment(),
            new SpecialContentFragment(),
            new NotificationsFragment(),
            new TutorialFragment(),
            new AdoutFragment()
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUp();
    }

    private void setUp() {

        setContentView(R.layout.activity_main);

        mNavigationBox = new SlidingMenu(this);
        mNavigationBox.setMode(SlidingMenu.LEFT);
        mNavigationBox.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        mNavigationBox.setShadowWidthRes(R.dimen.sliding_menu_shadow_width);
        mNavigationBox.setShadowDrawable(R.drawable.drawer_shadow);
        mNavigationBox.setBehindWidth(Math.round(Utils.dpToPx(230)));
        mNavigationBox.setFadeDegree(0.0f);
        mNavigationBox.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        mNavigationBox.setMenu(R.layout.sliding_menu);

        getSupportActionBar().setHomeButtonEnabled(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.activityMain_FrameLayout_container, new WallFragment())
                .commit();

        for (IContentFragment f : mContensFragments) {

            NavigationItem item = new NavigationItem(this);
            item.setTitle(f.getNameTitle())
                    .setName(f.getNameTitle())
                    .setUrlAva(f.getUrlIco())
                    .setIdIco(f.getIdIco())
                    .setIdIcoTop(f.getIdIcoTop());

            mArrNavigationItem.add(item);
        }
        mArrNavigationItem.get(1).isTap(true);
        mNavigationAdapter = new NavigationAdapter(mArrNavigationItem);
        ListView listView = (ListView) mNavigationBox.findViewById(R.id.slidimgMenu_ListView_menu);
        listView.setAdapter(mNavigationAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {  // узнаем ID нажатой кнопки
            case android.R.id.home: // если это кнопка-иконка ActionBar,
                mNavigationBox.toggle(true);        // открываем меню (или закрываем)
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { // если нажата кнопка "Назад"
            if (mNavigationBox.isMenuShowing()) { // и если SlidingMenu открыто
                mNavigationBox.toggle(true); // закрываем его
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //Если уже активная вкладка
        if(mArrNavigationItem.get(position).isTap()) return;

        MainActivity.IContentFragment contentFragment = mContensFragments[position];

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activityMain_FrameLayout_container, (Fragment) contentFragment)
                .commit();

        getSherlock().setTitle(getString(contentFragment.getIdTitle()));

        if (mNavigationBox.isMenuShowing()) { // и если SlidingMenu открыто
            mNavigationBox.toggle(true); // закрываем его

        }

        for(NavigationItem navigationItem : mArrNavigationItem){
            navigationItem.isTap(false);
        }

        mArrNavigationItem.get(position).isTap(true);

    }

    private class NavigationAdapter extends BaseAdapter {

        private ArrayList<NavigationItem> mArrItem;

        public NavigationAdapter(ArrayList<NavigationItem> arrItem) {
            mArrItem = arrItem;
        }

        @Override
        public int getCount() {
            return mArrItem.size();
        }

        @Override
        public Object getItem(int position) {
            return mArrItem.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            return mArrItem.get(position).getItemView();
        }
    }

    public static interface IContentFragment {

        public int getIdTitle();

        public String getNameTitle();

        public int getIdIco();

        public int getIdIcoTop();

        public String getUrlIco();

    }

}
