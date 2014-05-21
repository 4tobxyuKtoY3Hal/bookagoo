package com.itech.bookagoo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import com.actionbarsherlock.app.SherlockActivity;
import com.itech.bookagoo.tool.Log;
import com.itech.bookagoo.tool.Toast;
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

public class LoginActivity extends SherlockActivity implements View.OnClickListener {

    private final String LOG_TEST = "LoginActivity";
    private Handler mHandler = new Handler();
    ProgressDialog mProgressDialog = null;

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
//                startActivity(new Intent(this, MainActivity.class));
//                startActivity(new Intent(this, AddContentActivity.class));
//                startActivity(new Intent(this, TestActivity.class));

                LoginParams p = new LoginParams();
                p.email = ((EditText) findViewById(R.id.activityLogin_EditText_email)).getText().toString();
                p.password = ((EditText) findViewById(R.id.activityLogin_EditText_pass)).getText().toString();

                new LoginAsyncTask(mHandler).execute(p);

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


    class LoginAsyncTask extends AsyncTask<LoginParams, Void, Void> {

        private Handler mHandler;

        public LoginAsyncTask(Handler h) {
            mHandler = h;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            mProgressDialog = new ProgressDialog(LoginActivity.this);
            mProgressDialog.setMessage(getString(R.string.mess_log_in));
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(LoginParams... params) {
            LoginParams p = params[0];
            try {
                BookAgooApi api = BookAgooApi.getInstance();
                JSONObject jsObj = api.login(p.email, p.password);
                    /*
                    {
                        "auth_token":"537a73586b733130f9460100:SMw-zSqNjHERdRLCxCyk",
                        "user_id":"537a73586b733130f9460100"
                    }
                    */
                Log.v(LOG_TEST, jsObj.toString());

                Profile profile = Profile.getInstance();
                profile.setAuthToken(jsObj.getString(BookAgooApi.JSON.AUTH_TOKEN));
                String userId = jsObj.getString(BookAgooApi.JSON.USER_ID);
                profile.setUserId(userId);
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
                jsObj = api.userData(userId);

                profile.setEmail(jsObj.getString(BookAgooApi.JSON.EMAIL));
                profile.setFirstName(jsObj.getString(BookAgooApi.JSON.FIRST_NAME));

                JSONObject jsObjBaby = jsObj.getJSONObject(BookAgooApi.JSON.BABY);
                profile.setBabyBorn(jsObjBaby.getBoolean(BookAgooApi.JSON.BORN));
                profile.setBabyFirstName(jsObjBaby.getString(BookAgooApi.JSON.FIRST_NAME));
                profile.setBabySex(jsObjBaby.getString(BookAgooApi.JSON.SEX));
                profile.setBabyBirthDateUnix(jsObjBaby.getLong(BookAgooApi.JSON.BIRT_DATE_UNIX));

                // TODO надо больше данных дергать

                Log.v(LOG_TEST, jsObj.toString());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(App.getContext(), MainActivity.class));
                        finish();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NetworkDisabledException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.show(R.string.no_internet_connection);
                    }
                });
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
                    case 401:
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.show(R.string.invalid_credentials);
                            }
                        });
                        break;
                    case 403:
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.show(R.string.unknown_error);
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

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(mProgressDialog != null) {
                           mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        }

    }

    class LoginParams {
        public String email = null;
        public String password = null;
    }

}
