package com.example.test500;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.example.test500.tool.Utils;
import com.example.test500.work.BookAgooApi;
import org.json.JSONObject;

/**
 * Created by Artem on 28.02.14.
 */
public class CreateAccountActivity extends SherlockActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.activityCreateAccount_Button_ok).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activityCreateAccount_Button_ok:

                BookAgooApi api = BookAgooApi.getInstance();
                String userEmail = Utils.editTextToString(this, R.id.activityCreateAccount_EditText_email);
                String userPass = Utils.editTextToString(this, R.id.activityCreateAccount_EditText_enterYourDetails);
                String userName = Utils.editTextToString(this, R.id.activityCreateAccount_EditText_name);
                String babyDate = Utils.editTextToString(this, R.id.activityCreateAccount_EditText_dateOfBirt);
                String babyName = Utils.editTextToString(this, R.id.activityCreateAccount_EditText_nameaby);
                boolean isBorn = Utils.radioButtonToBoolean(this, R.id.activityCreateAccount_RadioButton_born);
                boolean isBoy = Utils.radioButtonToBoolean(this, R.id.activityCreateAccount_RadioButton_boy);

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

    @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    finish();
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }


}
