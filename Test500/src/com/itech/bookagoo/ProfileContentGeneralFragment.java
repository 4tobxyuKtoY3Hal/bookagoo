package com.itech.bookagoo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import com.itech.bookagoo.service.ApiService;
import com.itech.bookagoo.tool.Log;
import com.itech.bookagoo.tool.Toast;
import com.itech.bookagoo.work.BookAgooApi;
import com.itech.bookagoo.work.Profile;

/**
 * Created by Artem on 13.04.14.
 */
public class ProfileContentGeneralFragment extends Fragment implements ProfileFragment.ITabProfileContentFragment, View.OnClickListener {

    private static final String LOG_TAG = ProfileContentGeneralFragment.class.getName();
    private RadioButton mRbtnDaddy;
    private RadioButton mRbtnMommy;
    private RadioButton mRbtnOther;
    private EditText mEtxtName;
    private EditText mEtxtMail;
    private  EditText mEtxtPass;
    private EditText mEtxtNewPass;
    private EditText mEtxtConfirmPass;
    private Button mBtnOk;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile_content_general, container, false);

        assert v != null;
        mRbtnDaddy = (RadioButton) v.findViewById(R.id.fragmentProfileContentGeneral_RadioButton_daddy);
        mRbtnMommy = (RadioButton) v.findViewById(R.id.fragmentProfileContentGeneral_RadioButton_mommy);
        mRbtnOther = (RadioButton) v.findViewById(R.id.fragmentProfileContentGeneral_RadioButton_other);
        mEtxtName = (EditText) v.findViewById(R.id.fragmentProfileContentGeneral_EditText_name);
        mEtxtMail = (EditText) v.findViewById(R.id.fragmentProfileContentGeneral_EditText_mail);
        mEtxtPass = (EditText) v.findViewById(R.id.fragmentProfileContentGeneral_EditText_pass);
        mEtxtNewPass = (EditText) v.findViewById(R.id.fragmentProfileContentGeneral_EditText_newPass);
        mEtxtConfirmPass = (EditText) v.findViewById(R.id.fragmentProfileContentGeneral_EditText_confirmPass);
        mBtnOk = (Button) v.findViewById(R.id.fragmentProfileContentGeneral_Button_ok);

        mBtnOk.setOnClickListener(this);


        return v;

    }

    @Override
        public void onStart() {
            super.onStart();
            updateUi();
        }


    @Override
    public int getIdTitle() {
        return R.string.profile_content_tab_itle_general;
    }

    @Override
    public void updateUi(){
        Log.i(LOG_TAG, "updateUi");

        Profile profile = Profile.getInstance();

        String im = profile.getFamilyStatus();

        if(im != null){
            if(im.equals(BookAgooApi.FAMILY_STATUS.MAMMY)){
                mRbtnMommy.setChecked(true);
            } else if(im.equals(BookAgooApi.FAMILY_STATUS.DADDY)){
                mRbtnDaddy.setChecked(true);
            } else if(im.equals(BookAgooApi.FAMILY_STATUS.OTHER)){
                mRbtnOther.setChecked(true);
            }
        }

        mEtxtName.setText(profile.getFirstName());
        mEtxtMail.setText(profile.getEmail());


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragmentProfileContentGeneral_Button_ok:
                String im = null;
                if(mRbtnMommy.isChecked()){
                    im = BookAgooApi.FAMILY_STATUS.MAMMY;
                } else if (mRbtnDaddy.isChecked()){
                    im = BookAgooApi.FAMILY_STATUS.DADDY;
                } else if (mRbtnOther.isChecked()){
                    im = BookAgooApi.FAMILY_STATUS.OTHER;
                }

                String newPass = mEtxtNewPass.getText().toString();
                String confirmPass = mEtxtConfirmPass.getText().toString();

                if(newPass.equals(confirmPass)){
                    ApiService.queryPutGeneral(
                            im,
                            mEtxtName.getText().toString(),
                            mEtxtMail.getText().toString(),
                            mEtxtPass.getText().toString(),
                            newPass
                    );
                } else {
                    Toast.show(R.string.mess_input_error_pass);
                }


            break;

        }
    }
}
