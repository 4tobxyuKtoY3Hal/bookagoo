package com.example.test500;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Artem on 13.04.14.
 */
public class ProfileContentGeneralFragment extends Fragment implements ITabContentFragment {

    @Override
       public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
           return inflater.inflate(R.layout.fragment_profile_content_general, container, false);
       }


    @Override
    public int getIdTitle() {
        return R.string.profile_content_tab_itle_general;
    }
}
