package com.itech.bookagoo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Artem on 07.04.14.
 */
public class AddContentTextFragment extends AbstrTabAddContentFragment{

    private static final int TYPE = TYPES.TEXT;

    private View mV = null;

    private EditText mEdComment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.fragment_add_content_text, container, false);
        mEdComment = (EditText) mV.findViewById(R.id.fragmentAddContext_EditText_comment);
        return mV;
    }

    @Override
    public View getV() {
        return mV;
    }

    @Override
    public int getIdTitle() {
        return R.string.add_content_tab_itle_text;
    }

    @Override
    public int getType() {
        return TYPE;
    }


    @Override
    public ContentData getContData() {
        ContentData cd = new ContentData();
        cd.text = mEdComment.getText().toString();
        return cd;
    }
}
