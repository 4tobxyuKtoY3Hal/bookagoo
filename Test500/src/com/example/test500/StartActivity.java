package com.example.test500;

import android.content.Intent;
import android.os.Bundle;
import com.actionbarsherlock.app.SherlockActivity;

/**
 * Created by Artem on 25.02.14.
 */

public class StartActivity extends SherlockActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        getSupportActionBar().hide();

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }




}
