package com.itech.bookagoo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.itech.bookagoo.tool.Log;
import com.itech.bookagoo.tool.Toast;
import com.itech.bookagoo.tool.Utils;
import com.itech.bookagoo.tool.errors.ApiException;
import com.itech.bookagoo.tool.errors.NetworkDisabledException;
import com.itech.bookagoo.work.BookAgooApi;
import com.itech.bookagoo.work.Profile;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

/**
 * Created by Artem on 28.02.14.
 */
public class CreateAccountActivity extends SherlockActivity implements View.OnClickListener {

    private static final String LOG_TEST = "CreateAccountActivity";
    private Button mBtnOk;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBtnOk = (Button) findViewById(R.id.activityCreateAccount_Button_ok);
        mBtnOk.setOnClickListener(this);
        mBtnOk.setEnabled(false);

        ((CheckBox) findViewById(R.id.activityCreateAccount_CheckBox_ok)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mBtnOk.setEnabled(isChecked);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activityCreateAccount_Button_ok:

                RegistrationParams p = new RegistrationParams();
                p.userEmail = ((EditText) findViewById(R.id.activityCreateAccount_EditText_email)).getText().toString();
                p.userPassword = ((EditText) findViewById(R.id.activityCreateAccount_EditText_enterYourDetails)).getText().toString();
                p.userFirstName = ((EditText) findViewById(R.id.activityCreateAccount_EditText_name)).getText().toString();
                if (p.userFirstName.equals("")) p.userFirstName = null;
                p.userBabyFirstName = ((EditText) findViewById(R.id.activityCreateAccount_EditText_nameaby)).getText().toString();
                if (p.userBabyFirstName.equals("")) p.userBabyFirstName = null;
                p.userBabyBorn = ((RadioButton) findViewById(R.id.activityCreateAccount_RadioButton_born)).isChecked();
                boolean isBabyMale = ((RadioButton) findViewById(R.id.activityCreateAccount_RadioButton_boy)).isChecked();
                String babyBirthDate = ((EditText) findViewById(R.id.activityCreateAccount_EditText_dateOfBirt)).getText().toString();
                p.userBabySex = isBabyMale ? BookAgooApi.SEX.MALE : BookAgooApi.SEX.FEMALE;
                p.userBabyBirthDateUnix = Utils.dateStringToLong(babyBirthDate);

                int userPasswordLength = p.userPassword.length();
                if (p.userEmail.equals("") || p.userPassword.equals("")
                        || userPasswordLength < 8 || userPasswordLength > 128) {
                    Toast.show(R.string.invalid_parameters);
                    return;
                }

                new RegistrationAsyncTask(mHandler).execute(p);

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

    class RegistrationAsyncTask extends AsyncTask<RegistrationParams, Void, Void> {

        private Handler mHandler;

        public RegistrationAsyncTask(Handler h) {
            mHandler = h;
        }

        @Override
        protected Void doInBackground(RegistrationParams... params) {
            RegistrationParams p = params[0];
            try {
                BookAgooApi api = BookAgooApi.getInstance();
                JSONObject jsObj = api.registration(p.userEmail, p.userPassword, p.userFirstName,
                        p.userBabyBorn, p.userBabyFirstName, p.userBabySex, p.userBabyBirthDateUnix);

                Log.v(LOG_TEST, jsObj.toString());
                /*
                {
                    "subscription_plan":{
                        "active":true,
                        "customized":false,
                        "space":"1073741824.0",
                        "valid_til_unix":1400187600,
                        "type":"basic",
                        "valid_til":"2014-05-16"
                    },
                    "newsletter":true,
                    "social_data":{
                        "twitter":null,
                        "facebook":null
                    },
                    "pdf_status":"not_requested",
                    "father":{
                        "last_name":null,
                        "first_name":null,
                        "avatar":{
                            "original":null,
                            "profile":null
                        },
                        "birth_date":null,
                        "birth_date_unix":null
                    },
                    "country":null,
                    "total_space":0,
                    "id":"537a73586b733130f9460100",
                    "first_name":"Артём",
                    "mother":{
                        "last_name":null,
                        "first_name":null,
                        "avatar":{
                            "original":null,
                            "profile":null
                        },
                        "birth_date":null,
                        "birth_date_unix":null
                    },
                    "address":null,
                    "email":"art7384@gmail.com",
                    "pdf_link":null,
                    "payments":[],
                    "last_name":null,
                    "created_at":"2014-05-19T22:10:48.630+01:00",
                    "email_notifications":true,
                    "baby":{
                        "born":true,
                        "first_name":"Анна",
                        "sex":"female",
                        "birth_date":"1970-01-01T00:00:00.000+00:00",
                        "middle_name":null,
                        "last_name":null,
                        "avatar":{
                            "original":null,
                            "profile":null
                        },
                        "birth_date_unix":0,
                        "place_of_birth":null
                    },
                    "family_status":null,
                    "created_at_unix":1400533848,
                    "site_notifications":true
                }
                */
                Profile profile = Profile.getInstance();
                profile.setEmail(jsObj.getString(BookAgooApi.JSON.EMAIL));
                profile.setFirstName(jsObj.getString(BookAgooApi.JSON.FIRST_NAME));

                JSONObject jsObjBaby = jsObj.getJSONObject(BookAgooApi.JSON.BABY);
                profile.setBabyBorn(jsObjBaby.getBoolean(BookAgooApi.JSON.BORN));
                profile.setBabyFirstName(jsObjBaby.getString(BookAgooApi.JSON.FIRST_NAME));
                profile.setBabySex(jsObjBaby.getString(BookAgooApi.JSON.SEX));
                profile.setBabyBirthDateUnix(jsObjBaby.getLong(BookAgooApi.JSON.BIRT_DATE_UNIX));

                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NetworkDisabledException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (ApiException e) {
                Log.e(LOG_TEST, "ApiException key=" + e.getCode() + "   mess: " + e.getMessage());
                switch (e.getCode()) {
                    case 400:
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.show(R.string.invalid_parameters);
                            }
                        });
                        break;
                    case 409:
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.show(R.string.email_in_use);
                            }
                        });
                        break;
                    default:
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.show(R.string.unknown_error);
                            }
                        });
                }

            }
            return null;
        }
    }

    class RegistrationParams {
        public String userEmail = null;
        public String userPassword = null;
        public String userFirstName = null;
        public String userBabyFirstName = null;
        public String userBabySex = null;
        public boolean userBabyBorn = false;
        public long userBabyBirthDateUnix = 0;
    }

}
