package com.itech.bookagoo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Artem on 02.03.14.
 */
public class AdoutFragment extends Fragment implements MainActivity.IContentFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_adout, container, false);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public int getIdTitle() {
        return R.string.title_adout;
    }

    @Override
    public String getName() {
        return App.getContext().getString(R.string.title_adout);
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public int getIdIco() {
        return R.drawable.ic_menu6;
    }

    @Override
    public String getUrlIco() {
        return null;
    }


}