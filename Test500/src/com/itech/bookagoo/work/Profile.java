package com.itech.bookagoo.work;

import android.content.Context;
import android.content.SharedPreferences;
import com.itech.bookagoo.App;

/**
 * Created by Artem on 26.03.14.
 */
public class Profile {

    private static Profile sProfile = null;
    private SharedPreferences mPref;

    private Profile() {
        Context context = App.getContext();
        mPref = context.getSharedPreferences("BookAgoo", context.MODE_PRIVATE);
    }

    public static Profile getInstance() {
        if (sProfile == null) {
            sProfile = new Profile();
        }
        return sProfile;
    }

    public void setEmail(String value) {
        SharedPreferences.Editor ed = mPref.edit();
        ed.putString(KEY.EMAIL, value);
        ed.commit();
    }

    public String getEmail() {
        return mPref.getString(KEY.EMAIL, null);
    }

    public void setFirstName(String value) {
        SharedPreferences.Editor ed = mPref.edit();
        ed.putString(KEY.FIRST_NAME, value);
        ed.commit();
    }

    public String getFirstName() {
        return mPref.getString(KEY.FIRST_NAME, null);
    }

    public void setBabyBirthDateUnix(long value) {
        SharedPreferences.Editor ed = mPref.edit();
        ed.putLong(KEY.BABY_BIRTH_DATE_UNIX, value);
        ed.commit();
    }

    public long getBabyBirthDateUnix() {
        return mPref.getLong(KEY.BABY_BIRTH_DATE_UNIX, 0);
    }

    public void setBabyBorn(boolean value) {
        SharedPreferences.Editor ed = mPref.edit();
        ed.putBoolean(KEY.BABY_BORN, value);
        ed.commit();
    }

    public boolean getBabyBorn() {
        return mPref.getBoolean(KEY.BABY_BORN, false);
    }

    public void setBabyFirstName(String value) {
        SharedPreferences.Editor ed = mPref.edit();
        ed.putString(KEY.BABY_FIRST_NAME, value);
        ed.commit();
    }

    public void setBabySex(String value) {
        SharedPreferences.Editor ed = mPref.edit();
        ed.putString(KEY.BABY_SEX, value);
        ed.commit();
    }

    public String getBabySex() {
        return mPref.getString(KEY.BABY_SEX, null);
    }

    public void setAuthToken(String value) {
        SharedPreferences.Editor ed = mPref.edit();
        ed.putString(KEY.AUTh_TOKEN, value);
        ed.commit();
        BookAgooApi.getInstance().getAutoToken(value);
    }

    public void setUserId(String value) {
            SharedPreferences.Editor ed = mPref.edit();
            ed.putString(KEY.USER_ID, value);
            ed.commit();
    }

    public String getAutoToken() {
        return mPref.getString(KEY.AUTh_TOKEN, null);
    }

    /*
        public void setAuthorization(String authorization) {
            SharedPreferences.Editor ed = mPref.edit();
            ed.putString(KEY.AUTHORIZATION, authorization);
            ed.commit();
        }

        public String getAuthorization() {
            return mPref.getString(KEY.AUTHORIZATION, null);
        }
    */
    private static interface KEY {
        public static final String AUTHORIZATION = "authorization";
        public static final String AUTh_TOKEN = "auth_token";
        public static final String USER_ID = "user_id";
        public static final String FIRST_NAME = "first_name";
        public static final String EMAIL = "email";
        public static final String ID = "id";
        public static final String BABY_BORN = "baby_born";
        public static final String BABY_FIRST_NAME = "baby_first_name";
        public static final String BABY_SEX = "baby_sex";
        public static final String BABY_BIRTH_DATE_UNIX = "baby_birth_date_unix";
    }

}
