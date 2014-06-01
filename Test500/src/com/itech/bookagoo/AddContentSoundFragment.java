package com.itech.bookagoo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Artem on 07.04.14.
 */
public class AddContentSoundFragment extends Fragment implements ITabContentFragment {

    private View mV = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.fragment_add_content_sound, container, false);
        return mV;
    }

    @Override
    public int getIdTitle() {
        return R.string.add_content_tab_itle_sound;
    }

    @Override
    public View getView() {
        return mV;
    }


}
