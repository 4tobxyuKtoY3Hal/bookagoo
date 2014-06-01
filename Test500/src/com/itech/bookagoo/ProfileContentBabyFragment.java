package com.itech.bookagoo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import com.itech.bookagoo.tool.Log;
import com.itech.bookagoo.work.Profile;

/**
 * Created by Artem on 13.04.14.
 */
public class ProfileContentBabyFragment extends Fragment implements ProfileFragment.ITabProfileContentFragment,
        CompoundButton.OnCheckedChangeListener {

    private static final String LOG_TAG = ProfileContentBabyFragment.class.getName();

    private CheckBox mChcbxExpecting;
    private ImageView mImgAva;
    private EditText mEdTxtName;
    private EditText mEdTxtMiddleName;
    private EditText mEdTxtLastName;
    private EditText mEdTxtDateOfBirth;
    private EditText mEdTxtExpectrdBirthDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile_content_baby, container, false);

        mChcbxExpecting = (CheckBox) v.findViewById(R.id.fragmentProfileContentBaby_CheckBox_expecting);
        mImgAva = (ImageView) v.findViewById(R.id.fragmentProfileContentBaby_ImageView_ava);
        mEdTxtName = (EditText) v.findViewById(R.id.fragmentProfileContentBaby_EditText_name);
        mEdTxtMiddleName = (EditText) v.findViewById(R.id.fragmentProfileContentBaby_EditText_middleName);
        mEdTxtLastName = (EditText) v.findViewById(R.id.fragmentProfileContentBaby_EditText_lastName);
        mEdTxtDateOfBirth = (EditText) v.findViewById(R.id.fragmentProfileContentBaby_EditText_dateOfBirth);
        mEdTxtExpectrdBirthDate = (EditText) v.findViewById(R.id.fragmentProfileContentBaby_EditText_expectrdBirthDate);

        mChcbxExpecting.setOnCheckedChangeListener(this);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        mChcbxExpecting.setChecked(!Profile.getInstance().getBabyBorn());

        updateUi(false);
    }

    @Override
    public int getIdTitle() {
        return R.string.profile_content_tab_itle_baby;
    }

    @Override
    public void updateUi() {
        updateUi(true);
    }

    public void updateUi(boolean updateChcbxExpecting) {

        Log.i(LOG_TAG, "updateUi");
        Profile profile = Profile.getInstance();

        if (updateChcbxExpecting) {
            mChcbxExpecting.setChecked(!profile.getBabyBorn());
        }


        String date = "" + profile.getBabyBirthDateUnix();
        if(date.equals("0")) date = "";

        if (mChcbxExpecting.isChecked()) {

            mEdTxtExpectrdBirthDate.setVisibility(View.VISIBLE);
            mEdTxtName.setVisibility(View.GONE);
            mEdTxtMiddleName.setVisibility(View.GONE);
            mEdTxtLastName.setVisibility(View.GONE);
            mEdTxtDateOfBirth.setVisibility(View.GONE);
            mImgAva.setVisibility(View.GONE);

            mEdTxtExpectrdBirthDate.setText(date);

        } else {

            mEdTxtExpectrdBirthDate.setVisibility(View.GONE);
            mEdTxtName.setVisibility(View.VISIBLE);
            mEdTxtMiddleName.setVisibility(View.VISIBLE);
            mEdTxtLastName.setVisibility(View.VISIBLE);
            mEdTxtDateOfBirth.setVisibility(View.VISIBLE);
            mImgAva.setVisibility(View.VISIBLE);

            mEdTxtName.setText(profile.getBabyFirstName());
            mEdTxtMiddleName.setText(profile.getBabyMidleName());
            mEdTxtLastName.setText(profile.getBabyLastName());

            mEdTxtDateOfBirth.setText(date);

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        updateUi(false);
    }
}
