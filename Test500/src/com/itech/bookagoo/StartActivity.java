package com.itech.bookagoo;

import android.content.Intent;
import android.os.Bundle;
import com.actionbarsherlock.app.SherlockActivity;
import com.itech.bookagoo.work.Profile;

/**
 * Created by Artem on 25.02.14.
 */

public class StartActivity extends SherlockActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        getSupportActionBar().hide();

        if(Profile.getInstance().getAuthToken() == null){
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }

        finish();
    }




}
