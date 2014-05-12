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

    private Profile(){
        Context context = App.getContext();
        mPref = context.getSharedPreferences("BookAgoo", context.MODE_PRIVATE);
    }

    public static Profile getInstance(){
        if(sProfile == null){
            sProfile = new Profile();
        }
        return sProfile;
    }


    public String getAuthorization(){
        return mPref.getString(VAR.AUTHORIZATION, null);
    }

    public void setAuthorization(String authorization){
            SharedPreferences.Editor ed = mPref.edit();
            ed.putString(VAR.AUTHORIZATION, authorization);
            ed.commit();
    }

    private static interface VAR {
        public static final String AUTHORIZATION = "authorization";
    }

}
