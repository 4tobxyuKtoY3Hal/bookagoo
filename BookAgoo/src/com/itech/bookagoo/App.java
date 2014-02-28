package com.itech.bookagoo;

import android.app.Application;
import android.content.Context;

/**
 * Created by Artem on 28.02.14.
 */

public class App extends Application {

    private static App sInstance = null;
    private static Context sContext = null;

    @Override
    public void onCreate() {
        sInstance = this;
        sContext = getApplicationContext();
    }

    public static App getInstance() {
        return sInstance;
    }

    public static Context getContext() {
        return sContext;
    }


}
