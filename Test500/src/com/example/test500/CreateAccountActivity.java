package com.example.test500;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockActivity;
import com.example.test500.tool.Utils;
import com.example.test500.work.BookAgooApi;
import org.json.JSONObject;

/**
 * Created by Artem on 28.02.14.
 */
public class CreateAccountActivity extends SherlockActivity implements View.OnClickListener {

    private String mUserName;
    private String mBabyDate;
    private String mBabyName;

    private boolean mIsBorn;
    private boolean mIsBoy;

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
        switch (v.getId()) {
            case R.id.activityCreateAccount_Button_ok:

                BookAgooApi api = BookAgooApi.getInstance();
                String userEmail = Utils.editTextToString(this, R.id.activityCreateAccount_EditText_email);
                String userPass = Utils.editTextToString(this, R.id.activityCreateAccount_EditText_enterYourDetails);

                mUserName = Utils.editTextToString(this, R.id.activityCreateAccount_EditText_name);
                mBabyDate = Utils.editTextToString(this, R.id.activityCreateAccount_EditText_dateOfBirt);
                mBabyName = Utils.editTextToString(this, R.id.activityCreateAccount_EditText_nameaby);
                mIsBorn = Utils.radioButtonToBoolean(this, R.id.activityCreateAccount_RadioButton_born);
                mIsBoy = Utils.radioButtonToBoolean(this, R.id.activityCreateAccount_RadioButton_boy);

                if(true){
                    api.registration(userEmail, userPass, mOnRegistration);
                } else {

                }


                break;
        }
    }

    private BookAgooApi.OnQueryListener mOnRegistration = new BookAgooApi.OnQueryListener(){

        @Override
        public void onCompletion(JSONObject e) {
            Log.v("log_test", e.toString());
        }

        @Override
        public void onError(String error) {
            Log.e("log_test", error);
        }

        @Override
        public void onApiError(int errorCode, String mess) {
            Toast.makeText(App.getContext(), "Error " + errorCode + ": " + mess, Toast.LENGTH_SHORT).show();
        }
    };


}
