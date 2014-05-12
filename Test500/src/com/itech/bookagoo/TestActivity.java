package com.itech.bookagoo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.itech.bookagoo.tool.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * Created by Artem on 08.04.14.
 */
public class TestActivity extends SherlockFragmentActivity implements AdapterView.OnItemClickListener {

    private SlidingMenu mMenu;
    private CharSequence mTitle = null;
    private View mContent;
    private final MainActivity.IContentFragment[] mContensFragments = new MainActivity.IContentFragment[]{
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

        mTitle = getTitle();

        ListView listView = (ListView) mMenu.findViewById(R.id.slidimgMenu_ListView_menu);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                new String[]{"1", "2", "3"});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private void setUp() {
        setContentView(R.layout.activity_test);


        // configure the SlidingMenu
        mMenu = new SlidingMenu(this);
        mMenu.setMode(SlidingMenu.LEFT);
        mMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        mMenu.setShadowWidthRes(R.dimen.sliding_menu_shadow_width);
        mMenu.setShadowDrawable(R.drawable.drawer_shadow);
        mMenu.setBehindWidth(Math.round(Utils.dpToPx(220)));
        mMenu.setFadeDegree(0.0f);
        mMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        mMenu.setMenu(R.layout.sliding_menu);

        getSupportActionBar().setHomeButtonEnabled(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.activityTest_FrameLayout_container, new WallFragment())
                .commit();
        mContent = findViewById(R.id.activityTest_FrameLayout_container);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {  // узнаем ID нажатой кнопки
            case android.R.id.home: // если это кнопка-иконка ActionBar,
                mMenu.toggle(true);        // открываем меню (или закрываем)
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { // если нажата кнопка "Назад"
            if (mMenu.isMenuShowing()) { // и если SlidingMenu открыто
                mMenu.toggle(true); // закрываем его
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MainActivity.IContentFragment contentFragment = mContensFragments[position];

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activityTest_FrameLayout_container, (Fragment) contentFragment)
                        .commit();

                mTitle = getString(contentFragment.getIdTitle());
    }
}
