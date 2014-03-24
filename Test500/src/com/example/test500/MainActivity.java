package com.example.test500;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.example.test500.work.NavigationDrawerItem;

import java.util.ArrayList;

public class MainActivity extends SherlockFragmentActivity
        implements NavigationDrawerFragment.INavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private View mContent;
    private CharSequence mTitle = null;

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
        setContentView(R.layout.activity_main);

        setUp();


    }

    private void setUp() {

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.activityMain_NavigationDrawerFragment);

        mTitle = getTitle();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.activityMain_FrameLayout_container, new WallFragment())
                .commit();
        mContent = findViewById(R.id.activityMain_FrameLayout_container);


    }



    @Override
    public void onNavigationDrawerItemSelected(int position) {

        IContentFragment contentFragment = mContensFragments[position];

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activityMain_FrameLayout_container, (Fragment) contentFragment)
                .commit();

        mTitle = getString(contentFragment.getIdTitle());
    }

    @Override
    public ArrayList<NavigationDrawerItem> getNavigationDrawerItem() {

        ArrayList<NavigationDrawerItem> arrItem = new ArrayList<NavigationDrawerItem>();

        for (IContentFragment f : mContensFragments) {
            NavigationDrawerItem itemView = new NavigationDrawerItem();
            itemView.setNameTitle(f.getNameTitle())
                    .setEmail(f.getEmail())
                    .setUrlIco(f.getUrlIco())
                    .setIdIco(f.getIdIco())
                    .setIdIcoTap(f.getIdIcoTop());
            arrItem.add(itemView);
        }

        return arrItem;
    }

    public static interface IContentFragment {

        public int getIdTitle();

        public String getNameTitle();

        public String getEmail();

        public int getIdIco();

        public int getIdIcoTop();

        //public int getIdIcoBar();
        public String getUrlIco();

    }

}
