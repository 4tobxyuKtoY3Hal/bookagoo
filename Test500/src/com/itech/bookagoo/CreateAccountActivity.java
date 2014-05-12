package com.itech.bookagoo;

import android.os.Bundle;
import android.view.View;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.itech.bookagoo.tool.Utils;
import com.itech.bookagoo.tool.errors.ApiException;
import com.itech.bookagoo.tool.errors.NetworkDisabledException;
import com.itech.bookagoo.work.BookAgooApi;
import org.json.JSONException;

import java.net.URISyntaxException;

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
                    try {
                        api.registration(userEmail, userPass);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (NetworkDisabledException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    } catch (ApiException e) {
                        e.printStackTrace();
                    }
                } else {

                }


                break;
        }
    }



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
