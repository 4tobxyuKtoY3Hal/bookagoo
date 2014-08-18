package com.itech.bookagoo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.itech.bookagoo.tool.Log;
import com.itech.bookagoo.tool.Toast;
import com.itech.bookagoo.tool.errors.ApiException;
import com.itech.bookagoo.tool.errors.NetworkDisabledException;
import com.itech.bookagoo.work.BookAgooApi;
import com.itech.bookagoo.work.Profile;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

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
    private EditText mEdTxtDateOfBirth;
    private EditText mEdTxtName2;
    private EditText mEdTxtDateOfBirth2;
    private View mButTapMommyPhoto;
    private View mButTapDaddyPhoto;

    private boolean mIsMommy = true;

    private Bitmap mNewBitmapMommy;
    private Bitmap mNewBitmapDaddy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile_content_family, container, false);

        mTxtMommy = (TextView) v.findViewById(R.id.fragmentProfileContentFamily_TextView_mommy);
        mTxtDaddy = (TextView) v.findViewById(R.id.fragmentProfileContentFamily_TextView_daddy);
        mImgMommy = (ImageView) v.findViewById(R.id.fragmentProfileContentFamily_ImageView_mommy);
        mImgDaddy = (ImageView) v.findViewById(R.id.fragmentProfileContentFamily_ImageView_daddy);
        mEdTxtName = (EditText) v.findViewById(R.id.fragmentProfileContentFamily_EditText_name);
        //mEdTxtLastName = (EditText) v.findViewById(R.id.fragmentProfileContentFamily_EditText_lastName);
        mEdTxtDateOfBirth = (EditText) v.findViewById(R.id.fragmentProfileContentFamily_EditText_dateOfBirth);
        mEdTxtName2 = (EditText) v.findViewById(R.id.fragmentProfileContentFamily_EditText_name2);
        //mEdTxtLastName = (EditText) v.findViewById(R.id.fragmentProfileContentFamily_EditText_lastName);
        mEdTxtDateOfBirth2 = (EditText) v.findViewById(R.id.fragmentProfileContentFamily_EditText_dateOfBirth2);

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
        mEdTxtName2.addTextChangedListener(
                new TextWatcher() {
                    public void afterTextChanged(Editable s) {
                        Drawable drawable;
                        if (mEdTxtName2.getText().length() == 0) {
                            drawable = getResources().getDrawable(R.drawable.ic_fields_manicon);
                        } else {
                            drawable = getResources().getDrawable(R.drawable.ic_fields_manicon_green);
                        }
                        mEdTxtName2.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                }
        );

        v.findViewById(R.id.fragmentProfileContentFamily_Button_Ok).setOnClickListener(this);

        mTxtMommy.setOnClickListener(this);
        mTxtDaddy.setOnClickListener(this);

        v.findViewById(R.id.fragmentProfileContentFamily_View_mommyPhoto).setOnClickListener(this);
        v.findViewById(R.id.fragmentProfileContentFamily_View_daddyPhoto).setOnClickListener(this);
        mButTapMommyPhoto = v.findViewById(R.id.fragmentProfileContentBaby_View_tapMommyPhoto);
        mButTapDaddyPhoto = v.findViewById(R.id.fragmentProfileContentBaby_View_tapDaddyPhoto);
        v.findViewById(R.id.fragmentProfileContentFamily_View_mommyPhoto).setOnClickListener(this);
        v.findViewById(R.id.fragmentProfileContentFamily_View_daddyPhoto).setOnClickListener(this);


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
            mImgDaddy.setAlpha(1 / 2);

            name = profile.getMotherFirstName();
            lastName = profile.getMotherLastName();
            date = "" + profile.getMotherBirtDateUnix();

        } else {
            mTxtMommy.setTextColor(0x66008F77);
            mTxtDaddy.setTextColor(0xFF008F77);
            mImgMommy.setAlpha(1 / 2);
            mImgDaddy.setAlpha(1);

            name = profile.getFatherFirstName();
            lastName = profile.getFatherLastName();
            date = "" + profile.getFatherBirtDateUnix();

        }

        if (date == null || date.equals("0")) date = "";

        mEdTxtName.setText(name);
        //mEdTxtLastName.setText(lastName);
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
            case R.id.fragmentProfileContentFamily_View_mommyPhoto:
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, ((MainActivity) getActivity()).REQUEST_MOMMY);
                break;
            case R.id.fragmentProfileContentFamily_View_daddyPhoto:
                Intent ii = new Intent(Intent.ACTION_PICK);
                ii.setType("image/*");
                startActivityForResult(ii, ((MainActivity) getActivity()).REQUEST_DADDY);
                break;
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
            case R.id.fragmentProfileContentFamily_Button_Ok:


                final String firstName = mEdTxtName.getText().toString();
                //final String lastName = mEdTxtLastName.getText().toString();
                final String date = mEdTxtDateOfBirth.getText().toString();


                final boolean isFather = !mIsMommy;

                new Thread(new Runnable() {
                    public void run() {
                        try {
                            BookAgooApi.getInstance().putUserData(
                                    Profile.getInstance().getUserId(),
                                    firstName, "", date, isFather);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NetworkDisabledException e) {
                            e.printStackTrace();
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        } catch (ApiException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();


                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ((MainActivity) getActivity()).REQUEST_MOMMY && resultCode == getActivity().RESULT_OK) {
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
            if (btmp == null) return;
            mImgMommy.setImageBitmap(btmp);
            mButTapMommyPhoto.setVisibility(View.GONE);
            mNewBitmapMommy = btmp;
        } else if (requestCode == ((MainActivity) getActivity()).REQUEST_DADDY && resultCode == getActivity().RESULT_OK) {
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
            if (btmp == null) return;
            mImgDaddy.setImageBitmap(btmp);
            mButTapDaddyPhoto.setVisibility(View.GONE);
            mNewBitmapDaddy = btmp;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}

