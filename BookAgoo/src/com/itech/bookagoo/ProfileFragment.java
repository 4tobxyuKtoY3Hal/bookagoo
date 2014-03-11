package com.itech.bookagoo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Artem on 02.03.14.
 */
public class ProfileFragment extends Fragment implements MainActivity.IContentFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public int getIdTitle() {
        return R.string.title_profile;
    }

    @Override
    public String getName() {
        return "Name";
    }

    @Override
    public String getEmail() {
        return "mail@mail.com";
    }

    @Override
    public int getIdIco() {
        return 0;
    }

    @Override
    public int getIdIcoActiv() {
        return 0;
    }

    @Override
    public int getIdIcoBar() {
        return 0;
    }

    @Override
    public String getUrlIco() {
        return null;
    }


}