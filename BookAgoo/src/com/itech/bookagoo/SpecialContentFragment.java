package com.itech.bookagoo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Artem on 02.03.14.
 */

public class SpecialContentFragment extends Fragment implements MainActivity.IContentFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_special_content, container, false);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public int getIdTitle() {
        return R.string.title_special_content;
    }

    @Override
    public String getName() {
        return App.getContext().getString(R.string.title_special_content);
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public int getIdIco() {
        return R.drawable.ic_menu3;
    }

    @Override
    public int getIdIcoTop() {
        return R.drawable.ic_menu3_tap;
    }

    @Override
    public int getIdIcoBar() {
        return R.drawable.ic_menu3;
    }

    @Override
    public String getUrlIco() {
        return null;
    }


}