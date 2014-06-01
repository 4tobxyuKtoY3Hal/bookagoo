package com.itech.bookagoo.work;

import android.content.Context;
import android.content.SharedPreferences;
import com.itech.bookagoo.App;
import com.itech.bookagoo.tool.ErrorReporter;

/**
 * Created by Artem on 26.03.14.
 */
public class Profile {

    private static Profile sProfile = null;
    private SharedPreferences mPref;
    private String mUserId = null;

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

    public void logaut() {
        SharedPreferences.Editor ed = mPref.edit();
        ed.clear();
        ed.commit();
        App.getErrorReporter().processReports(ErrorReporter.PRCESS.DELETE);
    }

    public void setEmail(String value) {
        if (value == null) return;
        if (value.equals("null")) return;
        SharedPreferences.Editor ed = mPref.edit();
        ed.putString(KEY.EMAIL, value);
        ed.commit();
    }

    public String getEmail() {
        synchronized (KEY.EMAIL) {
            return mPref.getString(KEY.EMAIL, null);
        }
    }

    public void setFirstName(String value) {
        if (value == null) return;
        if (value.equals("null")) return;
        synchronized (KEY.FIRST_NAME) {
            SharedPreferences.Editor ed = mPref.edit();
            ed.putString(KEY.FIRST_NAME, value);
            ed.commit();
        }
    }

    public String getFirstName() {
        synchronized (KEY.FIRST_NAME) {
            return mPref.getString(KEY.FIRST_NAME, "");
        }
    }

    public void setBabyBirthDateUnix(Long value) {
        if (value == null) return;
        if (value.equals("null")) return;
        synchronized (KEY.BABY_BIRTH_DATE_UNIX) {
            SharedPreferences.Editor ed = mPref.edit();
            ed.putLong(KEY.BABY_BIRTH_DATE_UNIX, value);
            ed.commit();
        }
    }

    public long getBabyBirthDateUnix() {
        synchronized (KEY.BABY_BIRTH_DATE_UNIX) {
            return mPref.getLong(KEY.BABY_BIRTH_DATE_UNIX, 0);
        }
    }

    public void setBabyBorn(Boolean value) {
        if (value == null) return;
        if (value.equals("null")) return;
        synchronized (KEY.BABY_BORN) {
            SharedPreferences.Editor ed = mPref.edit();
            ed.putBoolean(KEY.BABY_BORN, value);
            ed.commit();
        }
    }

    public boolean getBabyBorn() {
        synchronized (KEY.BABY_BORN) {
            return mPref.getBoolean(KEY.BABY_BORN, false);
        }
    }

    public void setBabyFirstName(String value) {
        if (value == null) return;
        if (value.equals("null")) return;
        synchronized (KEY.BABY_FIRST_NAME) {
            SharedPreferences.Editor ed = mPref.edit();
            ed.putString(KEY.BABY_FIRST_NAME, value);
            ed.commit();
        }
    }

    public String getBabyFirstName() {
        synchronized (KEY.BABY_FIRST_NAME) {
            return mPref.getString(KEY.BABY_FIRST_NAME, "");
        }
    }

    public void setBabySex(String value) {
        if (value == null) return;
        if (value.equals("null")) return;
        synchronized (KEY.BABY_SEX) {
            SharedPreferences.Editor ed = mPref.edit();
            ed.putString(KEY.BABY_SEX, value);
            ed.commit();
        }
    }

    public String getBabySex() {
        synchronized (KEY.BABY_SEX) {
            return mPref.getString(KEY.BABY_SEX, null);
        }
    }

    public void setBabyAvaProfile(String value) {
        if (value == null) return;
        if (value.equals("null")) return;
        synchronized (KEY.BABY_AVA_PROFILLE) {
            SharedPreferences.Editor ed = mPref.edit();
            ed.putString(KEY.BABY_AVA_PROFILLE, value);
            ed.commit();
        }
    }

    public void setBabyMidleName(String value) {
        if (value == null) return;
        if (value.equals("null")) return;
        synchronized (KEY.BABY_MIDLE_NAME) {
            SharedPreferences.Editor ed = mPref.edit();
            ed.putString(KEY.BABY_MIDLE_NAME, value);
            ed.commit();
        }
    }

