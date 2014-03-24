package com.example.test500;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.actionbarsherlock.app.SherlockActivity;

/**
 * Created by Artem on 28.02.14.
 */

public class LoginActivity extends SherlockActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        findViewById(R.id.activityLogin_Button_login).setOnClickListener(this);
        findViewById(R.id.activityLogin_View_loginCreate).setOnClickListener(this);
        findViewById(R.id.activityLogin_View_loginFacebook).setOnClickListener(this);
        findViewById(R.id.activityLogin_View_loginTwitter).setOnClickListener(this);

    }

    @Override
       public void onClick(View v) {

           switch (v.getId()) {
               case R.id.activityLogin_Button_login:
                   startActivity(new Intent(this, MainActivity.class));
                   finish();
                   break;
               case R.id.activityLogin_View_loginCreate:
                   startActivity(new Intent(this, CreateAccountActivity.class));
                   break;
               case R.id.activityLogin_View_loginFacebook:

                   break;
               case R.id.activityLogin_View_loginTwitter:

                   break;
           }
       }

}
