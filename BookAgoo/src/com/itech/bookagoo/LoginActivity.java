package com.itech.bookagoo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

/**
 * Created by Artem on 28.02.14.
 */

public class LoginActivity extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        findViewById(R.id.activityLogin_Button_login).setOnClickListener(this);
        findViewById(R.id.activityLogin_Button_createLogin).setOnClickListener(this);
        findViewById(R.id.activityLogin_Button_loginFacebook).setOnClickListener(this);
        findViewById(R.id.activityLogin_Button_loginTwitter).setOnClickListener(this);

    }

    @Override
       public void onClick(View v) {

           switch (v.getId()) {
               case R.id.activityLogin_Button_login:
                   startActivity(new Intent(this, MainActivity.class));
                   finish();
                   break;
               case R.id.activityLogin_Button_createLogin:
                   startActivity(new Intent(this, CreateAccountActivity.class));
                   break;
               case R.id.activityLogin_Button_loginFacebook:

                   break;
               case R.id.activityLogin_Button_loginTwitter:

                   break;
           }
       }

}
