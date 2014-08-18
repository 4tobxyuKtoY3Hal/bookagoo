package com.itech.bookagoo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.itech.bookagoo.service.ApiService;
import com.itech.bookagoo.tool.Log;
import com.itech.bookagoo.tool.Toast;
import com.itech.bookagoo.tool.errors.ApiException;
import com.itech.bookagoo.tool.errors.NetworkDisabledException;
import com.itech.bookagoo.work.BookAgooApi;
import com.itech.bookagoo.work.Profile;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

/**
 * Created by Artem on 02.03.14.
 */
public class BookFragment extends Fragment implements MainActivity.IContentFragment {

    private static final String LOG_TAG = "BookFragment";
    private WebView mWebView;
    private View mProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_book, container, false);

        assert v != null;

        mProgress = v.findViewById(R.id.fragmentBook_View_progress);

        mWebView = (WebView) v.findViewById(R.id.fragmentBook_WebView_book);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.setInitialScale(1);
//        webView.loadUrl("http://itech-mobile.ru/");

        new BookAsyncTask().execute();

        return v;
    }

    @Override
    public int getIdTitle() {
        return R.string.title_book;
    }

    @Override
    public String getNameTitle() {
        return App.getContext().getString(R.string.title_book);
    }

    @Override
    public int getIdIco() {
        return R.drawable.ic_menu2;
    }

    @Override
    public int getIdIcoTop() {
        return R.drawable.ic_menu2_tap;
    }

//    @Override
//    public int getIdIcoBar() {
//        return R.drawable.ic_menu2;
//    }


    @Override
    public String getUrlIco() {
        return null;
    }

    private void startWebView(String strUri) {
        if (strUri != null) {
            mWebView.setVisibility(View.VISIBLE);
            mWebView.loadUrl(strUri);
        }

        mProgress.setVisibility(View.GONE);

    }

    class BookAsyncTask extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... params) {

            String strUri = null;

            try {
                BookAgooApi api = BookAgooApi.getInstance();
                JSONObject jsObj = api.getBookPublish();
                        /*
                            {
                                "subdomain":null,
                                "published":false,
                                "password":""
                                }
                        */
                Log.v(LOG_TAG, "jsObj > " + jsObj.toString());


                if (jsObj.getBoolean(BookAgooApi.JSON.PUBLISHED)) {
                    strUri = Build.BUUK_AGOO_API_SERVER + jsObj.getString(BookAgooApi.JSON.SUBDOMAIN);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NetworkDisabledException e) {
                Log.e(LOG_TAG, App.getContext().getString(R.string.no_internet_connection));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (ApiException e) {
                Log.e(LOG_TAG, "ApiException key=" + e.getCode() + "   mess: " + e.getMessage());
            }
            return strUri;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            startWebView(result);

        }

    }
}