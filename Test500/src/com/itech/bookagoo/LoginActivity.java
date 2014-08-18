package com.itech.bookagoo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsoluteLayout;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.itech.bookagoo.service.ApiService;
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

public class LoginActivity extends BaseActivity implements View.OnClickListener, View.OnKeyListener {

    private final String LOG_TAG = LoginActivity.class.getName();
    private Handler mHandler = new Handler();
    ProgressDialog mProgressDialog = null;

    private static final int WIDH_TUCHA = Utils.dpToPx(240);
    private static final int HEIT_TUCHA = Utils.dpToPx(160);


    private FrameLayout mContntTuhca = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        App.getTracker().send(MapBuilder
                .createAppView()
                .set(Fields.SCREEN_NAME, "Log in")
                .build()
        );
        getSupportActionBar().hide();

        for (int i = 0; i < 5; i++) {
            newTucha((FrameLayout) findViewById(R.id.activityLogin_FrameLayout_cotextTuga));
        }
        findViewById(R.id.activityLogin_Button_login).setOnClickListener(this);
        findViewById(R.id.activityLogin_View_loginCreate).setOnClickListener(this);
        findViewById(R.id.activityLogin_View_loginFacebook).setOnClickListener(this);
        findViewById(R.id.activityLogin_View_loginTwitter).setOnClickListener(this);
        findViewById(R.id.activityLogin_TextView_forgot).setOnClickListener(this);

        final EditText etMail = (EditText) findViewById(R.id.activityLogin_EditText_email);
        etMail.addTextChangedListener(
                new TextWatcher() {
                    public void afterTextChanged(Editable s) {
                        Drawable drawable;
                        if (etMail.getText().length() == 0) {
                            drawable = getResources().getDrawable(R.drawable.ic_mail);
                        } else {
                            drawable = getResources().getDrawable(R.drawable.ic_mail_green);
                        }
                        etMail.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                }
        );
        final EditText etPass = (EditText) findViewById(R.id.activityLogin_EditText_pass);
        etPass.setOnKeyListener(this);
        etPass.addTextChangedListener(

                new TextWatcher() {
                    public void afterTextChanged(Editable s) {
                        if (etPass.getText().toString().length() == 0) {
                            findViewById(R.id.activityLogin_TextView_forgot).setBackgroundResource(R.drawable.bt_text_gray);
                        } else {
                            findViewById(R.id.activityLogin_TextView_forgot).setBackgroundResource(R.drawable.bt_text);
                        }
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                }
        );

    }

    private ImageView newTucha(FrameLayout fl) {
        final double k = .2 + Math.random() * .8;
        Display display = getWindowManager().getDefaultDisplay();
        final int widthDisplay = display.getWidth();
        final int heightDislay = display.getHeight();
        int width = (int) Math.round((widthDisplay + WIDH_TUCHA) * Math.random())- WIDH_TUCHA;
        int heiht = (int) Math.round((heightDislay - HEIT_TUCHA) * Math.random());
        final ImageView iv = new ImageView(this);

        Animation.AnimationListener onAnimaionListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                int width = - (int) Math.round(WIDH_TUCHA * k);
                int heiht = (int) Math.round(heightDislay * Math.random());
                TranslateAnimation anim = new TranslateAnimation(
                                width,
                                widthDisplay,
                                heiht,
                                heiht);
                        anim.setAnimationListener(this);
                        anim.setDuration(Math.round(k * 100 * (widthDisplay - width)));
                        anim.setFillAfter(true);
                        iv.startAnimation(anim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

        iv.setImageResource(R.drawable.tucha);
        FrameLayout.LayoutParams lParams = new FrameLayout.LayoutParams(
                (int) Math.round(WIDH_TUCHA * k),
                (int) Math.round(HEIT_TUCHA * k));

        int alpha = (int) Math.round(100 * k);

        if (android.os.Build.VERSION.SDK_INT < 11) {
                final AlphaAnimation animation = new AlphaAnimation(alpha, alpha);
                animation.setDuration(1);
                iv.startAnimation(animation);
            } else {
                iv.setAlpha(alpha);
        }

        TranslateAnimation anim = new TranslateAnimation(
                width,
                widthDisplay,
                heiht,
                heiht);
        anim.setAnimationListener(onAnimaionListener);
        anim.setFillAfter(true);
        anim.setInterpolator(new AccelerateInterpolator(1));
        anim.setDuration(Math.round((1 - k) * 400 * (widthDisplay - width)));
        //anim.setFillAfter(true);
        iv.startAnimation(anim);
        fl.addView(iv, lParams);
        return iv;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.activityLogin_Button_login:
                startLogin(
                        ((EditText) findViewById(R.id.activityLogin_EditText_email)).getText().toString(),
                        ((EditText) findViewById(R.id.activityLogin_EditText_pass)).getText().toString()
                );

                break;
            case R.id.activityLogin_View_loginCreate:
                startActivity(new Intent(this, CreateAccountActivity.class));
                break;
            case R.id.activityLogin_View_loginFacebook:

                break;
            case R.id.activityLogin_View_loginTwitter:

                break;
            case R.id.activityLogin_TextView_forgot:
                startActivity(new Intent(this, RecoverPassActivity.class));
                break;
        }
    }

    private void startLogin(String email, String password) {
        LoginParams p = new LoginParams();
        p.email = email;
        p.password = password;

        new LoginAsyncTask(mHandler).execute(p);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        switch (v.getId()) {
            case R.id.activityLogin_EditText_pass:

                if (event.getAction() == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    startLogin(
                            ((EditText) findViewById(R.id.activityLogin_EditText_email)).getText().toString(),
                            ((EditText) findViewById(R.id.activityLogin_EditText_pass)).getText().toString()
                    );
                    return true;
                }

        }
        return false;
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
            mProgressDialog.setCancelable(false);
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
                Log.v(LOG_TAG, "jsObj = " + jsObj);
                Log.v(LOG_TAG, "jsObj > " + jsObj.toString());

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
