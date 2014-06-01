package com.itech.bookagoo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.itech.bookagoo.tool.Log;
import com.itech.bookagoo.work.Profile;

/**
 * Created by Artem on 13.04.14.
 */
public class ProfileContentFamilyFragment extends Fragment implements ProfileFragment.ITabProfileContentFragment,
        View.OnClickListener {

    private final String LOG_TAG = ProfileContentFamilyFragment.class.getName();

    private TextView mTxtMommy;
    private TextView mTxtDaddy;
    private ImageView mImgMommy;
    private ImageView mImgDaddy;
    private EditText mEdTxtName;
    private EditText mEdTxtLastName;
    private EditText mEdTxtDateOfBirth;

    private boolean mIsMommy = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile_content_family, container, false);

        mTxtMommy = (TextView) v.findViewById(R.id.fragmentProfileContentFamily_TextView_mommy);
        mTxtDaddy = (TextView) v.findViewById(R.id.fragmentProfileContentFamily_TextView_daddy);
        mImgMommy = (ImageView) v.findViewById(R.id.fragmentProfileContentFamily_ImageView_mommy);
        mImgDaddy = (ImageView) v.findViewById(R.id.fragmentProfileContentFamily_ImageView_daddy);
        mEdTxtName = (EditText) v.findViewById(R.id.fragmentProfileContentFamily_EditText_name);
        mEdTxtLastName = (EditText) v.findViewById(R.id.fragmentProfileContentFamily_EditText_lastName);
        mEdTxtDateOfBirth = (EditText) v.findViewById(R.id.fragmentProfileContentFamily_EditText_dateOfBirth);

        mTxtMommy.setOnClickListener(this);
        mTxtDaddy.setOnClickListener(this);

        return v;
    }

    @Override
    public int getIdTitle() {
        return R.string.profile_content_tab_itle_family;
    }

    @Override
    public void updateUi() {
        Log.i(LOG_TAG, "updateUi");
        Profile profile = Profile.getInstance();

        String name;
        String lastName;
        String date;

        if (mIsMommy) {
            mTxtMommy.setTextColor(0xFF008F77);
            mTxtDaddy.setTextColor(0x66008F77);
            mImgMommy.setAlpha(1);
            mImgDaddy.setAlpha(1/2);

            name = profile.getMotherFirstName();
            lastName = profile.getMotherLastName();
            date = "" + profile.getMotherBirtDateUnix();

        } else {
            mTxtMommy.setTextColor(0x66008F77);
            mTxtDaddy.setTextColor(0xFF008F77);
            mImgMommy.setAlpha(1/2);
            mImgDaddy.setAlpha(1);

            name = profile.getFatherFirstName();
            lastName = profile.getFatherLastName();
            date = "" + profile.getFatherBirtDateUnix();

        }

        if (date == null || date.equals("0")) date = "";

        mEdTxtName.setText(name);
        mEdTxtLastName.setText(lastName);
        mEdTxtDateOfBirth.setText(date);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUi();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragmentProfileContentFamily_TextView_mommy:
                if (!mIsMommy) {
                    mIsMommy = true;
                    updateUi();
                }
                break;
            case R.id.fragmentProfileContentFamily_TextView_daddy:
                if (mIsMommy) {
                    mIsMommy = false;
                    updateUi();
                }
                break;

        }
    }
}
