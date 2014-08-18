package com.itech.bookagoo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.app.ActionBar;
import com.itech.bookagoo.tool.Utils;
import com.itech.bookagoo.view.NavigationItem;
import com.itech.bookagoo.view.TabletMenuItem;
import com.itech.bookagoo.work.Profile;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

public class MainActivity extends BaseFragmentActivity implements AdapterView.OnItemClickListener,
        View.OnClickListener {

    private NavigationAdapter mNavigationAdapter;
    private TablMenuAdapter mTablMenuAdapter;
    private SlidingMenu mNavigationBox;
    private ImageView mIcoAdd;
    private View mButLogaut;
    private ArrayList<NavigationItem> mArrNavigationItem = new ArrayList<NavigationItem>();
    private ArrayList<TabletMenuItem> mArrTabletMenuItem = new ArrayList<TabletMenuItem>();
    private TextView mTitle;
    private ListView mMenuTablet;
    private App mApp;
    private int mPositionFragment = 1;
    private ImageView mImgAvaTablet = null;
    private ImageView mImgAva;
    private TextView mTxtUserName;
    private TextView mTxtUserMail;

    private static final String POSITION_FRAGMENT = "position_fragment";

    public static final int REQUEST_BABY = 110;
    public static final int REQUEST_MOMMY = 111;
    public static final int REQUEST_DADDY = 112;

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

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.ab_main_activity);
        actionBar.getCustomView().findViewById(R.id.absMainActivity_ImageView_menu).setOnClickListener(this);
        mIcoAdd = (ImageView) actionBar.getCustomView().findViewById(R.id.absMainActivity_ImageView_add);
        mIcoAdd.setOnClickListener(this);
        mButLogaut = actionBar.getCustomView().findViewById(R.id.absMainActivity_Button_logaut);
        mButLogaut.setOnClickListener(this);
        mTitle = (TextView) actionBar.getCustomView().findViewById(R.id.absMainActivity_TextView_title);


        setContentView(R.layout.activity_main);

        mApp = App.getInstance();

        mNavigationBox = new SlidingMenu(this);
        mNavigationBox.setMode(SlidingMenu.LEFT);
        mNavigationBox.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        mNavigationBox.setShadowWidthRes(R.dimen.sliding_menu_shadow_width);
        mNavigationBox.setShadowDrawable(R.drawable.drawer_shadow);
        mNavigationBox.setBehindWidth(Math.round(Utils.dpToPx(
                App.getInstance().isTablet() ? 320 : 250)));
        mNavigationBox.setFadeDegree(0.0f);
        mNavigationBox.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        mNavigationBox.setMenu(R.layout.sliding_menu);
        mNavigationBox.setOnOpenListener(new SlidingMenu.OnOpenListener() {
            @Override
            public void onOpen() {
                if (mMenuTablet.getVisibility() != View.GONE) {
                    mMenuTablet.setVisibility(View.GONE);
                }
            }
        });
        mNavigationBox.setOnCloseListener(new SlidingMenu.OnCloseListener() {
            @Override
            public void onClose() {
                if (mApp.isTablet()) {
                    mMenuTablet.setVisibility(View.VISIBLE);

                }
            }
        });

        getSupportActionBar().setHomeButtonEnabled(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.activityMain_FrameLayout_container, new WallFragment())
                .commit();

        for (IContentFragment f : mContensFragments) {
            NavigationItem item = new NavigationItem(this);
            item.setTitle(f.getNameTitle())
//                    .setName(f.getNameTitle())
//                    .setUrlAva(f.getUrlIco())
                    .setIdIco(f.getIdIco())
                    .setIdIcoTop(f.getIdIcoTop());

            mArrNavigationItem.add(item);

            TabletMenuItem tItem = new TabletMenuItem(this);
            tItem.setUrlAva(f.getUrlIco())
                    .setIdIco(f.getIdIco())
                    .setIdIcoTop(f.getIdIcoTop());
            mArrTabletMenuItem.add(tItem);
        }
        mArrNavigationItem.get(mPositionFragment).isTap(true);
        mArrTabletMenuItem.get(mPositionFragment).isTap(true);
        mNavigationAdapter = new NavigationAdapter(mArrNavigationItem);

        ListView listView = (ListView) mNavigationBox.findViewById(R.id.slidimgMenu_ListView_menu);
        View headerNavigation = getLayoutInflater().inflate(R.layout.header_navigation, null);
        mImgAva = (ImageView) headerNavigation.findViewById(R.id.headerNavigation_ImageView_ava);
        mTxtUserName = (TextView) headerNavigation.findViewById(R.id.headerNavigation_TextView_name);
        mTxtUserMail = (TextView) headerNavigation.findViewById(R.id.headerNavigation_TextView_mail);
        listView.addHeaderView(headerNavigation);
        listView.setAdapter(mNavigationAdapter);
        listView.setOnItemClickListener(this);


        mMenuTablet = (ListView) findViewById(R.id.activityMain_ListView_menu);

        if (mApp.isTablet()) {

            mTablMenuAdapter = new TablMenuAdapter(mArrTabletMenuItem);
            View headerTabletMenu = getLayoutInflater().inflate(R.layout.item_tablet_menu, null);
            mImgAvaTablet = (ImageView) headerTabletMenu.findViewById(R.id.itemTabletMenu_ImageView_ico);
            mImgAvaTablet.setImageResource(R.drawable.ic_menu_ava);
            mMenuTablet.addHeaderView(headerTabletMenu);
            mMenuTablet.setVisibility(View.VISIBLE);
            mMenuTablet.setAdapter(mTablMenuAdapter);
            mMenuTablet.setOnItemClickListener(this);
        }

        updateProfile();

    }

    public void updateProfile(){
        Profile profile = Profile.getInstance();
        String name = profile.getFirstName();
        String mail = profile.getEmail();

        if(name == null){
            mTxtUserName.setText("");
        } else {
            mTxtUserName.setText(name);
        }

        if(mail == null){
            mTxtUserMail.setText(""); ;
        } else {
            mTxtUserMail.setText(mail);
        }

//        TODO дописать установку аватарки
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(POSITION_FRAGMENT, mPositionFragment);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        gotoFragment(savedInstanceState.getInt(POSITION_FRAGMENT));

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
        if (position == 0) return;
        position--;
        gotoFragment(position);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.absMainActivity_ImageView_menu:
                mNavigationBox.toggle(true);
                break;
            case R.id.absMainActivity_Button_logaut:
                Profile.getInstance().logaut();
                startActivity(new Intent(this, StartActivity.class));
                finish();
                break;
            case R.id.absMainActivity_ImageView_add:
                startActivity(new Intent(this, AddContentActivity.class));
                break;
        }
    }

    private void gotoFragment(int position) {
        if (mArrNavigationItem.get(position).isTap()) return;

        if (mIcoAdd.getVisibility() != View.GONE) mIcoAdd.setVisibility(View.GONE);
        if (mButLogaut.getVisibility() != View.GONE) mButLogaut.setVisibility(View.GONE);

        MainActivity.IContentFragment contentFragment = mContensFragments[position];

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activityMain_FrameLayout_container, (Fragment) contentFragment)
                .commit();

        mTitle.setText(getString(contentFragment.getIdTitle()));

        if (mNavigationBox.isMenuShowing()) { // и если SlidingMenu открыто
            mNavigationBox.toggle(true); // закрываем его

        }

        for (NavigationItem navigationItem : mArrNavigationItem) {
            navigationItem.isTap(false);
        }

        for (TabletMenuItem tableMenuItem : mArrTabletMenuItem) {
            tableMenuItem.isTap(false);
        }

        mPositionFragment = position;
        mArrNavigationItem.get(position).isTap(true);
        mArrTabletMenuItem.get(position).isTap(true);
    }

    public void visibleIcoAdd() {
        if (mIcoAdd != null && mIcoAdd.getVisibility() != View.VISIBLE) {
            mIcoAdd.setVisibility(View.VISIBLE);
        }
    }

    public void visibleButLogaut() {
        if (mButLogaut != null && mButLogaut.getVisibility() != View.VISIBLE) {
            mButLogaut.setVisibility(View.VISIBLE);
        }
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
            if (position == 0) mArrItem.get(position).visibleExid();
            return mArrItem.get(position).getItemView();
        }
    }

    private class TablMenuAdapter extends BaseAdapter {

        private ArrayList<TabletMenuItem> mArrItem;

        public TablMenuAdapter(ArrayList<TabletMenuItem> arrItem) {
            mArrItem = arrItem;
        }

        @Override
        public int getCount() {
            return mArrItem.size();
        }

        @Override
        public Object getItem(int position) {
            return mArrItem.get(position).getItemView();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            return (View) getItem(position);
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
