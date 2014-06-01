package com.itech.bookagoo.tool;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StatFs;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.*;

/**
 * Created by Artem on 22.05.14.
 */
public class ErrorReporter implements Thread.UncaughtExceptionHandler {

    static final String STR_CRASH_FILE_EXT = ".stacktrace";
    static final String STR_LOG_FILE_EXT = ".sessionlog";
    //static String STR_EMAIL_ADDRESS = null;
    static String[] STR_EMAIL_ADDRESSES = null;
    static String APP_NAME = "";
    static final int N_MAX_LOG_FILES_COUNT = 10;
    static final int N_MAX_CRASH_FILES_COUNT = 3;
    static final String ASDEVEL_BUG_REPORTER_URI = null;
    static final String ASDEVEL_BUG_REPORTER_SEND_SUCCEDED_STATUS = "Message sent";

    File mInternalDir = null;
    File mExternalDir = null;

    String mVersionName;
    String mAckageName;
    String mFilePath;
    String mPhoneModel;
    String mAndroidVersion;
    String mBoard;
    String mBrand;
    String mDevice;
    String mDisplay;
    String mFingerPrint;
    String mHost;
    String mId;
    String m_manufacturer;
    String mModel;
    String mProduct;
    String mTags;
    long mTime;
    String mType;
    String mUser;
    long mSdAll = 0;
    long mSdFree = 0;
    ArrayList<String> mCustomParameters = new ArrayList<String>();
    ArrayList<String> mCustomParametersCache = new ArrayList<String>();

    static File sErrorDir = null;

    static File m_logFile = null;

    private Thread.UncaughtExceptionHandler mPreviousHandler = null;
    private static ErrorReporter sInstance = null;
    private Context mCurContext = null;

    private ErrorReporter(Context c) {
        mInternalDir = c.getCacheDir();
        if (mInternalDir != null) {
            mInternalDir = new File(mInternalDir, "traces");
            if (mInternalDir != null)
                mInternalDir.mkdirs();
        }
        mExternalDir = c.getExternalCacheDir();
        if (mExternalDir != null) {
            mExternalDir = new File(mExternalDir, "traces");
            if (mExternalDir != null)
                mExternalDir.mkdirs();
        }
    }

    public static synchronized ErrorReporter getInstance(Context c, String[] emailsTo) {
        if (sInstance == null) {
            sInstance = new ErrorReporter(c);
            sInstance.init(c, emailsTo);
        }
        return sInstance;
    }

    public File[] getCrashFiles() {
        File f = new File(getDir(), "");
        File[] files = f.listFiles(
                new FilenameFilter() {
                    public boolean accept(File dir, String filename) {
                        return filename.endsWith(ErrorReporter.STR_CRASH_FILE_EXT);
                    }
                });
        return files;
    }

    public synchronized void flashLogData() {
        try {
            if (m_logFile == null) return;
            StringBuilder CustomInfo = new StringBuilder();
            for (int n = 0; n < mCustomParametersCache.size(); n++) {
                CustomInfo.append(mCustomParametersCache.get(n) + "\n");
                mCustomParameters.add(mCustomParametersCache.get(n));
            }
            CustomInfo.append(Log.getSessionLog());
            mCustomParametersCache.clear();
            FileOutputStream trace = new FileOutputStream(m_logFile, false);
            trace.write(CustomInfo.toString().getBytes());
            trace.close();

        } catch (Exception e) {

        }
    }

    public boolean wasCrash() {
        File[] ff = getCrashFiles();
        if (ff == null || ff.length == 0) return false;
        return true;
    }

    public static void __test_crash() {
        //		if (!Log.DEBUG) return;
        ((String) null).intern();
    }

    public String getExceptionStackTrace(Throwable e, boolean withLog) {
        if (e == null)
            return null;

        String Report = "";
        Date CurDate = new Date(System.currentTimeMillis());
        Report += "Error Report collected on: " + CurDate.toString();
        Report += "\n";
        Report += "\n";
        Report += "Informations :";
        Report += "\n";
        Report += "==============";
        Report += "\n";
        Report += "\n";
        Report += createInformationString();

        Report += "Custom Informations :\n";
        Report += "=====================\n";
        Report += createCustomInfoString();
        Report += "=====================\n";

        Report += "Free memory: " + Runtime.getRuntime().freeMemory() + "\n";
        Report += "Max memory" + Runtime.getRuntime().maxMemory() + "\n";
        Report += "Total memory" + Runtime.getRuntime().totalMemory() + "\n";
        Report += "=====================\n";
        Report += "\n\n";

        if (withLog)
            Report += Log.getSessionLog();

        if (e != null) { // crash report, else - log
            Report += "Stack: \n";
            Report += "======= \n";
            Writer result = new StringWriter();
            PrintWriter printWriter = new PrintWriter(result);
            e.printStackTrace(printWriter);

            printWriter.append("\n"
                    + "Cause: \n"
                    + "======= \n");

            Throwable cause = e.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            Report += result.toString();
            printWriter.close();
            Report += "********** End of current Report **********";
        }

        return Report;
    }

