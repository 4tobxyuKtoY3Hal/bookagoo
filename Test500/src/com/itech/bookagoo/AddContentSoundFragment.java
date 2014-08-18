package com.itech.bookagoo;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Field;

/**
 * Created by Artem on 07.04.14.
 */
public class AddContentSoundFragment extends AbstrTabAddContentFragment {

    private static final int TYPE = TYPES.SOUND;
    private File mFile = null;
    private View mV = null;
    private ImageView imgLoadSound;
    private TextView txtLoadSound;
    private EditText edLoadSound;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.fragment_add_content_sound, container, false);
//        mV.findViewById(R.id.fragmentAddContent_View_loadSound).setOnClickListener(this);
//        imgLoadSound = (ImageView) mV.findViewById(R.id.fragmentAddContent_ImageView_loadSound);
//        txtLoadSound = (TextView) mV.findViewById(R.id.fragmentAddContent_TextView_loadSound);
//        edLoadSound = (EditText) mV.findViewById(R.id.fragmentAddContent_EditText_loadSound);

        return mV;
    }

    @Override
    public View getV() {
        return mV;
    }

    @Override
    public int getIdTitle() {
        return R.string.add_content_tab_itle_sound;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public ContentData getContData() {
        ContentData cd = new ContentData();
        cd.file = mFile;
        cd.text = edLoadSound.getText().toString();
        return cd;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragmentAddContent_View_loadSound:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");
                startActivityForResult(intent, 128);

                break;
        }
        super.onClick(v);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 128:
                if (resultCode == getActivity().RESULT_OK) {
                    Uri uri = data.getData();

                   mFile = new File(uri.getPath());

                    txtLoadSound.setTextColor(App.getContext().getResources().getColor(R.color.txt_active));
                    txtLoadSound.setText(mFile.getName());
                }

                break;
        }
    }

}

