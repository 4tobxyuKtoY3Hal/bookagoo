package com.itech.bookagoo;

import android.app.Application;
import android.content.Context;
import com.itech.bookagoo.tool.Log;

/**
 * Created by Artem on 28.02.14.
 */

public class App extends Application {

    private static App sInstance = null;
    private  Context mContext = null;
    private  boolean mIsTamlet = false;

    @Override
    public void onCreate() {

        sInstance = this;
        mContext = getApplicationContext();
        mIsTamlet = mContext.getResources().getBoolean(R.bool.is_tablet);

        Log.i(">>>>> " + mContext.getString(R.string.res) + "<<<<<");

    }

    public static App getInstance() {
        return sInstance;
    }

    public static Context getContext() {
        return sInstance.mContext;
    }

    public static boolean isTablet() { return sInstance.mIsTamlet; }

}
