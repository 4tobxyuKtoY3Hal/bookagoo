package com.itech.bookagoo.tool;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import com.itech.bookagoo.App;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by Artem on 27.03.14.
 */
public class Network {

    private static final String LOG_TAG = Network.class.getName();
    public static final String WIFI = "WIFI";
    public static final String MOBILE = "MOBILE";
    public static final int CONNECTION_TIMEOUT = 60000;
    public static final int SOCKET_TIMEOUT = 60000;
    public static final String DEVICE_DESCRIPTION = "Android " +
            Build.VERSION.RELEASE + " (" + android.os.Build.MODEL + ")";
    public static final String USER_AGENT = DEVICE_DESCRIPTION;

    private static final ThreadPoolExecutor TIMEOUT_EXECUTOR =
            new ThreadPoolExecutor(1, 1, 500, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>());

    public static HttpResponse put(){
        return null;
    }

    public static HttpResponse delete(){
        return null;
    }

    public static HttpResponse get(String strUri, Map<String, String> headers)
            throws URISyntaxException, IOException {

        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParameters, SOCKET_TIMEOUT);
        HttpClient client = new DefaultHttpClient(httpParameters);
        URI uri = new URI(strUri);
        HttpGet get = new HttpGet(uri);
        HttpResponse responseGet = null;
        addHeaders(get, headers);
        //get.setHeader("User-Agent", USER_AGENT);

        if (com.itech.bookagoo.Build.SEND_REPORT) {
            Log.i(LOG_TAG, ">>>>> QUERTY <<<<<");
            Log.i(LOG_TAG, "// URI: " + strUri);
            Log.i(LOG_TAG, "// METOD: GET");
            Log.i(LOG_TAG, "// HEADER:");
            Set<String> keys = headers.keySet();
            for (String key : keys) {
                Log.i(LOG_TAG, "//     " + key + "=" + headers.get(key));
            }
        }

        responseGet = client.execute(get);

        return responseGet;
    }

    public static HttpResponse post(String strUri, Map<String, String> headers, String query)
            throws URISyntaxException, UnsupportedEncodingException {


        Log.d(LOG_TAG, query);

        StringEntity postEntity = null;
        if (query != null) {
            postEntity = new StringEntity(query, "UTF-8");
            postEntity.setContentType("application/x-www-form-urlencoded");
        }

        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParameters, SOCKET_TIMEOUT);
        final HttpClient client = new DefaultHttpClient(httpParameters);
        URI uri = new URI(strUri);
        final HttpPost post = new HttpPost(uri);
        addHeaders(post, headers);


        if (postEntity != null) post.setEntity(postEntity);
        HttpResponse response = null;
        final HttpResponse[] _response = new HttpResponse[]{null};
        // post.setHeader("User-Agent", USER_AGENT);

        if (com.itech.bookagoo.Build.SEND_REPORT) {
            Log.i(LOG_TAG, ">>>>> QUERTY <<<<<");
            Log.i(LOG_TAG, "// URI: " + strUri);
            Log.i(LOG_TAG, "// METOD: POST");
            Log.i(LOG_TAG, "// QUERT: " + query);
            Log.i(LOG_TAG, "// HEADER:");
            Set<String> keys = headers.keySet();
            for (String key : keys) {
                Log.i(LOG_TAG, "//     " + key + "=" + headers.get(key));
            }
        }

        // This hack is for timout during looking up by sucking DNS
        Future<?> future = TIMEOUT_EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    _response[0] = client.execute(post);
                } catch (Exception e) {
                }
            }
        });
        try {
            future.get(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
//            if (e.getCause() != null && e.getCause() instanceof Exception) {
//                Exception ex = (Exception) e.getCause();
//                throw ex;
//            }
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        response = _response[0];

        return response;

    }

    public static void addHeaders(AbstractHttpMessage request,
                                  Map<String, String> headers) {
        if (headers != null) {
            Set<String> keys = headers.keySet();
            for (String key : keys) {
                request.setHeader(key, headers.get(key));
            }
        }
    }

    public static boolean isOnline() {
        return isOnline(App.getContext());
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager _cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (_cm == null) return false;

        NetworkInfo[] _netInfo = _cm.getAllNetworkInfo();

        if (_netInfo == null) return false;

        for (NetworkInfo ni : _netInfo) {
            if (ni.getTypeName().equalsIgnoreCase(WIFI))
                if (ni.isConnected()) {

                    return true;
                }
            if (ni.getTypeName().equalsIgnoreCase(MOBILE))
                if (ni.isConnected()) {
                    return true;
                }
        }

        return false;
    }

}
