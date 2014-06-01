package com.itech.bookagoo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import com.actionbarsherlock.app.SherlockActivity;
import com.itech.bookagoo.service.ApiService;
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

    private final String LOG_TAG = LoginActivity.class.getName();
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
            if (mProgressDialog != null) {
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
                Log.v(LOG_TAG, jsObj.toString());

                Profile profile = Profile.getInstance();
                profile.setAuthToken(jsObj.getString(BookAgooApi.JSON.AUTH_TOKEN));
                String userId = jsObj.getString(BookAgooApi.JSON.USER_ID);
                profile.setUserId(userId);

                ApiService.queryGetUserData();

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
                Log.e(LOG_TAG, "ApiException key=" + e.getCode() + "   mess: " + e.getMessage());
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
            if (mProgressDialog != null) {
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
