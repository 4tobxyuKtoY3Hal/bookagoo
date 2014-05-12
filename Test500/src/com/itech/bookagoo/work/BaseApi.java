package com.itech.bookagoo.work;

import com.itech.bookagoo.tool.Log;
import com.itech.bookagoo.tool.Network;
import com.itech.bookagoo.tool.errors.ApiException;
import com.itech.bookagoo.tool.errors.NetworkDisabledException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by Artem on 29.03.14.
 */
public class BaseApi {

    protected synchronized JSONObject sendCommand(
            String command,
            METHOD method,
            Map<String, ? extends Object> query,
            Map<String, String> headers) throws JSONException, NetworkDisabledException,
            URISyntaxException, ApiException {

        InputStream is = sendCommandForStream(command, method, query, headers);
        if (is == null) return null;

        return toJSONObject(is);
    }

    private synchronized InputStream sendCommandForStream(
            String strUri, METHOD method, Map<String, ? extends Object> query,
            Map<String, String> headers)
            throws NetworkDisabledException, URISyntaxException, ApiException {


        if (!Network.isOnline()) throw new NetworkDisabledException();

        HttpEntity entity = null;
        InputStream is = null;

        try {

            if (headers == null) {
                headers = new HashMap<String, String>();
            }

            if (query == null) {
                query = new HashMap<String, String>();
            }

            String strQuery = queryListToString(query);

            HttpResponse response = null;

            if (method == METHOD.POST) {
                response = Network.post(strUri, headers, strQuery);
            } else if(method == METHOD.GET) {
                if (strQuery != null && strQuery.length() > 0)
                    strUri += "?" + strQuery;
                response = Network.get(strUri, headers);
            } else if(method == METHOD.PUT){
                //TODO дописать put запрос
                response = Network.put();
            } else if(method == METHOD.DELETE){
                //TODO дописать delete запрос
                response = Network.delete();
            } else {
                return null;
            }

            if (response == null) return null;

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != 200) {
                throw new ApiException(response.getStatusLine().getReasonPhrase(), statusCode);
            }

            Log.i(">>>>> END QUERTY <<<<<");
            Log.i("// STATUS: " + statusCode);
            Log.i("// MESS: " + response.getStatusLine().getReasonPhrase());

            entity = response.getEntity();

            if (entity == null) return null;


            is = entity.getContent();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;

    }

    private JSONObject toJSONObject(InputStream is) throws JSONException {
        String strIs = null;
        try {
            strIs = new java.util.Scanner(is).useDelimiter("\\A").next();
        } catch (java.util.NoSuchElementException e) {

        }

        return new JSONObject(strIs);
    }

    private String queryListToString(Map<String, ? extends Object> params) {
        if (params == null || params.size() == 0) return "";
        try {
            StringBuilder str = new StringBuilder();
            Set<String> keys = params.keySet();
            for (String key : keys) {
                if (key == null || key.length() == 0) continue;
                Object valVar = params.get(key);
                if (valVar instanceof Collection<?>) {
                    Collection<?> arr = (Collection<?>) valVar;
                    Iterator i = arr.iterator();
                    while (i.hasNext()) {
                        String val;
                        Object o = i.next().toString();
                        if (o != null) val = o.toString();
                        else val = "";
                        str.append(URLEncoder.encode(key, "UTF-8"));
                        str.append("=");
                        str.append(URLEncoder.encode(val, "UTF-8"));
                        str.append("&");
                    }
                } else {
                    String val = valVar.toString();
                    if (val == null) val = "";
                    str.append(URLEncoder.encode(key, "UTF-8"));
                    str.append("=");
                    str.append(URLEncoder.encode(val, "UTF-8"));
                    str.append("&");
                }
            }
            if (str.length() > 0) str.deleteCharAt(str.length() - 1);
            return str.toString();
        } catch (UnsupportedEncodingException e) {
            //aLog.wtf("UTILS", "ENCODE TO UTF8: " + params.toString());
            return null;
        }
    }

    protected static enum METHOD {
        GET,
        POST,
        PUT,
        DELETE
    }

}
