package com.itech.bookagoo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

/**
 * Created by Artem on 28.02.14.
 */
public class CreateAccountActivity extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        findViewById(R.id.activityCreateAccount_Button_ok).setOnClickListener(this);

    }

    @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.activityCreateAccount_Button_ok:
                    finish();
                break;
            }
        }

}
