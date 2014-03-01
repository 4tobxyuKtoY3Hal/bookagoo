package com.itech.bookagoo;

import android.app.Application;
import android.content.Context;
import com.itech.bookagoo.tool.Log;

/**
 * Created by Artem on 28.02.14.
 */

public class App extends Application {

    private static App sInstance = null;
    private static Context sContext = null;
    private static boolean sIsTamlet = false;

    @Override
    public void onCreate() {

        sInstance = this;
        sContext = getApplicationContext();
        sIsTamlet = sContext.getResources().getBoolean(R.bool.is_tablet);

        Log.i(">>>>> " + sContext.getString(R.string.res) + "<<<<<");

    }

    public static App getInstance() {
        return sInstance;
    }

    public static Context getContext() {
        return sContext;
    }

    public static boolean isTablet() { return sIsTamlet; }

}
