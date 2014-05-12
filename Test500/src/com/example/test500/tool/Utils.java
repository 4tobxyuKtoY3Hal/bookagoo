package com.example.test500.tool;

import android.app.Activity;
import android.widget.EditText;
import android.widget.RadioButton;
import com.example.test500.App;

/**
 * Created by Artem on 04.04.14.
 */
public class Utils {


    public static String editTextToString(Activity a, int id){
           EditText et = (EditText) a.findViewById(id);
           return et.getText().toString();
       }

    public static boolean radioButtonToBoolean(Activity a, int id){
           RadioButton rb = (RadioButton) a.findViewById(id);
           return rb.isChecked();
       }

    public static int pxToDp(float px) {
    	return Math.round(px / App.getContext().getResources().getDisplayMetrics().density);
    }

    public static int dpToPx(float dp) {
    	return Math.round(dp * App.getContext().getResources().getDisplayMetrics().density);
    }

}
