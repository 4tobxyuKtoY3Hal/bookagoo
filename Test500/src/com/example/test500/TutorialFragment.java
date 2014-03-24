package com.example.test500;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.test500.MainActivity;

/**
 * Created by Artem on 02.03.14.
 */
public class TutorialFragment extends Fragment implements MainActivity.IContentFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tutorial, container, false);
    }

    @Override
    public int getIdTitle() {
        return R.string.title_tutorial;
    }

    @Override
    public String getNameTitle() {
        return App.getContext().getString(R.string.title_tutorial);
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public int getIdIco() {
        return R.drawable.ic_menu5;
    }

    @Override
    public int getIdIcoTop() {
        return R.drawable.ic_menu5_tap;
    }

//    @Override
//    public int getIdIcoBar() {
//        return R.drawable.ic_menu5;
//    }

    @Override
    public String getUrlIco() {
        return null;
    }


}