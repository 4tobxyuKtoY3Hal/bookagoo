package com.itech.bookagoo.service;

import android.app.IntentService;
import android.content.Intent;
import com.itech.bookagoo.tool.Log;

/**
 * Created by Artem on 21.05.14.
 */
public class ApiService extends IntentService {

    private final String LOG_TAG = "ApiService";
    public final String COMMAND = "command";

    public static interface COMMANDS {
        public static final int GET_WALL = 0;
        public static final int PUT_USER_DATA = 1;
        public static final int POST_WALL = 2;
    }

    public ApiService() {
        super("ApiService");
    }

    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(LOG_TAG, "onHandleIntent BEGIN");

        int apiComand = intent.getIntExtra(COMMAND, -1);
        switch (apiComand){
            case COMMANDS.GET_WALL:
                Log.i(LOG_TAG, "COMMAND -> GET_WALL");
            break;
            case COMMANDS.PUT_USER_DATA:
                Log.i(LOG_TAG, "COMMAND -> PUT_USER_DATA");
            break;
            case COMMANDS.POST_WALL:
                Log.i(LOG_TAG, "COMMAND -> POST_WALL");
            break;

        }

        Log.d(LOG_TAG, "onHandleIntent END");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }
}
