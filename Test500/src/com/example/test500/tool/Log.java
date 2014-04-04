package com.example.test500.tool;

import com.example.test500.Build;

/**
 * Created by Artem on 04.04.14.
 */
public class Log {

    static private final String LOG_TEST = "log_test";

    static public void d(String mess) {
        d(LOG_TEST, mess);
    }

    static public void d(String tag, String mess) {
        if (Build.LOG_TEST) {
            android.util.Log.d(tag, mess);
        }
    }

    static public void i(String mess) {
        if (Build.LOG_TEST) {
            i(LOG_TEST, mess);
        }
    }

    static public void i(String tag, String mess) {
        android.util.Log.i(tag, mess);
    }

    static public void e(String mess) {
        if (Build.LOG_TEST) {
            e(LOG_TEST, mess);
        }
    }

    static public void e(String tag, String mess) {
        android.util.Log.e(tag, mess);
    }

    static public void v(String mess) {
        if (Build.LOG_TEST) {
            v(LOG_TEST, mess);
        }
    }

    static public void v(String tag, String mess) {
        android.util.Log.v(tag, mess);
    }

}