    public String getBabyMidleName() {
        synchronized (KEY.BABY_MIDLE_NAME) {
            return mPref.getString(KEY.BABY_MIDLE_NAME, "");
        }
    }

    public void setBabyLastName(String value) {
        if (value == null) return;
        if (value.equals("null")) return;
        synchronized (KEY.BABY_LAST_NAME) {
            SharedPreferences.Editor ed = mPref.edit();
            ed.putString(KEY.BABY_LAST_NAME, value);
            ed.commit();
        }
    }

    public String getBabyLastName() {
            synchronized (KEY.BABY_LAST_NAME) {
                return mPref.getString(KEY.BABY_LAST_NAME, "");
            }
        }

    public void setAuthToken(String value) {
        if (value == null) return;
        if (value.equals("null")) return;
        synchronized (KEY.AUTh_TOKEN) {
            SharedPreferences.Editor ed = mPref.edit();
            ed.putString(KEY.AUTh_TOKEN, value);
            ed.commit();
            BookAgooApi.getInstance().setAutoToken(value);
        }
    }

    public String getAuthToken() {
        synchronized (KEY.AUTh_TOKEN) {
            return mPref.getString(KEY.AUTh_TOKEN, null);
        }
    }

    public void setUserId(String value) {
        if (value == null) return;
        if (value.equals("null")) return;
        synchronized (KEY.USER_ID) {
            SharedPreferences.Editor ed = mPref.edit();
            ed.putString(KEY.USER_ID, value);
            ed.commit();
            mUserId = value;
        }
    }

    public String getUserId() {
        synchronized (KEY.USER_ID) {
            if (mUserId == null) {
                mUserId = mPref.getString(KEY.USER_ID, null);
            }
            return mUserId;
        }
    }

    public void setFamilyStatus(String value) {
        if (value == null) return;
        if (value.equals("null")) return;
        synchronized (KEY.FAMILY_STATUS) {
            SharedPreferences.Editor ed = mPref.edit();
            ed.putString(KEY.FAMILY_STATUS, value);
            ed.commit();
        }
    }

    public String getFamilyStatus() {
            synchronized (KEY.FAMILY_STATUS) {
                return mUserId = mPref.getString(KEY.FAMILY_STATUS, null);
            }
        }

    public void setSocialData(String value) {
        if (value == null) return;
        if (value.equals("null")) return;
        synchronized (KEY.SOCIAL_DATA) {
            SharedPreferences.Editor ed = mPref.edit();
            ed.putString(KEY.SOCIAL_DATA, value);
            ed.commit();
        }
    }

    public void setMotherAvaProfile(String value) {
        if (value == null) return;
        if (value.equals("null")) return;
        synchronized (KEY.MOTHER_AVA_PROFILE) {
            SharedPreferences.Editor ed = mPref.edit();
            ed.putString(KEY.MOTHER_AVA_PROFILE, value);
            ed.commit();
        }
    }

    public void setMotherFirstName(String value) {
        if (value == null) return;
        if (value.equals("null")) return;
        synchronized (KEY.MOTHER_FIRST_NAME) {
            SharedPreferences.Editor ed = mPref.edit();
            ed.putString(KEY.MOTHER_FIRST_NAME, value);
            ed.commit();
        }
    }

    public String getMotherFirstName() {
        synchronized (KEY.MOTHER_FIRST_NAME) {
            return mPref.getString(KEY.MOTHER_FIRST_NAME, "");
        }
    }

    public void setMotherLastName(String value) {
        if (value == null) return;
        if (value.equals("null")) return;
        synchronized (KEY.MOTHER_LAST_NAME) {
            SharedPreferences.Editor ed = mPref.edit();
            ed.putString(KEY.MOTHER_LAST_NAME, value);
            ed.commit();
        }
    }

    public String getMotherLastName() {
        synchronized (KEY.MOTHER_LAST_NAME) {
            return mPref.getString(KEY.MOTHER_LAST_NAME, "");
        }
    }

