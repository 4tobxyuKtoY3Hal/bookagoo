package com.itech.bookagoo.tool;

import android.content.Context;
import com.itech.bookagoo.App;

/**
 * Created by Artem on 19.05.14.
 */
public class Toast {
    public static void show(int stringId){
        Toast.show(stringId, android.widget.Toast.LENGTH_SHORT);
    }
    public static void show(int stringId, int duration){
        Context cnx = App.getContext();
        String mess = cnx.getString(stringId);
        android.widget.Toast.makeText(cnx, mess, duration).show();
    }
}
