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
        show(cnx, mess, duration);
    }
    public static void show(String mess){
        show(mess, android.widget.Toast.LENGTH_SHORT);
    }
    public static void show(String mess, int duration){
        show(App.getContext(), mess, duration);
    }
    public static void show(Context cnx, String mess, int duration){
        android.widget.Toast.makeText(cnx, mess, duration).show();
    }
}
