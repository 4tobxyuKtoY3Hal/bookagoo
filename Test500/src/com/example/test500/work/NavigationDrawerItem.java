package com.example.test500.work;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.test500.App;
import com.example.test500.R;

/**
 * Created by Artem on 12.03.14.
 */
public class NavigationDrawerItem {

    private ImageView mIco;
    private TextView mTextName;
    private TextView mTextEmail;

    private boolean mIsProfil = false;
    private boolean mIsTap = false;

    private int mIdIco = 0;
    private int mIdIcoTap = 0;
    private String mUrlIco = null;

    private View mItemView = null;

    public NavigationDrawerItem() {
        setupUi();
    }

    private void setupUi() {

        LayoutInflater inflater = (LayoutInflater) App.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mItemView = inflater.inflate(R.layout.view_item_navigation_drawer, null, true);

        mIco = (ImageView) mItemView.findViewById(R.id.viewItrmNavigationDrawer_ImageView_ico);
        mTextName = (TextView) mItemView.findViewById(R.id.viewItrmNavigationDrawer_TextView_name);
        mTextEmail = (TextView) mItemView.findViewById(R.id.viewItrmNavigationDrawer_TextView_email);
    }

    public NavigationDrawerItem isTap(boolean isTop) {
        if (isTop != mIsTap) {
            mIsTap = isTop;
            if (mIsTap) {
                mTextName.setTextColor(App.getContext().getResources().getColor(R.color.navigation_draver_item_text_name_tap));
                if (mIdIcoTap > 0) {
                    mIco.setImageResource(mIdIcoTap);
                }
            } else {
                mTextName.setTextColor(App.getContext().getResources().getColor(R.color.navigation_draver_item_text_name));
                if (mIdIco > 0) {
                    mIco.setImageResource(mIdIco);
                }
            }

        }
        return this;
    }

    public boolean isTap() {
        return mIsTap;
    }

    public NavigationDrawerItem isProfil(boolean isProfil) {
        if (mIsProfil != isProfil) {
            mIsProfil = isProfil;
            if (mIsProfil) {
                if (mTextEmail.getVisibility() != View.VISIBLE) mTextEmail.setVisibility(View.VISIBLE);
            } else {
                if (mTextEmail.getVisibility() != View.GONE) mTextEmail.setVisibility(View.GONE);
            }
        }
        return this;
    }

    public boolean isProfil() {
        return mIsProfil;
    }

    public NavigationDrawerItem setNameTitle(String name) {
        mTextName.setText(name);
        return this;
    }

    public NavigationDrawerItem setEmail(String email) {
        if (email != null) isProfil(true);
        mTextEmail.setText(email);
        return this;
    }

    public NavigationDrawerItem setIdIco(int id) {
        mIdIco = id;
        if (mIco != null) {
            if (mIdIco>0) {
                if (!mIsTap) {
                    mIco.setImageResource(mIdIco);
                }
            }
        }

        return this;
    }

    public NavigationDrawerItem setIdIcoTap(int id) {
        mIdIcoTap = id;
        return this;
    }

    public NavigationDrawerItem setUrlIco(String url) {
        mUrlIco = url;
//        if (mIsProfil) {
//            mIco.setImageURI(Uri.parse(url));
//        }
        return this;
    }

    public View getItemView() {
        return mItemView;
    }

}