    private synchronized void createLogFile() {
        try {
            if (m_logFile == null) {  // start session
                String fileName = writeComplitedLog();
                m_logFile = new File(fileName);

                checkLogFiles();

            }
        } catch (Exception e) {
            // do nothing
        }
    }

    private File getDir() {
        if (mExternalDir != null && mExternalDir.exists())
            return mExternalDir;
        if (mInternalDir != null && mInternalDir.exists())
            return mInternalDir;
        return null;
    }

    private synchronized void checkLogFiles() {
        try {
            File f = getDir();
            if (f == null) return;
            // remove old logs
            File[] files = f.listFiles(
                    new FilenameFilter() {
                        public boolean accept(File dir, String filename) {
                            return filename.endsWith(STR_LOG_FILE_EXT);
                        }
                    });
            if (files == null || files.length <= N_MAX_LOG_FILES_COUNT)
                return;
            // get modif times
            SortedMap<Long, File> modifFiles = new TreeMap<Long, File>();
            for (File log : files) {
                modifFiles.put(log.lastModified(), log);
            }
            Iterator<Long> iterator = modifFiles.keySet().iterator();
            int n = files.length;
            while (iterator.hasNext()) {
                Object key = iterator.next();
                if (n >= N_MAX_LOG_FILES_COUNT)
                    modifFiles.get(key).delete();
                n--;
            }

        } catch (Exception e) {

        }
    }

    private String createCustomInfoString() {
        String CustomInfo = "";
        for (int n = 0; n < mCustomParameters.size(); n++) {
            CustomInfo += mCustomParameters.get(n) + "\n";
        }
        return CustomInfo;
    }

    private void init(Context context,
                      String[] emailsTo) {

        STR_EMAIL_ADDRESSES = emailsTo;

        sErrorDir = getDir();
        if (sErrorDir == null) return;
        if (mPreviousHandler == null) mPreviousHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        mCurContext = context;
        m_logFile = null;
        mCustomParameters.clear();
        mCustomParametersCache.clear();

        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            APP_NAME = context.getString(pi.applicationInfo.labelRes);
            APP_NAME += " v" + pi.versionName + "." + String.format("%04d", pi.versionCode);
//            if (Log.DEBUG) APP_NAME += "D";
        } catch (Exception e) {
            e.printStackTrace();
        }

