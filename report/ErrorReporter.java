package com.asd.common.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Date;
import java.util.*;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.asd.common.tools.net.ClientException;
import com.asd.common.tools.net.HttpException;
import com.asd.common.tools.net.Network;
import com.asd.common.tools.net.NetworkException;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StatFs;

public class ErrorReporter implements Thread.UncaughtExceptionHandler 
{
	public static synchronized ErrorReporter getInstance(Context c, String[] emailsTo)	{ 
		if (s_instance == null) {
			s_instance = new ErrorReporter(c);
			s_instance.init(c, emailsTo);
		}
		return s_instance;
	}
	public File[] getCrashFiles() {
		File f = new File(getDir(), "");
		File[] files = f.listFiles(
				new FilenameFilter() {
					public boolean accept (File dir, String filename) {
						return filename.endsWith(ErrorReporter.STR_CRASH_FILE_EXT);
					}
				});
		return files;
	}
	public synchronized void flashLogData() {
		try {
			if (m_logFile == null/* || !m_logFile.exists()*/) return;
			StringBuilder CustomInfo = new StringBuilder();
			for (int n = 0; n < m_customParametersCache.size(); n++) {
				CustomInfo.append(m_customParametersCache.get(n) + "\n");
				m_customParameters.add(m_customParametersCache.get(n));
			}
			CustomInfo.append(Log.getSessionLog());
			m_customParametersCache.clear();
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
		((String)null).intern();
	}
	
	public String getExceptionStackTrace(Throwable e, boolean withLog) {
		if (e == null)
			return null;
		
		String Report = "";
		Date CurDate = new Date(System.currentTimeMillis());
		Report += "Error Report collected on : " + CurDate.toString();
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
		/* ^^
		Report += "Filesystem cash size: " + LocalDataProvider.getInstance().sizeWebFilesCache() + "\n";*/
		Report += "Free memory: " + Runtime.getRuntime().freeMemory() + "\n";
		Report += "Max memory" + Runtime.getRuntime().maxMemory() + "\n";
		Report += "Total memory" + Runtime.getRuntime().totalMemory() + "\n";
		Report += "=====================\n";
		Report += "\n\n";
		
		if (withLog)
			Report += Log.getSessionLog();

		if (e != null)  { // crash report, else - log
			Report += "Stack : \n";
			Report += "======= \n";
			Writer result = new StringWriter();
			PrintWriter printWriter = new PrintWriter(result);
			e.printStackTrace(printWriter);
			//String stacktrace = result.toString();
			//Report += stacktrace;
			printWriter.append( "\n"
					+ "Cause : \n"
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
			Report += "****  End of current Report ***";
		} // if (e != null) 
		
		return Report;
	}
	
	///////////////////////////////////////////////////////////////////////
	
	
	static final String STR_CRASH_FILE_EXT = ".stacktrace";
	static final String STR_LOG_FILE_EXT = ".sessionlog";
	//static String STR_EMAIL_ADDRESS = null;
	static String[] STR_EMAIL_ADDRESSES = null;
	static String APP_NAME = "";//"1qaz@WSX3edc$RFV";
	static final int N_MAX_LOG_FILES_COUNT = 10;
	static final int N_MAX_CRASH_FILES_COUNT = 3;
	static final String ASDEVEL_BUG_REPORTER_URI = "http://asdevel.com/sandbox/scripts/mail/mail_send.php";
	static final String ASDEVEL_BUG_REPORTER_SEND_SUCCEDED_STATUS = "Message sent";

	
	File mInternalDir = null;
	File mExternalDir = null;
	
	String m_versionName;
	String m_ackageName;
	String m_filePath;
	String m_phoneModel;
	String m_androidVersion;
	String m_board;
	String m_brand;
	String m_device;
	String m_display;
	String m_fingerPrint;
	String m_host;
	String m_id;
	String m_manufacturer;
	String m_model;
	String m_product;
	String m_tags;
	long m_time;
	String m_type;
	String m_user;
	long m_sdAll = 0;
	long m_sdFree = 0;
	ArrayList<String> m_customParameters = new ArrayList<String>();
	ArrayList<String> m_customParametersCache = new ArrayList<String>();
	
	static File s_errorDir = null;
	
	static File m_logFile = null;

	private Thread.UncaughtExceptionHandler m_previousHandler = null;
	private static ErrorReporter s_instance = null;
	private Context m_curContext = null;
	
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

	private synchronized void createLogFile() {
		try {
			String fileName = null;
			if (m_logFile == null) {  // start session
				fileName = writeComplitedLog();
				m_logFile = new File(fileName);
				
				checkLogFiles();
				
			} // if (m_logFile == null
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
						public boolean accept (File dir, String filename) {
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
		
	private String createCustomInfoString() 
	{
		String CustomInfo = "";
		for (int n = 0; n < m_customParameters.size(); n++) {
			CustomInfo += m_customParameters.get(n) + "\n";
		}
		return CustomInfo;
	}

	private void init(Context context,
			String[] emailsTo) {	
		
		STR_EMAIL_ADDRESSES = emailsTo;
		
		s_errorDir = getDir();
		if (s_errorDir == null) return;
		if (m_previousHandler == null) m_previousHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
		m_curContext = context;
		m_logFile = null;
		m_customParameters.clear();
		m_customParametersCache.clear();

		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			APP_NAME = context.getString(pi.applicationInfo.labelRes);
			APP_NAME += " v" + pi.versionName + "." + String.format("%04d", pi.versionCode);
			if (Log.DEBUG) APP_NAME += "D";
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
		try	{
			PackageManager pm = context.getPackageManager();
			PackageInfo pi;
			// Version
			pi = pm.getPackageInfo(context.getPackageName(), 0);
			m_versionName = pi.versionName;
			// Package name
			m_ackageName = pi.packageName;
			// Device model
			m_phoneModel = android.os.Build.MODEL;
			// Android version
			m_androidVersion = android.os.Build.VERSION.RELEASE;

			m_board = android.os.Build.BOARD;
			m_brand = android.os.Build.BRAND;
			m_device = android.os.Build.DEVICE;
			m_display = android.os.Build.DISPLAY;
			m_fingerPrint = android.os.Build.FINGERPRINT;
			m_host = android.os.Build.HOST;
			m_id = android.os.Build.ID;
			m_model = android.os.Build.MODEL;
			m_product = android.os.Build.PRODUCT;
			m_tags = android.os.Build.TAGS;
			m_time = android.os.Build.TIME;
			m_type = android.os.Build.TYPE;
			m_user = android.os.Build.USER;
			
			File dir = Environment.getExternalStorageDirectory();
			if (dir != null)
			{
				StatFs fs = new StatFs(dir.getAbsolutePath());
				if (fs != null)
				{
					m_sdAll = (long)fs.getBlockCount() * (long)fs.getBlockSize();
					m_sdFree = (long)fs.getAvailableBlocks() * (long)fs.getBlockSize();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String createInformationString()	{
		recoltInformations(m_curContext);

		String ReturnVal = "";
		ReturnVal += "Version : " + m_versionName;
		ReturnVal += "\n";
		ReturnVal += "Package : " + m_ackageName;
		ReturnVal += "\n";
		ReturnVal += "FilePath : " + m_filePath;
		ReturnVal += "\n";
		ReturnVal += "Phone Model" + m_phoneModel;
		ReturnVal += "\n";
		ReturnVal += "Android Version : " + m_androidVersion;
		ReturnVal += "\n";
		ReturnVal += "Board : " + m_board;
		ReturnVal += "\n";
		ReturnVal += "Brand : " + m_brand;
		ReturnVal += "\n";
		ReturnVal += "Device : " + m_device;
		ReturnVal += "\n";
		ReturnVal += "Display : " + m_display;
		ReturnVal += "\n";
		ReturnVal += "Finger Print : " + m_fingerPrint;
		ReturnVal += "\n";
		ReturnVal += "Host : " + m_host;
		ReturnVal += "\n";
		ReturnVal += "ID : " + m_id;
		ReturnVal += "\n";
		ReturnVal += "Model : " + m_model;
		ReturnVal += "\n";
		ReturnVal += "Product : " + m_product;
		ReturnVal += "\n";
		ReturnVal += "Tags : " + m_tags;
		ReturnVal += "\n";
		ReturnVal += "Time : " + m_time;
		ReturnVal += "\n";
		ReturnVal += "Type : " + m_type;
		ReturnVal += "\n";
		ReturnVal += "User : " + m_user;
		ReturnVal += "\n";
		ReturnVal += "Total Internal memory : " + getTotalInternalMemorySize();
		ReturnVal += "\n";
		ReturnVal += "Available Internal memory : "
				+ getAvailableInternalMemorySize();
		ReturnVal += "\n";
		ReturnVal += "Total External memory : " + m_sdAll;
		ReturnVal += "\n";
		ReturnVal += "Available External memory : " + m_sdFree;
		ReturnVal += "\n";
		ReturnVal += "\n";

		return ReturnVal;
	}
	
	private String writeComplitedLog()
	{
		Exception e = null;
		return writeComplitedLog(e);
	}

	private String writeComplitedLog(Throwable e)
	{				
		String Report = "";
		Date CurDate = new Date(System.currentTimeMillis());
		Report += "Error Report collected on : " + CurDate.toString();
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
		/* ^^
		Report += "Filesystem cash size: " + LocalDataProvider.getInstance().sizeWebFilesCache() + "\n";*/
		Report += "Free memory: " + Runtime.getRuntime().freeMemory() + "\n";
		Report += "Max memory" + Runtime.getRuntime().maxMemory() + "\n";
		Report += "Total memory" + Runtime.getRuntime().totalMemory() + "\n";
		Report += "=====================\n";
		Report += "\n\n";
		Report += Log.getSessionLog();

		if (e != null)  { // crash report, else - log
			Report += "Stack : \n";
			Report += "======= \n";
			Writer result = new StringWriter();
			PrintWriter printWriter = new PrintWriter(result);
			e.printStackTrace(printWriter);
			//String stacktrace = result.toString();
			//Report += stacktrace;
			printWriter.append( "\n"
					+ "Cause : \n"
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
			Report += "****  End of current Report ***";
		} // if (e != null) 
		

		Calendar c = Calendar.getInstance();
		String strDate = String.format("%td%tm%tY_%tk%tM%tS%tl", 
				c, c, c, c, c, c, c);
		String fileName = APP_NAME + "_" + strDate 
				+ (e == null ? STR_LOG_FILE_EXT : STR_CRASH_FILE_EXT);
		return saveAsFile(Report, fileName);
	}
	
	public void uncaughtException(Thread t, Throwable e) 
	{
		System.gc();

		e.printStackTrace();
		
//		flashLogData();
		writeComplitedLog(e);

		if (this != m_previousHandler) {
			m_previousHandler.uncaughtException(t, e); 
			Thread.setDefaultUncaughtExceptionHandler(m_previousHandler);
		}
	}

	private String saveAsFile(String ErrorContent, String fileName) 
	{
		try 
		{
			File dir = getDir();
			if (dir == null) return null;
			File f = new File(dir, fileName);
			if(f.createNewFile())
			{
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
		HashMap<String, String> headers = new HashMap<String, String>();
		try {
			ArrayList<BasicNameValuePair> postData = new ArrayList<BasicNameValuePair>();
			postData.add(new BasicNameValuePair("theme", theme));
			postData.add(new BasicNameValuePair("dest", address));
			postData.add(new BasicNameValuePair("body", body));
			HttpEntity response = Network.post(uri, headers, postData);
			result = true;
			if (response != null) { 
				try {
					String responseString = new String(EntityUtils.toString(response, HTTP.UTF_8));
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
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (NetworkException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} 
		
		return result;
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
			while((len = reader.read(buf)) > 0){
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
        	case 0: // only delete crush logs
        	case 1: // send and delete crush logs

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
					// remove all
//	    			try {
//	    				File[] files = getCrashFiles();
//	    				if (files == null) return null;
//	    				for (File file : files) {
//	    					file.delete();
//	    				}
//	    			} catch (Exception e) {
//	    			}
					// remove OLD crashlogs
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
				} // finally
	        	break; // case 1:
	        	
        	case 2: // send this session log
        		try {
        			synchronized (m_logFile) {
	        			if (m_logFile == null 
	        					|| !m_logFile.exists()) break;
	        			trySendSessionLog(m_logFile);	        			
        			}
        		} catch (Exception e) {
        			
        		}
        		break;
        	} // switch (type)
        	return null;
        } // do in background function

        protected void onPostExecute(Void x) {
        }
    } // class SendingTask


	
}
