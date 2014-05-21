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
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.itech.bookagoo.tool.Utils;
import com.itech.bookagoo.view.NavigationItem;
import com.itech.bookagoo.work.Profile;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

public class MainActivity extends SherlockFragmentActivity implements AdapterView.OnItemClickListener,
        View.OnClickListener {

    private NavigationAdapter mNavigationAdapter;
    private SlidingMenu mNavigationBox;
    private ImageView mIcoAdd;
    private Button mButLogaut;
    private ArrayList<NavigationItem> mArrNavigationItem = new ArrayList<NavigationItem>();
    private TextView mTitle;

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
        mButLogaut = (Button) actionBar.getCustomView().findViewById(R.id.absMainActivity_Button_logaut);
        mButLogaut.setOnClickListener(this);
        mTitle = (TextView) actionBar.getCustomView().findViewById(R.id.absMainActivity_TextView_title);

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


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {  // узнаем ID нажатой кнопки
//            case android.R.id.home: // если это кнопка-иконка ActionBar,
//                mNavigationBox.toggle(true);        // открываем меню (или закрываем)
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

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
        if (mArrNavigationItem.get(position).isTap()) return;

        if(mIcoAdd.getVisibility() != View.GONE) mIcoAdd.setVisibility(View.GONE);
        if(mButLogaut.getVisibility() != View.GONE) mButLogaut.setVisibility(View.GONE);

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

        mArrNavigationItem.get(position).isTap(true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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

    public void visibleIcoAdd(){
        if(mIcoAdd != null && mIcoAdd.getVisibility() != View.VISIBLE){
            mIcoAdd.setVisibility(View.VISIBLE);
        }
    }

    public void visibleButLogaut(){
            if(mButLogaut != null && mButLogaut.getVisibility() != View.VISIBLE){
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
