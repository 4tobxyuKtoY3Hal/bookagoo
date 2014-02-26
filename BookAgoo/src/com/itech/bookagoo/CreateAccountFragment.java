package com.itech.bookagoo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Artem on 26.02.14.
 */
public class CreateAccountFragment extends StartActivity.BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("log_test", "CreateAccountFragment");

        View rootView = inflater.inflate(R.layout.fragment_create_account, container, false);
        return rootView;
    }

    public int getIdTitle() {
        return R.string.action_bar_title_create_account;
    }

}
