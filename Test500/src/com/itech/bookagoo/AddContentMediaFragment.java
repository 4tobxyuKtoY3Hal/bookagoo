package com.itech.bookagoo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Artem on 07.04.14.
 */
public class AddContentMediaFragment extends AbstrTabAddContentFragment {

    private static final int TYPE = TYPES.MEDIA;

    private View mV = null;
    private ImageView mImg;
    private File mFile = null;
    private EditText mEdTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.fragment_add_content_media, container, false);
        mImg = (ImageView) mV.findViewById(R.id.fragmentAddContentMedia_ImageView_img);
        mEdTxt = (EditText) mV.findViewById(R.id.fragmentAddContentMedia_ImageView_mess);
        mImg.setOnClickListener(this);
        return mV;
    }

    @Override
    public View getV() {
        return mV;
    }

    @Override
    public int getIdTitle() {
        return R.string.add_content_tab_itle_media;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragmentAddContentMedia_ImageView_img:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                startActivityForResult(intent, 228);
                break;
            default:
                super.onClick(v);
        }
    }

    @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode) {
                case 228:
                    if (resultCode == getActivity().RESULT_OK) {
                        Uri uri = data.getData();


                        mFile = new File(getRealPathFromURI(uri));
                        mImg.setImageURI(uri);

                    }

                    break;
            }
        }

}
