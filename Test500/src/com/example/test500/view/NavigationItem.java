package com.example.test500.view;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.test500.R;

/**
 * Created by Artem on 11.04.14.
 */
public class NavigationItem {

    private ImageView mIco;
    private ImageView mAva;
    private TextView mTextName;
    private TextView mTextTitle;

    private boolean mIsProfile = false;

    private int mIdIco = 0;
    private int mIdIcoTap = 0;
    private String mUrlIco = null;

    private View mItemView = null;
    private boolean mIsTap = false;
    private Context mContext;

    public NavigationItem(Context context) {


        setupUi(context);
    }

    private void setupUi(Context context) {

        mContext = context;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mItemView = inflater.inflate(R.layout.item_navigation, null, true);

        mIco = (ImageView) mItemView.findViewById(R.id.itemNavigation_ImageView_ico);
        mAva = (ImageView) mItemView.findViewById(R.id.itemNavigation_ImageView_ava);
        mTextTitle = (TextView) mItemView.findViewById(R.id.itemNavigation_TextView_title);
        mTextName = (TextView) mItemView.findViewById(R.id.itemNavigation_TextView_name);
    }

    public NavigationItem setTitle(String title) {
        mTextTitle.setText(title);
        return this;
    }

    public NavigationItem setName(String name) {
        mTextName.setText(name);
        return this;
    }

    public NavigationItem setIdIco(int id) {
        mIdIco = id;
        if (mIco != null) {
            if (mIdIco > 0) {
                mIco.setImageResource(mIdIco);
            }
        }
        return this;
    }

    public NavigationItem setIdIcoTop(int id) {
        mIdIcoTap = id;
        return this;
    }

    public NavigationItem setUrlAva(String url) {
        mUrlIco = url;
        if (mUrlIco != null) {
            isProfile(true);
            mAva.setImageURI(Uri.parse(url));
        }
        return this;
    }

    public boolean isTap() {
        return mIsTap;
    }

    public NavigationItem isTap(boolean isTop) {
        if (isTop != mIsTap) {
            mIsTap = isTop;
            if (mIsTap) {
                mTextName.setTextColor(mContext.getResources().getColor(R.color.navigation_draver_item_text_name_tap));
                mTextTitle.setTextColor(mContext.getResources().getColor(R.color.navigation_draver_item_text_name_tap));
                if (mIdIcoTap > 0) {
                    mIco.setImageResource(mIdIcoTap);
                }
            } else {
                mTextName.setTextColor(mContext.getResources().getColor(R.color.navigation_draver_item_text_name));
                mTextTitle.setTextColor(mContext.getResources().getColor(R.color.navigation_draver_item_text_name));
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

        View vi = mItemView.findViewById(R.id.itemNavigation_View_item);
        View vp = mItemView.findViewById(R.id.itemNavigation_View_profile);

        if (mIsProfile) {
            if (vi.getVisibility() != View.GONE) vi.setVisibility(View.GONE);
            if (vp.getVisibility() != View.VISIBLE) vp.setVisibility(View.VISIBLE);
        } else {
            if (vi.getVisibility() != View.VISIBLE) vi.setVisibility(View.VISIBLE);
            if (vp.getVisibility() != View.GONE) vp.setVisibility(View.GONE);
        }
    }
}
