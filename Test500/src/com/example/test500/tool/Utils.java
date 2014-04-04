package com.example.test500.tool;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;
import android.widget.RadioButton;

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

}
