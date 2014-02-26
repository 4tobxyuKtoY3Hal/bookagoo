package com.itech.bookagoo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Artem on 25.02.14.
 */
public class LoginFragment extends StartActivity.BaseFragment implements View.OnClickListener {


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, null);

        view.findViewById(R.id.fragmntLogin_Button_login).setOnClickListener(this);
        view.findViewById(R.id.fragmntLogin_Button_createLogin).setOnClickListener(this);
        view.findViewById(R.id.fragmntLogin_Button_loginFacebook).setOnClickListener(this);
        view.findViewById(R.id.fragmntLogin_Button_loginTwitter).setOnClickListener(this);


        return view;

    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.fragmntLogin_Button_login:
                Log.d("log_test", "login");
                break;
            case R.id.fragmntLogin_Button_createLogin:
                Log.d("log_test", "createLogin");
                _activity.gotoFragment(0);
                break;
            case R.id.fragmntLogin_Button_loginFacebook:
                Log.d("log_test", "loginFacebook");
                break;
            case R.id.fragmntLogin_Button_loginTwitter:
                Log.d("log_test", "loginTwitter");
                break;
        }
    }


}
