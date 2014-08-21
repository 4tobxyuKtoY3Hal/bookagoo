package com.itech.bookagoo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by Artem on 05.08.14.
 */
public class AddContentVideoFragment extends AbstrTabAddContentFragment {

    private static final int TYPE = TYPES.VIDEO;

    private View mV = null;
    private File mFile = null;
    private EditText mEdTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.fragment_add_content_video, container, false);

        assert mV != null;
        View viewImg = mV.findViewById(R.id.fragmentAddContentVideo_View_img);
        mEdTxt = (EditText) mV.findViewById(R.id.fragmentAddContentVideo_EditText_mess);
//        mImg.setOnClickListener(this);

        viewImg.setMinimumHeight(viewImg.getWidth());

        return mV;
    }

    @Override
    public View getV() {


        return mV;
    }

    @Override
    public int getIdTitle() {
        return R.string.add_content_tab_itle_video;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public ContentData getContData() {
        ContentData cd = new ContentData();
        cd.file = mFile;
        cd.text = mEdTxt.getText().toString();
        return cd;
    }
}
