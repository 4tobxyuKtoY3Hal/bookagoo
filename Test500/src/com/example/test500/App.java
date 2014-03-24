package com.example.test500;

import android.app.Application;
import android.content.Context;

/**
 * Created by Artem on 18.03.14.
 */
public class App extends Application {

    private static App sInstance = null;
       private Context mContext = null;

       @Override
       public void onCreate() {
           sInstance = this;
           mContext = getApplicationContext();
       }

       public static App getInstance() {
           return sInstance;
       }

       public static Context getContext() {
           return sInstance.mContext;
       }


}
