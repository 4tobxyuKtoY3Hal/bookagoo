package com.itech.bookagoo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Artem on 02.03.14.
 */
public class NotificationsFragment extends BaseContentFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);

        return rootView;
    }

    @Override
    public int getIdTitle() {
        return R.string.title_notifications;
    }

    @Override
    public String getNameTitle() {
        return App.getContext().getString(R.string.title_notifications) + " (13)";
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public int getIdIco() {
        return R.drawable.ic_menu4;
    }

    @Override
    public int getIdIcoTop() {
        return R.drawable.ic_menu4_tap;
    }

//    @Override
//    public int getIdIcoBar() {
//        return R.drawable.ic_menu4;
//    }

    @Override
    public String getUrlIco() {
        return null;
    }


}