package com.itech.bookagoo;

import android.app.Application;
import android.content.Context;
import com.itech.bookagoo.tool.ErrorReporter;

/**
 * Created by Artem on 18.03.14.
 */
public class App extends Application {

    private static App sInstance = null;
    private Context mContext = null;
    private static ErrorReporter sErrorReporter = null;

    public static ErrorReporter getErrorReporter() {
        return sErrorReporter;
    }

    @Override
    public void onCreate() {
        sInstance = this;
        mContext = getApplicationContext();
        sErrorReporter = ErrorReporter.getInstance(getApplicationContext(), Build.ARR_REPORT_EMAIL);
        sErrorReporter.processReports(ErrorReporter.PRCESS.SEND_AND_DELETE);
    }

    public static App getInstance() {
        return sInstance;
    }

    public static Context getContext() {
        return sInstance.mContext;
    }
}



