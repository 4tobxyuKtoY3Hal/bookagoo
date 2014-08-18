package com.itech.bookagoo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.itech.bookagoo.tool.Log;
import com.itech.bookagoo.work.Profile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

/**
 * Created by Artem on 13.04.14.
 */
public class ProfileContentBabyFragment extends Fragment implements ProfileFragment.ITabProfileContentFragment,
        View.OnClickListener {

    private int mYear = 2010;
    private int mMonth = 11;
    private int mDay = 1;
    private static final int DATE_DIALOG = 1;
    private long mDateTime = 0;
    private Bitmap mNewBitmap = null;
    private View mButTapBabyPhoto;

    private DatePickerDialog.OnDateSetListener mOnDateSet = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            Date d = new Date(0);
            d.setYear(year - 1900);
            d.setMonth(monthOfYear - 1);
            d.setDate(dayOfMonth);
            mDateTime = d.getTime();
            Profile.getInstance().setBabyBirthDateUnix(mDateTime);
            TextView txt = (TextView) getActivity().findViewById(R.id.fragmentProfileContentBaby_TextView_dateOfBirt);

            txt.setText(
                    "" + dayOfMonth
                            + "/" + ((monthOfYear < 10) ? "0" + monthOfYear : "" + monthOfYear)
                            + "/" + year);

            txt.setTextColor(0xFF008f77);
            ((ImageView) getActivity().findViewById(R.id.fragmentProfileContentBaby_ImageView_dateOfBirt))
                    .setImageResource(R.drawable.ic_calendaricon_green);
        }
    };

    private static final String LOG_TAG = ProfileContentBabyFragment.class.getName();

    //private CheckBox mChcbxExpecting;
    private ImageView mImgAva;
    private EditText mEdTxtName;
    // private EditText mEdTxtMiddleName;
    private EditText mEdTxtLastName;
//    private EditText mEdTxtDateOfBirth;
//    private EditText mEdTxtExpectrdBirthDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile_content_baby, container, false);

        // mChcbxExpecting = (CheckBox) v.findViewById(R.id.fragmentProfileContentBaby_CheckBox_expecting);
        mImgAva = (ImageView) v.findViewById(R.id.fragmentProfileContentBaby_ImageView_ava);
        mEdTxtName = (EditText) v.findViewById(R.id.fragmentProfileContentBaby_EditText_name);
        //mEdTxtMiddleName = (EditText) v.findViewById(R.id.fragmentProfileContentBaby_EditText_middleName);
        mEdTxtLastName = (EditText) v.findViewById(R.id.fragmentProfileContentBaby_EditText_lastName);
//        mEdTxtDateOfBirth = (EditText) v.findViewById(R.id.fragmentProfileContentBaby_EditText_dateOfBirth);
//        mEdTxtExpectrdBirthDate = (EditText) v.findViewById(R.id.fragmentProfileContentBaby_EditText_expectrdBirthDate);

        mEdTxtName.addTextChangedListener(
                new TextWatcher() {
                    public void afterTextChanged(Editable s) {
                        Drawable drawable;
                        if (mEdTxtName.getText().length() == 0) {
                            drawable = getResources().getDrawable(R.drawable.ic_fields_manicon);
                        } else {
                            drawable = getResources().getDrawable(R.drawable.ic_fields_manicon_green);
                        }
                        mEdTxtName.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                }
        );
        mEdTxtLastName.addTextChangedListener(
                new TextWatcher() {
                    public void afterTextChanged(Editable s) {
                        Drawable drawable;
                        if (mEdTxtLastName.getText().length() == 0) {
                            drawable = getResources().getDrawable(R.drawable.ic_fields_manicon);
                        } else {
                            drawable = getResources().getDrawable(R.drawable.ic_fields_manicon_green);
                        }
                        mEdTxtLastName.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                }
        );
        v.findViewById(R.id.fragmentProfileContentBaby_View_dateOfBirt).setOnClickListener(this);
        //mChcbxExpecting.setOnCheckedChangeListener(this);

        mButTapBabyPhoto = v.findViewById(R.id.fragmentProfileContentBaby_View_tapBabyPhoto);
        v.findViewById(R.id.fragmentProfileContentBaby_View_babyPhoto).setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragmentProfileContentBaby_View_dateOfBirt:
                mDateTime = Profile.getInstance().getBabyBirthDateUnix();
                Date d = (mDateTime != 0) ? new Date(mDateTime) : new Date();

                mYear = d.getYear() + 1900;
                mMonth = d.getMonth() + 1;
                mDay = d.getDate();
                (new DatePickerDialog(getActivity(), mOnDateSet, mYear, mMonth, mDay)).show();
                break;
            case R.id.fragmentProfileContentBaby_View_babyPhoto:
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, ((MainActivity) getActivity()).REQUEST_BABY);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ((MainActivity) getActivity()).REQUEST_BABY && resultCode == getActivity().RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap btmp = null;
            try {
                btmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                //http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(btmp == null) return;
            mImgAva.setImageBitmap(btmp);
            mButTapBabyPhoto.setVisibility(View.GONE);
            mNewBitmap = btmp;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();

        //mChcbxExpecting.setChecked(!Profile.getInstance().getBabyBorn());

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
//            mChcbxExpecting.setChecked(!profile.getBabyBorn());
        }


        String date = "" + profile.getBabyBirthDateUnix();
//        if(date.equals("0")) date = "";

//        mEdTxtDateOfBirth.setText(date);

//        if (mChcbxExpecting.isChecked()) {
//
//            mEdTxtExpectrdBirthDate.setVisibility(View.VISIBLE);
//            mEdTxtName.setVisibility(View.GONE);
//            mEdTxtMiddleName.setVisibility(View.GONE);
//            mEdTxtLastName.setVisibility(View.GONE);
//            mEdTxtDateOfBirth.setVisibility(View.GONE);
//            mImgAva.setVisibility(View.GONE);
//
//            mEdTxtExpectrdBirthDate.setText(date);
//
//        } else {
//
//            mEdTxtExpectrdBirthDate.setVisibility(View.GONE);
//            mEdTxtName.setVisibility(View.VISIBLE);
//            mEdTxtMiddleName.setVisibility(View.VISIBLE);
//            mEdTxtLastName.setVisibility(View.VISIBLE);
//            mEdTxtDateOfBirth.setVisibility(View.VISIBLE);
//            mImgAva.setVisibility(View.VISIBLE);
//
//            mEdTxtName.setText(profile.getBabyFirstName());
//            mEdTxtMiddleName.setText(profile.getBabyMidleName());
//            mEdTxtLastName.setText(profile.getBabyLastName());
//
//            mEdTxtDateOfBirth.setText(date);
//
//        }
    }

//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        updateUi(false);
//    }


}