        createLogFile();
    }

    private long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    private long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    private void recoltInformations(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi;
            // Version
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            mVersionName = pi.versionName;
            // Package name
            mAckageName = pi.packageName;
            // Device model
            mPhoneModel = android.os.Build.MODEL;
            // Android version
            mAndroidVersion = android.os.Build.VERSION.RELEASE;

            mBoard = android.os.Build.BOARD;
            mBrand = android.os.Build.BRAND;
            mDevice = android.os.Build.DEVICE;
            mDisplay = android.os.Build.DISPLAY;
            mFingerPrint = android.os.Build.FINGERPRINT;
            mHost = android.os.Build.HOST;
            mId = android.os.Build.ID;
            mModel = android.os.Build.MODEL;
            mProduct = android.os.Build.PRODUCT;
            mTags = android.os.Build.TAGS;
            mTime = android.os.Build.TIME;
            mType = android.os.Build.TYPE;
            mUser = android.os.Build.USER;

            File dir = Environment.getExternalStorageDirectory();
            if (dir != null) {
                StatFs fs = new StatFs(dir.getAbsolutePath());
                if (fs != null) {
                    mSdAll = (long) fs.getBlockCount() * (long) fs.getBlockSize();
                    mSdFree = (long) fs.getAvailableBlocks() * (long) fs.getBlockSize();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String createInformationString() {
        recoltInformations(mCurContext);

        String ReturnVal = "";
        ReturnVal += "Version : " + mVersionName;
        ReturnVal += "\n";
        ReturnVal += "Package : " + mAckageName;
        ReturnVal += "\n";
        ReturnVal += "FilePath : " + mFilePath;
        ReturnVal += "\n";
        ReturnVal += "Phone Model" + mPhoneModel;
        ReturnVal += "\n";
        ReturnVal += "Android Version : " + mAndroidVersion;
        ReturnVal += "\n";
        ReturnVal += "Board : " + mBoard;
        ReturnVal += "\n";
        ReturnVal += "Brand : " + mBrand;
        ReturnVal += "\n";
        ReturnVal += "Device : " + mDevice;
        ReturnVal += "\n";
        ReturnVal += "Display : " + mDisplay;
        ReturnVal += "\n";
        ReturnVal += "Finger Print : " + mFingerPrint;
        ReturnVal += "\n";
        ReturnVal += "Host : " + mHost;
        ReturnVal += "\n";
        ReturnVal += "ID : " + mId;
        ReturnVal += "\n";
        ReturnVal += "Model : " + mModel;
        ReturnVal += "\n";
        ReturnVal += "Product : " + mProduct;
        ReturnVal += "\n";
        ReturnVal += "Tags : " + mTags;
        ReturnVal += "\n";
        ReturnVal += "Time : " + mTime;
        ReturnVal += "\n";
        ReturnVal += "Type : " + mType;
        ReturnVal += "\n";
        ReturnVal += "User : " + mUser;
        ReturnVal += "\n";
        ReturnVal += "Total Internal memory: " + getTotalInternalMemorySize();
        ReturnVal += "\n";
        ReturnVal += "Available Internal memory: " + getAvailableInternalMemorySize();
        ReturnVal += "\n";
        ReturnVal += "Total External memory: " + mSdAll;
        ReturnVal += "\n";
        ReturnVal += "Available External memory : " + mSdFree;
        ReturnVal += "\n";
        ReturnVal += "\n";

        return ReturnVal;
    }

    private String writeComplitedLog() {
        Exception e = null;
        return writeComplitedLog(e);
    }

    private String writeComplitedLog(Throwable e) {
        String Report = "";
        Date CurDate = new Date(System.currentTimeMillis());
        Report += "Error Report collected on: " + CurDate.toString();
        Report += "\n";
        Report += "\n";
        Report += "Informations:";
        Report += "\n";
        Report += "==============";
        Report += "\n";
        Report += "\n";
        Report += createInformationString();

        Report += "Custom Informations:\n";
        Report += "=====================\n";
        Report += createCustomInfoString();
        Report += "=====================\n";
            /* ^^
            Report += "Filesystem cash size: " + LocalDataProvider.getInstance().sizeWebFilesCache() + "\n";*/
        Report += "Free memory: " + Runtime.getRuntime().freeMemory() + "\n";
        Report += "Max memory" + Runtime.getRuntime().maxMemory() + "\n";
        Report += "Total memory" + Runtime.getRuntime().totalMemory() + "\n";
        Report += "=====================\n";
        Report += "\n\n";
        Report += Log.getSessionLog();

        if (e != null) { // crash report, else - log
            Report += "Stack: \n";
            Report += "======= \n";
            Writer result = new StringWriter();
            PrintWriter printWriter = new PrintWriter(result);
            e.printStackTrace(printWriter);
            //String stacktrace = result.toString();
            //Report += stacktrace;
            printWriter.append("\n"
                    + "Cause: \n"
                    + "======= \n");

            // If the exception was thrown in a background thread inside
            // AsyncTask, then the actual exception can be found with getCause
            Throwable cause = e.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            Report += result.toString();
            printWriter.close();
            Report += "********* End of current Report *********";
        } // if (e != null)


        Calendar c = Calendar.getInstance();
        String strDate = String.format("%td%tm%tY_%tk%tM%tS%tl",
                c, c, c, c, c, c, c);
        String fileName = APP_NAME + "_" + strDate
                + (e == null ? STR_LOG_FILE_EXT : STR_CRASH_FILE_EXT);
        return saveAsFile(Report, fileName);
    }

    public void uncaughtException(Thread t, Throwable e) {
        System.gc();

        e.printStackTrace();

        //		flashLogData();
        writeComplitedLog(e);

        if (this != mPreviousHandler) {
            mPreviousHandler.uncaughtException(t, e);
            Thread.setDefaultUncaughtExceptionHandler(mPreviousHandler);
        }
    }

    private String saveAsFile(String ErrorContent, String fileName) {
        try {
            File dir = getDir();
            if (dir == null) return null;
            File f = new File(dir, fileName);
            if (f.createNewFile()) {
                String output = "##\n#\n"
                        + "# " + f.getAbsolutePath()
                        + "\n#\n#\n\n"
                        + ErrorContent;
                FileOutputStream trace = new FileOutputStream(f);
                trace.write(output.getBytes());
                trace.close();
            }
            return f.getAbsolutePath();
        } catch (Exception e) {
        }
        return null;
    }

    /*
     * @param type 1-send and delete 0 - delete
     */
    public void processReports(int type) {
        new SendingTask().execute(Integer.valueOf(type));
    }


    public void sendThisSessionLog() {
        flashLogData();
        new SendingTask().execute(Integer.valueOf(2));
    }

    private boolean trySendCrash(File crashFile) {
        boolean result = false;

        String theme = APP_NAME + " (android) report";

        String body = stringFromFile(crashFile);

        if (body == null || body.length() < 5)
            return false;

        // send
        if (STR_EMAIL_ADDRESSES != null) {
            for (String email : STR_EMAIL_ADDRESSES) {
                if (sendReportByAsdevelScript(theme, email, body))
                    result = true;
            }
        }

        return result;
    }

    private boolean trySendSessionLog(File sessionFile) {
        boolean result = false;
        flashLogData();
        String theme = APP_NAME + " (android) session log";

        String body = stringFromFile(sessionFile);

        if (body == null)
            return false;

        // send
        if (STR_EMAIL_ADDRESSES != null) {
            for (String email : STR_EMAIL_ADDRESSES) {
                if (sendReportByAsdevelScript(theme, email, body))
                    result = true;
            }
        }

        return result;
    }

    private boolean sendReportByAsdevelScript(String theme, String address, String body) {
        boolean result = false;
        String uri = ASDEVEL_BUG_REPORTER_URI;
        Map<String, String> headers = new HashMap<String, String>();
        try {
            ArrayList<BasicNameValuePair> postData = new ArrayList<BasicNameValuePair>();
            postData.add(new BasicNameValuePair("theme", theme));
            postData.add(new BasicNameValuePair("dest", address));
            postData.add(new BasicNameValuePair("body", body));
            HttpResponse response = Network.post(uri, headers, queryListToString(postData));

            result = true;
            if (response != null) {
                try {
                    String responseString = new String(EntityUtils.toString(response.getEntity(), HTTP.UTF_8));
                    Log.i("BugReporter", "report send result>>> " + responseString);
                    if (responseString != null) {
                        if (responseString.equalsIgnoreCase(ASDEVEL_BUG_REPORTER_SEND_SUCCEDED_STATUS))
                            result = true;
                        else
                            result = false;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return result;
    }

    private String queryListToString(ArrayList<BasicNameValuePair> params) {
        if (params == null || params.size() == 0) return "";
        try {
            StringBuilder str = new StringBuilder();

            for (BasicNameValuePair bnvp : params) {
                String key = bnvp.getName();
                String val = bnvp.getValue();
                str.append(URLEncoder.encode(key, "UTF-8"));
                str.append("=");
                str.append(URLEncoder.encode(val, "UTF-8"));
                str.append("&");
            }

            if (str.length() > 0) str.deleteCharAt(str.length() - 1);
            return str.toString();


        } catch (UnsupportedEncodingException e) {
            //aLog.wtf("UTILS", "ENCODE TO UTF8: " + params.toString());
            return null;
        }
    }

    private String stringFromFile(File file) {
        if (file == null)
            return null;
        String body = null;
        try {
            StringBuilder str = new StringBuilder();
            BufferedReader reader;
            reader = new BufferedReader(
                    new FileReader(file));
            char[] buf = new char[2048];
            int len = 0;
            while ((len = reader.read(buf)) > 0) {
                str.append(buf, 0, len);
            }
            reader.close();
            body = str.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }

    private class SendingTask extends AsyncTask<Integer, Void, Void> {
        protected synchronized Void doInBackground(Integer... x) {
            int type = x[0].intValue();
            switch (type) {
                case PRCESS.DELETE: // only delete crush logs
                case PRCESS.SEND_AND_DELETE: // send and delete crush logs

                    try {
                        File[] files = getCrashFiles();
                        if (files == null || files.length == 0) return null;
                        if (type == 1) {
                            for (File file : files) {
                                if (trySendCrash(file))
                                    file.delete();
                            }
                        }
                    } catch (Exception e) {

                    } finally {
                        try {
                            File[] files = getCrashFiles();
                            if (files != null && files.length <= N_MAX_CRASH_FILES_COUNT) {
                                // get modif times
                                SortedMap<Long, File> modifFiles = new TreeMap<Long, File>();
                                for (File log : files) {
                                    modifFiles.put(log.lastModified(), log);
                                }
                                Iterator<Long> iterator = modifFiles.keySet().iterator();
                                int n = files.length;
                                while (iterator.hasNext()) {
                                    Object key = iterator.next();
                                    if (n >= N_MAX_CRASH_FILES_COUNT)
                                        modifFiles.get(key).delete();
                                    n--;
                                }
                            }

                        } catch (Exception e) {
                        }
                    }
                    break;

                case 2:
                    try {
                        synchronized (m_logFile) {
                            if (m_logFile == null
                                    || !m_logFile.exists()) break;
                            trySendSessionLog(m_logFile);
                        }
                    } catch (Exception e) {

                    }
                    break;
            }
            return null;
        }

        protected void onPostExecute(Void x) {
        }
    }

    public static interface PRCESS {
        public static final int DELETE = 0;
        public static final int SEND_AND_DELETE = 1;
    }

}
