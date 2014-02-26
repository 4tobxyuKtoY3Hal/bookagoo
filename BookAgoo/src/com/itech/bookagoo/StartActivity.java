package com.itech.bookagoo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

/**
 * Created by Artem on 25.02.14.
 */

public class StartActivity extends ActionBarActivity {

    private static StartActivity mStartActivity;
    private BaseFragment mFragment = null;
    private static LoginFragment mLoginFragment = new LoginFragment();
    private static CreateAccountFragment mCreateAccountFragment = new CreateAccountFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mStartActivity = this;

  //      gotoFragment(mLoginFragment);
        gotoFragment(mCreateAccountFragment);

    }

    private void gotoFragment(BaseFragment fragment) {

        Log.d("log_test", "gotoFragment>" + fragment);

        ActionBar actionBar = getSupportActionBar();
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();

        if (mFragment != null) {
            trans.remove(mFragment);
            trans.commit();
        }

        mFragment = fragment;

        int id = fragment.getIdTitle();
        if (id > 0) {
            actionBar.show();
            actionBar.setTitle(id);
        } else {
            actionBar.hide();
        }
        Log.d("log_test", "v");
        trans.add(R.id.activityStart_layout_container, mFragment).commit();
        Log.d("log_test", "^");
    }

    public static void gotoFragment(int iFragment) {

        switch (iFragment) {
            case Fragments.CREATE_ACCOUNT:
                mStartActivity.gotoFragment(mCreateAccountFragment);
                break;
            case Fragments.LOGIN:
                mStartActivity.gotoFragment(mLoginFragment);
                break;
        }
    }

    public static class BaseFragment extends Fragment {

        protected StartActivity _activity = null;

        public BaseFragment() {
            _activity = (StartActivity) getActivity();
        }

        public int getIdTitle() {
            return -1;
        }

    }

    public class Fragments {
        public static final int LOGIN = 0;
        public static final int CREATE_ACCOUNT = 1;
    }

}
