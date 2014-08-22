package com.itech.bookagoo;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.itech.bookagoo.tool.Log;

import java.io.File;
import java.util.Date;

/**
 * Created by Artem on 05.08.14.
 */
public class AddContentVideoFragment extends AbstrTabAddContentFragment {

    private static final int TYPE = TYPES.VIDEO;

    private View mV = null;
    private File mFile = null;
    private EditText mEdTxt;
    private View mViewContent;
    private ImageView mImg;
    private DisplayMetrics mDisplayMetrics;
    private Bitmap mTumb = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.fragment_add_content_video, container, false);

        assert mV != null;
        mViewContent = mV.findViewById(R.id.fragmentAddContentVideo_View_content);
        mImg = (ImageView) mV.findViewById(R.id.fragmentAddContentVideo_ImageView_img);
        mEdTxt = (EditText) mV.findViewById(R.id.fragmentAddContentVideo_EditText_mess);
        mV.findViewById(R.id.fragmentAddContentVideo_View_buttonAddVideo).setOnClickListener(this);

        mDisplayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);


        return mV;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragmentAddContentVideo_View_buttonAddVideo:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
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
                    final Uri uri = data.getData();

                    mFile = new File(getRealPathFromURI(uri));

                    new Thread(new Runnable() {
                        public void run() {

                            long time = new Date().getTime();
                            final Bitmap tumb = ThumbnailUtils.createVideoThumbnail(getRealPathFromURI(uri),
                                    MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
                            Log.d(LOG_TAG, "Open video tumbnail, time=" + (new Date().getTime() - time));

                            getHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    mTumb = tumb;
                                    mImg.setImageBitmap(mTumb);
                                }
                            });

                        }
                    }).start();

                }

                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mViewContent.getLayoutParams();
        params.height = mDisplayMetrics.widthPixels;
        mViewContent.setLayoutParams(params);

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
