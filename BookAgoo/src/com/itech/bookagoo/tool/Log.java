package com.itech.bookagoo.tool;

import com.itech.bookagoo.Bild;

/**
 * Created by Artem on 28.02.14.
 */
public class Log {

    private static final String TAG = "BOOK aGoo TEST";

    public static void d(String msg) {
        if(!Bild.IS_RELISE){
            d(TAG, msg);
        }
    }

    public static void d(String tag, String msg) {
        if(!Bild.IS_RELISE){
            android.util.Log.d(tag, msg);
        }
    }

    public static void v(String msg) {
        if(!Bild.IS_RELISE){
            v(TAG, msg);
        }

    }

    public static void v(String tag, String msg) {
        android.util.Log.v(tag, msg);
    }

    public static void i(String msg) {
        if(!Bild.IS_RELISE){
            i(TAG, msg);
        }

    }

    public static void i(String tag, String msg) {
        android.util.Log.i(tag, msg);
    }

    public static void e(String msg) {
        if(!Bild.IS_RELISE){
                   e(TAG, msg);
               }
    }

    public static void e(String tag, String msg) {
        android.util.Log.e(tag, msg);
    }

}
