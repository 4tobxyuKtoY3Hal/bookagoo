package com.itech.bookagoo;

import android.content.Intent;
import android.os.Bundle;
import com.actionbarsherlock.app.SherlockActivity;
import com.itech.bookagoo.work.Profile;

/**
 * Created by Artem on 25.02.14.
 */

public class StartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        getSupportActionBar().hide();

//        startActivity(new Intent(this, TestActivity.class));
//        finish();


        if(Profile.getInstance().getAuthToken() == null){
            if(App.getInstance().isTablet()){
                startActivity(new Intent(this, LoginActivity.class));
            } else {
                startActivity(new Intent(this, StartTutorialActivity.class));
            }

        } else {
            startActivity(new Intent(this, MainActivity.class));
        }

        finish();
    }




}
