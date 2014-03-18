package com.itech.bookagoo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Artem on 02.03.14.
 */
public class ProfileFragment extends BaseContentFragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public int getIdTitle() {
        return R.string.title_profile;
    }

    @Override
    public String getNameTitle() {
        return "Фансека";
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
    public int getIdIcoTop() {
        return 0;
    }

//    @Override
//    public int getIdIcoBar() {
//        return 0;
//    }

    @Override
    public String getUrlIco() {
        return null;
    }


}