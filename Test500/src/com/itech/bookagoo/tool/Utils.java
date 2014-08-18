package com.itech.bookagoo.tool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    public static int calculateInSampleSize(
                BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

}
