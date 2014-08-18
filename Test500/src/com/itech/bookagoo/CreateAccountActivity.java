package com.itech.bookagoo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
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
import java.util.Date;

/**
 * Created by Artem on 28.02.14.
 */
public class CreateAccountActivity extends BaseActivity implements View.OnClickListener {

    private static final String LOG_TEST = "CreateAccountActivity";
    private View mBtnOk;
    private Handler mHandler = new Handler();
    private ProgressDialog mProgressDialog = null;

    private int mYear = 2010;
    private int mMonth = 11;
    private int mDay = 1;
    private static final int DATE_DIALOG = 1;
    private long mDateTime = 0;

    private DatePickerDialog.OnDateSetListener mOnDateSet = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            Date d = new Date(0);
            d.setYear(year - 1900);
            d.setMonth(monthOfYear - 1);
            d.setDate(dayOfMonth);
            mDateTime = d.getTime();

            TextView txt = (TextView) findViewById(R.id.activityCreateAccount_TextView_dateOfBirt);

            txt.setText(
                    "" + dayOfMonth
                            + "/" + ((monthOfYear < 10) ? "0" + monthOfYear : "" + monthOfYear)
                            + "/" + year);

            txt.setTextColor(0xFF008f77);
            ((ImageView) findViewById(R.id.activityCreateAccount_ImageView_dateOfBirt))
                    .setImageResource(R.drawable.ic_calendaricon_green);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setIcon(R.drawable.abc_ic_ab_back);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.ab_create_account_activity);
        actionBar.getCustomView().findViewById(R.id.absCreateAccountActivity_ImageView_back).setOnClickListener(this);

        mBtnOk = findViewById(R.id.activityCreateAccount_btView_ok);
        mBtnOk.setOnClickListener(this);
        mBtnOk.setEnabled(false);

        ((CheckBox) findViewById(R.id.activityCreateAccount_CheckBox_ok)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mBtnOk.setEnabled(isChecked);
            }
        });

        findViewById(R.id.activityCreateAccount_View_dateOfBirt).setOnClickListener(this);


        final EditText etName = (EditText) findViewById(R.id.activityCreateAccount_EditText_name);
        final EditText etEmail = (EditText) findViewById(R.id.activityCreateAccount_EditText_email);
        final EditText etPass = (EditText) findViewById(R.id.activityCreateAccount_EditText_enterYourDetails);
        final EditText etByName = (EditText) findViewById(R.id.activityCreateAccount_EditText_nameaby);

        etName.addTextChangedListener(
                new TextWatcher() {
                    public void afterTextChanged(Editable s) {
                        Drawable drawable;
                        if (etName.getText().length() == 0) {
                            drawable = getResources().getDrawable(R.drawable.ic_fields_manicon);
                        } else {
                            drawable = getResources().getDrawable(R.drawable.ic_fields_manicon_green);
                        }
                        etName.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                }
        );
        etEmail.addTextChangedListener(
                new TextWatcher() {
                    public void afterTextChanged(Editable s) {
                        Drawable drawable;
                        if (etEmail.getText().length() == 0) {
                            drawable = getResources().getDrawable(R.drawable.ic_mail);
                        } else {
                            drawable = getResources().getDrawable(R.drawable.ic_mail_green);
                        }
                        etEmail.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                }
        );
        etPass.addTextChangedListener(
                new TextWatcher() {
                    public void afterTextChanged(Editable s) {
                        Drawable drawable;
                        if (etPass.getText().length() == 0) {
                            drawable = getResources().getDrawable(R.drawable.ic_fields_staricon);
                        } else {
                            drawable = getResources().getDrawable(R.drawable.ic_fields_staricon_green);
                        }
                        etPass.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                }
        );
        etByName.addTextChangedListener(
                new TextWatcher() {
                    public void afterTextChanged(Editable s) {
                        Drawable drawable;
                        if (etByName.getText().length() == 0) {
                            drawable = getResources().getDrawable(R.drawable.ic_fields_manicon);
                        } else {
                            drawable = getResources().getDrawable(R.drawable.ic_fields_manicon_green);
                        }
                        etByName.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                }
        );

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activityCreateAccount_btView_ok:

                RegistrationParams p = new RegistrationParams();
                p.userEmail = ((EditText) findViewById(R.id.activityCreateAccount_EditText_email)).getText().toString();
                p.userPassword = ((EditText) findViewById(R.id.activityCreateAccount_EditText_enterYourDetails)).getText().toString();
                p.userFirstName = ((EditText) findViewById(R.id.activityCreateAccount_EditText_name)).getText().toString();
                if (p.userFirstName.equals("")) p.userFirstName = null;
                p.userBabyFirstName = ((EditText) findViewById(R.id.activityCreateAccount_EditText_nameaby)).getText().toString();
                if (p.userBabyFirstName.equals("")) p.userBabyFirstName = null;
                p.userBabyBorn = ((RadioButton) findViewById(R.id.activityCreateAccount_RadioButton_born)).isChecked();
                boolean isBabyMale = ((RadioButton) findViewById(R.id.activityCreateAccount_RadioButton_boy)).isChecked();
                p.userBabySex = isBabyMale ? BookAgooApi.SEX.MALE : BookAgooApi.SEX.FEMALE;
                p.userBabyBirthDateUnix = (long) Math.ceil(mDateTime / 1000);

                int userPasswordLength = p.userPassword.length();
                if (p.userEmail.equals("") || p.userPassword.equals("")
                        || userPasswordLength < 8 || userPasswordLength > 128 || p.userBabyBirthDateUnix == 0) {
                    Toast.show(R.string.invalid_parameters);
                    return;
                }

                new RegistrationAsyncTask(mHandler).execute(p);

                break;
            case R.id.absCreateAccountActivity_ImageView_back:
                finish();
                break;
            case R.id.activityCreateAccount_View_dateOfBirt:

                Date d = (mDateTime != 0) ? new Date(mDateTime) : new Date();

                mYear = d.getYear() + 1900;
                mMonth = d.getMonth() + 1;
                mDay = d.getDate();

                showDialog(DATE_DIALOG);
                break;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {                       //Overriding onCreateDialog()
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mOnDateSet, mYear, mMonth, mDay);

        }
        return null;
    }


    class RegistrationAsyncTask extends AsyncTask<RegistrationParams, Void, Void> {

        private Handler mHandler;

        public RegistrationAsyncTask(Handler h) {
            mHandler = h;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            mProgressDialog = new ProgressDialog(CreateAccountActivity.this);
            mProgressDialog.setMessage(getString(R.string.mess_registration));
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
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
                profile.setBabyBirthDateUnix(jsObjBaby.getLong(BookAgooApi.JSON.BIRTH_DATE_UNIX));

                finish();
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

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
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
