package com.itech.bookagoo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;

/**
 * Created by Artem on 02.03.14.
 */
public class WallFragment extends Fragment implements MainActivity.IContentFragment {

    @Override
       public void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
    }

   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_wall, container, false);

        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();
        ((MainActivity)getActivity()).visibleIcoAdd();
    }

    @Override
    public int getIdTitle() {
        return R.string.title_wall;
    }

    @Override
    public String getNameTitle() {
        return App.getContext().getString(R.string.title_wall);
    }

    @Override
    public int getIdIco() {
        return R.drawable.ic_menu1;
    }

    @Override
    public int getIdIcoTop() {
        return R.drawable.ic_menu1_tap;
    }

//    @Override
//    public int getIdIcoBar() {
//        return R.drawable.ic_menu1;
//    }

    @Override
    public String getUrlIco() {
        return null;
    }


}
