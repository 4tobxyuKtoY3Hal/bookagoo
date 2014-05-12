package com.example.test500;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.actionbarsherlock.app.SherlockActivity;
import com.example.test500.work.BookAgooApi;
import org.json.JSONObject;

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
//                startActivity(new Intent(this, AddContentActivity.class));
//                startActivity(new Intent(this, TestActivity.class));
                finish();
                BookAgooApi api = BookAgooApi.getInstance();
                api.login("1", "1", new BookAgooApi.OnQueryListener() {
                    @Override
                    public void onCompletion(JSONObject e) {
                        Log.d("log_test", e.toString());
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("log_test", error);
                    }

                    @Override
                    public void onApiError(int errorCode, String mess) {
                        Log.i("log_test", ">" + errorCode + " " + mess);
                    }
                });

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
