package com.itech.bookagoo.tool;

import com.itech.bookagoo.Build;

/**
 * Created by Artem on 04.04.14.
 */
public class Log {

    private static final String LOG_HEADER = "---------- Application Log -----------\n";
    private static final String LOG_FOOTER = "\n--------------------------------------";
    private static final int MAX_LOG_LENGTH = 1024 * 1024 / 2; // 1Mb

    private static StringBuilder sSessionLog = new StringBuilder(LOG_HEADER);

    private static synchronized int appendToLog(String tag, String msg) {
        try {
            msg = msg.replace("\n", "\n\t");
            msg = tag + "\t:\t" + msg + "\n";
            sSessionLog.append(msg);
            truncateLog();
            return msg.length();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static void truncateLog() {
        int dl = sSessionLog.length() - (MAX_LOG_LENGTH -
                (LOG_FOOTER.length() + LOG_HEADER.length()));
        if (dl > 0) sSessionLog.delete(0, dl);
    }

    public static synchronized String getSessionLog() {
            truncateLog();
            sSessionLog.insert(0, LOG_HEADER);
            sSessionLog.append(LOG_FOOTER);
            String log = sSessionLog.toString();
            sSessionLog.delete(0, sSessionLog.length() - 1);
            return log;
        }

    public static int d(String tag, String msg) {
        if (!Build.SEND_REPORT) return 0;
        android.util.Log.d(tag, msg);
        return appendToLog("D: " + tag, msg);
    }

    public static int i(String tag, String msg) {
        android.util.Log.i(tag, msg);
        return appendToLog("I: " + tag, msg);
    }

    public static int v(String tag, String msg) {
        android.util.Log.v(tag, msg);
        return appendToLog("V: " + tag, msg);
    }

    public static int w(String tag, String msg) {
        android.util.Log.w(tag, msg);
        return appendToLog("W: " + tag, msg);
    }

    public static int e(String tag, String msg) {
        android.util.Log.e(tag, msg);
        return appendToLog("E: " + tag, msg);
    }


}
