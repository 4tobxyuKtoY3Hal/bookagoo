package com.itech.bookagoo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Artem on 02.03.14.
 */
public class WallFragment extends Fragment implements MainActivity.IContentFragment {

   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_wall, container, false);

        return rootView;
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public int getIdTitle() {
        return R.string.title_wall;
    }

    @Override
    public String getName() {
        return App.getContext().getString(R.string.title_wall);
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public int getIdIco() {
        return R.drawable.ic_menu1;
    }

    @Override
    public int getIdIcoActiv() {
        return R.drawable.ic_menu1;
    }

    @Override
    public int getIdIcoBar() {
        return R.drawable.ic_menu1;
    }

    @Override
    public String getUrlIco() {
        return null;
    }


}