    public void setMotherBirtDateUnix(long value) {
        synchronized (KEY.MOTHER_BIRT_DATE_UNIX) {
            SharedPreferences.Editor ed = mPref.edit();
            ed.putLong(KEY.MOTHER_BIRT_DATE_UNIX, value);
            ed.commit();
        }
    }

    public long getMotherBirtDateUnix() {
        synchronized (KEY.MOTHER_BIRT_DATE_UNIX) {
            return mPref.getLong(KEY.MOTHER_BIRT_DATE_UNIX, 0);
        }
    }

    public void setFatherAvaProfile(String value) {
        if (value == null) return;
        if (value.equals("null")) return;
        synchronized (KEY.FATHER_AVA_PROFILE) {
            SharedPreferences.Editor ed = mPref.edit();
            ed.putString(KEY.FATHER_AVA_PROFILE, value);
            ed.commit();
        }
    }

    public void setFatherFirstName(String value) {
        if (value == null) return;
        if (value.equals("null")) return;
        synchronized (KEY.FATHER_FIRST_NAME) {
            SharedPreferences.Editor ed = mPref.edit();
            ed.putString(KEY.FATHER_FIRST_NAME, value);
            ed.commit();
        }

    }

    public String getFatherFirstName() {
        synchronized (KEY.FATHER_FIRST_NAME) {
            return mPref.getString(KEY.FATHER_FIRST_NAME, "");
        }
    }

    public void setFatherLastName(String value) {
        if (value == null) return;
        if (value.equals("null")) return;
        synchronized (KEY.FATHER_LAST_NAME) {
            SharedPreferences.Editor ed = mPref.edit();
            ed.putString(KEY.FATHER_LAST_NAME, value);
            ed.commit();
        }
    }

    public String getFatherLastName() {
        synchronized (KEY.FATHER_LAST_NAME) {
            return mPref.getString(KEY.FATHER_LAST_NAME, "");
        }
    }

    public void setFatherBirtDateUnix(Long value) {
        if (value == null) return;
        if (value.equals("null")) return;
        synchronized (KEY.FATHER_BIRTH_DATE_UNIX) {
            SharedPreferences.Editor ed = mPref.edit();
            ed.putLong(KEY.FATHER_BIRTH_DATE_UNIX, value);
            ed.commit();
        }
    }

    public long getFatherBirtDateUnix() {
        synchronized (KEY.FATHER_BIRTH_DATE_UNIX) {
            return mPref.getLong(KEY.FATHER_BIRTH_DATE_UNIX, 0);
        }
    }

    private static interface KEY {
        public static final String AUTh_TOKEN = "auth_token";
        public static final String USER_ID = "user_id";
        public static final String FIRST_NAME = "first_name";
        public static final String EMAIL = "email";
        public static final String ID = "id";
        public static final String BABY_BORN = "baby_born";
        public static final String BABY_FIRST_NAME = "baby_first_name";
        public static final String BABY_SEX = "baby_sex";
        public static final String BABY_BIRTH_DATE_UNIX = "baby_birth_date_unix";
        public static final String FAMILY_STATUS = "family_status";
        public static final String FATHER_BIRTH_DATE_UNIX = "father_birth_date_unix";
        public static final String FATHER_LAST_NAME = "father_last_name";
        public static final String FATHER_FIRST_NAME = "father_first_name";
        public static final String FATHER_AVA_PROFILE = "father_ava_profile";
        public static final String MOTHER_BIRT_DATE_UNIX = "mother_birt_date_unix";
        public static final String MOTHER_LAST_NAME = "mother_last_name";
        public static final String MOTHER_FIRST_NAME = "mother_first_name";
        public static final String MOTHER_AVA_PROFILE = "mother_ava_profile";
        public static final String BABY_LAST_NAME = "baby_last_name";
        public static final String BABY_MIDLE_NAME = "baby_midle_name";
        public static final String BABY_AVA_PROFILLE = "baby_ava_profile";
        public static final String SOCIAL_DATA = "social_data";
    }

}
