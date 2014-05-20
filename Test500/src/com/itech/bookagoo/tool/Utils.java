package com.itech.bookagoo.tool;

import com.itech.bookagoo.App;

/**
 * Created by Artem on 04.04.14.
 */
public class Utils {

    public static int pxToDp(float px) {
    	return Math.round(px / App.getContext().getResources().getDisplayMetrics().density);
    }

    public static int dpToPx(float dp) {
    	return Math.round(dp * App.getContext().getResources().getDisplayMetrics().density);
    }

    public static long dateStringToLong(String str){
        long date = 0;
        // TODO
        return date;
    }


}
