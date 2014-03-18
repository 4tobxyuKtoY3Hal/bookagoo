package com.itech.bookagoo;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
        //т.к. в стиле не цепается цвет, то назначаем его здесь
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_background_color)));
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
