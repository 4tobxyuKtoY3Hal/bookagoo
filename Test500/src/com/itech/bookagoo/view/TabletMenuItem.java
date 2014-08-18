package com.itech.bookagoo.view;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import com.itech.bookagoo.R;

/**
 * Created by Artem on 03.07.14.
 */
public class TabletMenuItem {

    private ImageView mIco;
    private int mIdIco = 0;
    private int mIdIcoTap = 0;
    private Context mContext;
    private String mUrlIco = null;
    private View mItemView = null;
    private boolean mIsProfile = false;
    private boolean mIsTap = false;

    public TabletMenuItem(Context cnx) {

        mContext = cnx;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mItemView = inflater.inflate(R.layout.item_tablet_menu, null, true);
        mIco = (ImageView) mItemView.findViewById(R.id.itemTabletMenu_ImageView_ico);

    }

    public TabletMenuItem setIdIco(int id) {
        mIdIco = id;
        if (mIco != null) {
            if (mIdIco > 0) {
                mIco.setImageResource(mIdIco);
            }
        }
        return this;
    }

    public TabletMenuItem setIdIcoTop(int id) {
        mIdIcoTap = id;
        return this;
    }

    public TabletMenuItem setUrlAva(String url) {
        mUrlIco = url;
        if (mUrlIco != null) {
            isProfile(true);
            mIco.setImageURI(Uri.parse(url));
        }
        return this;
    }

    public boolean isTap() {
        return mIsTap;
    }

    public TabletMenuItem isTap(boolean isTop) {
        if (isTop != mIsTap) {
            mIsTap = isTop;
            if (mIsTap) {
                if (mIdIcoTap > 0) {
                    mIco.setImageResource(mIdIcoTap);
                }
            } else {
                if (mIdIco > 0) {
                    mIco.setImageResource(mIdIco);
                }
            }

        }
        return this;
    }

    public View getItemView() {
        return mItemView;
    }

    private void isProfile(boolean profile) {
        mIsProfile = profile;

    }

}
