package com.asd.europaplustv;


import android.app.Application;
import android.content.Context;
import com.asd.common.tools.ErrorReporter;
import com.asd.common.tools.Log;
import com.asd.europaplustv.tool.AnalyticsExceptionParser;
import com.asd.europaplustv.tool.Base64;
import com.asd.europaplustv.tool.EPImageDownloader;
import com.asd.europaplustv.tool.Utils;
import com.asd.europaplustv.work.Connection;
import com.asd.europaplustv.work.Prefs;
import com.asd.europaplustv.work.commands.CommandManager;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.ExceptionReporter;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;


public class EuropaPlusTVApplication extends Application {
    //	private static final String[] STR_EMAIL_ADDRESSES = { "reports@asdevel.com" };
//	private static final String[] STR_EMAIL_ADDRESSES = { "dnosipov73@gmail.com" };
    private static final String[] STR_EMAIL_ADDRESSES =
            CommonDefs.RELEASE ?
            new String[] {"reports@asdevel.com"} :
            new String[] {"dnosipov73@gmail.com"};
    private static final int IMAGES_DISC_CACHE_MAX_SIZE = 100 * 1024 * 1024; // 100 mb

    private static ErrorReporter sErrorReporter = null;

    public static ErrorReporter getErrorReporter() {
        return sErrorReporter;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (CommonDefs.ENABLE_STATISTIC) {
            // Change uncaught exception parser...
            // Note: Checking uncaughtExceptionHandler type can be useful if clearing ga_trackingId during development to disable analytics - avoid NullPointerException.
            EasyTracker.getInstance(this);
            Thread.UncaughtExceptionHandler uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
            if (uncaughtExceptionHandler instanceof ExceptionReporter) {
                Log.i("Profiling", "set GA ExceptionHandler parser.");
                ExceptionReporter exceptionReporter = (ExceptionReporter) uncaughtExceptionHandler;
                exceptionReporter.setExceptionParser(new AnalyticsExceptionParser());
            }
        }

        Connection.setContext(getApplicationContext());
        CommandManager.sharedManager();
        Prefs.sIsTabletDevice = getResources().getBoolean(R.bool.is_tablet);
        initImageLoader(getApplicationContext());
        sErrorReporter = ErrorReporter.getInstance(getApplicationContext(), STR_EMAIL_ADDRESSES);
//		sErrorReporter.__test_crash();
        sErrorReporter.processReports(1);

//		configureCachePath();		

//		long availableMemory = Runtime.getRuntime().maxMemory();
//		double availableMemoryMb = (availableMemory / 1024.0 / 1024.0);
//		Log.i("MemoryDump", "Max heap size: " + availableMemoryMb + " mb.");		
    }

    @Override
    public void onLowMemory() {
        Log.i("LowMemory", "App>>> Low memory");
        System.gc();
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        StringBuilder authentication = new StringBuilder().append("api").append(":").append(Utils.simplePassword());
        String auth = Base64.encodeBytes(authentication.toString().getBytes());

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
//				.threadPriority(Thread.NORM_PRIORITY - 2)
                .threadPriority(Thread.MIN_PRIORITY)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
//				.writeDebugLogs() // Remove for release app
                .imageDownloader(new EPImageDownloader(context, "Basic " + auth))
                .discCacheSize(IMAGES_DISC_CACHE_MAX_SIZE)
                .memoryCache(new WeakMemoryCache())
//				.memoryCache(new RecycleableMemoryCache(10*1024*1024))
//				.memoryCache(new EPLruMemoryCache(10*1024*1024))
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

//	protected File extStorageAppBasePath;
//    protected File extStorageAppCachePath;
//	private void configureCachePath() {
//        // Check if the external storage is writeable
//        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
//        {
// 
//            // Retrieve the base path for the application in the external storage
//            File externalStorageDir = Environment.getExternalStorageDirectory();
// 
//            if (externalStorageDir != null)
//            {
//                // {SD_PATH}/Android/data/com.devahead.androidwebviewcacheonsd
//                extStorageAppBasePath = new File(externalStorageDir.getAbsolutePath() +
//                    File.separator + "Android" + File.separator + "data" +
//                    File.separator + getPackageName());
//            }
// 
//            if (extStorageAppBasePath != null)
//            {
//                // {SD_PATH}/Android/data/com.devahead.androidwebviewcacheonsd/cache
//                extStorageAppCachePath = new File(extStorageAppBasePath.getAbsolutePath() +
//                    File.separator + "cache");
// 
//                boolean isCachePathAvailable = true;
// 
//                if (!extStorageAppCachePath.exists())
//                {
//                    // Create the cache path on the external storage
//                    isCachePathAvailable = extStorageAppCachePath.mkdirs();
//                }
// 
//                if (!isCachePathAvailable)
//                {
//                    // Unable to create the cache path
//                    extStorageAppCachePath = null;
//                }
//            }
//        }
//	}

//    @Override
//    public File getCacheDir()
//    {
//        // NOTE: this method is used in Android 2.2 and higher
// 
//        if (extStorageAppCachePath != null)
//        {
//            // Use the external storage for the cache
//            Log.e("WebViewCache", "extStorageAppCachePath = " + extStorageAppCachePath);
//            return extStorageAppCachePath;
//        }
//        else
//        {
//            // /data/data/com.devahead.androidwebviewcacheonsd/cache
//            return super.getCacheDir();
//        }
//    }

//	@Override
//    public File getCacheDir()
//    {
//		Log.e("OfflineApp", "extStorageAppCachePath = " + getOfflineCacheDir());
//		return getOfflineCacheDir();
//    }
//	
//	public File getOfflineCacheDir() {
//		File dir = getExternalStorage();
//		if (dir == null) 
//			return null;
//		dir = new File(dir, "testCache"); 
//		if (!dir.exists()) 
//			if (!dir.mkdirs()) return null;
//		if (!dir.canWrite()) return null;
//		return dir;
//	}
//	
//	//// PRIVATE	
//	private File getExternalStorage() {
//		File dir = getApplicationContext().getExternalFilesDir(null);
//		if (dir == null)
//			dir = getApplicationContext().getCacheDir();
//		return dir;
//	}
}
