package com.example.test500.work;

import android.os.AsyncTask;
import android.os.Handler;
import com.example.test500.Build;
import com.example.test500.tool.errors.ApiException;
import com.example.test500.tool.errors.NetworkDisabledException;
import org.apache.http.HttpException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created by Artem on 27.03.14.
 */
public class BookAgooApi extends BaseApi {

    private static final int WALL_LIMIT = 25;
    private static BookAgooApi sBookAgooApi = null;
    private static String mAuthorization = null;
    private static Handler mHandler = null;

    private static Map<Long, OnQueryListener> mMapEvent = new HashMap<Long, OnQueryListener>();

    private BookAgooApi() {
        mAuthorization = Profile.getInstance().getAuthorization();
        mHandler = new Handler();
    }

    static public BookAgooApi getInstance() {
        if (sBookAgooApi == null) {
            sBookAgooApi = new BookAgooApi();
        }
        return sBookAgooApi;
    }

    public void registration(String email, String password, OnQueryListener onQueryListener) {

        HashMap<String, String> query = new HashMap<String, String>();
        query.put(PARAM.USER_EMAIL, email);
        query.put(PARAM.USER_PASSWORD, password);


         new QueryTask(Build.BUUK_AGOO_API_SERVER + COMMAND.USERS.command,
                COMMAND.USERS.method, query, null, addEvent(onQueryListener)).execute();

    }

    public void login(String email, String password, OnQueryListener onQueryListener) {

        Map<String, String> query = new HashMap<String, String>();
        query.put(PARAM.EMAIL, email);
        query.put(PARAM.PASSWORD, password);

        QueryTask task = new QueryTask(Build.BUUK_AGOO_API_SERVER + COMMAND.USERS_SIGN_IN.command,
                COMMAND.USERS.method, query, null, addEvent(onQueryListener));
        task.execute();

    }

    public void userData(String id, OnQueryListener onQueryListener)
            throws HttpException, JSONException, NetworkDisabledException {

        String url = Build.BUUK_AGOO_API_SERVER
                + String.format(COMMAND.GET_API_USERS_ID.command, id);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(HEADER.AUTHORIZATION, mAuthorization);

        QueryTask task = new QueryTask(url, COMMAND.USERS.method, null, null, addEvent(onQueryListener));
        task.execute();
    }

    public void getTariff() {
    }

    public void getWall(int offset) {
        getWall(offset, WALL_LIMIT);
    }

    public void getWall(int offset, int limit) {
    }

    public void loadFile(File file) {
    }

    public void getListAlbum() {
    }

    public void newAlbum(String title, String position) {
    }

    public void addInWall(String text, String upload_id, String comment) {
    }

    public void getTegAlbum() {
    }

    public void getTegAllAlbums(int includeTitles, String type) {
    }

    public void addDataAlbums(String lbum_id, String id, String wall_id) {
    }

    private long addEvent(OnQueryListener onQueryListener) {
        long id = new Date().getTime();
        mMapEvent.put(id, onQueryListener);
        return id;
    }

    private class QueryTask extends AsyncTask<Void, Void, Void> {

        private String mUrl;
        private METHOD mMethod;
        private Map<String, String> mHeaders = new HashMap<String, String>();
        private Map<String, String> mQuery = null;
        private long mId;

        public QueryTask(String url,
                         METHOD method,
                         Map<String, String> query,
                         Map<String, String> headers,
                         long id) {

            mUrl = url;
            mMethod = method;
            if (query != null) {
                mQuery = new HashMap<String, String>();
                Set<String> queryKeys = query.keySet();
                for (String key : queryKeys) {
                    mQuery.put(key, query.get(key));
                }
            }

            if (headers != null) {
                Set<String> headersKeys = headers.keySet();
                for (String key : headersKeys) {
                    mHeaders.put(key, headers.get(key));
                }
            }

            mId = id;

        }

        @Override
        protected Void doInBackground(Void... params) {

            String error = null;
            JSONObject jsonObj = null;
            int code = -1;
            String mess = null;

            try {
                jsonObj = sendCommand(mUrl, mMethod, mQuery, mHeaders);
            } catch (JSONException e) {
                e.printStackTrace();
                error = OnQueryListener.ERROR.JSON_EXCEPTION;
            } catch (NetworkDisabledException e) {
                e.printStackTrace();
                error = OnQueryListener.ERROR.NETWORK_DISABLED_EXCEPTION;
            } catch (URISyntaxException e) {
                e.printStackTrace();
                error = OnQueryListener.ERROR.URI_SYNTAX_EXCEPTION;
            } catch (ApiException e) {
                code = e.getCode();
                mess = e.getLocalizedMessage();
            }

            //if(mOnQuery == null) return null;

            final long ID = mId;
            final String STR_ERROR = error;
            final JSONObject JSON_OBJ = jsonObj;
            final int CODE_ERROR = code;
            final String MESS_ERROR = mess;

            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    BookAgooApi api = BookAgooApi.getInstance();

                    if (JSON_OBJ != null) {
                        event(JSON_OBJ, ID);
                    } else if (CODE_ERROR > -1) {
                        event(CODE_ERROR, MESS_ERROR, ID);
                    } else {
                        event(STR_ERROR, ID);
                    }

                }
            });

            return null;
        }

    }

    private void event(JSONObject j, long id) {
        mMapEvent.remove(id).onCompletion(j);
    }

    private void event(String s, long id) {
        mMapEvent.remove(id).onError(s);
    }

    private void event(int cod, String s, long id) {
        mMapEvent.remove(id).onApiError(cod, s);
    }


    public interface OnQueryListener {
        public void onCompletion(JSONObject e);

        public void onError(String error);

        public void onApiError(int errorCode, String mess);

        public interface ERROR {
            public static final String JSON_EXCEPTION = "JSONException";
            public static final String NETWORK_DISABLED_EXCEPTION = "NetworkDisabledException";
            public static final String URI_SYNTAX_EXCEPTION = "URISyntaxException";
        }
    }


    private static enum COMMAND {

        USERS("/users"),
        USERS_SIGN_IN("/users/sign_in"),
        GET_API_USERS_ID("/api/users/%s", METHOD.GET),//id
        API_USERS_ID("/api/users/%s"),//id
        GET_USERS_ID_PLAN("/api/users/%s/plan", METHOD.GET),//id
        GET_USER_ID_PAYMENTS("/api/users/%s/payments", METHOD.GET),//id
        GET_USERS_ID_BABY("/api/users/%s/baby", METHOD.GET),//id
        GET_API_WALL("/api/wall", METHOD.GET),
        API_UPLOADS("/api/uploads"),
        API_UPLOADS_UPLOAD_ID("/api/uploads/%s"),//upload_id
        GET_API_UPLOADS_UPLOAD_ID("/api/uploads/%s", METHOD.GET),//upload_id
        GET_API_ALBUMS("/api/albums", METHOD.GET),
        API_ALBUMS("/api/albums"),
        GET_API_ALBOMS_ID_FIELDS("/api/albums/%s/fields", METHOD.GET),//id
        GET_API_ALBUMS_ALBUM_ID_FIELDS_ID("/api/albums/%s/fields/%s", METHOD.GET);//album_id, id

        private String command;
        private METHOD method;

        COMMAND(String command) {
            this(command, METHOD.POST);
        }

        COMMAND(String command, METHOD method) {
            this.command = command;
            this.method = method;
        }


        @Override
        public String toString() {
            return command;
        }

    }

    public static interface TYPE {
        public static final String IMAGE = "image";
        public static final String VIDEO = "video";
        public static final String AUDIO = "audio";
    }

    public static interface JSON {
        public static final String ADDRESS = "address";
        public static final String BABY = "baby";
        public static final String AVTAR = "avatar";
        public static final String BIRT_DATE_UNIX = "birth_date_unix";
        public static final String BORN = "born";
        public static final String FIRST_NAME = "first_name";
        public static final String LIST_NAME = "last_name";
        public static final String MIDDLE_NAME = "middle_name";
        public static final String SEX = "sex";
        public static final String COUNTRY = "country";
        public static final String CREATED_AT = "created_at";
        public static final String EMAIL = "email";
        public static final String FAMILY_STATUS = "family_status";
        public static final String FATHER = "father";
        public static final String ORIGINAL = "original";
        public static final String PROFILE = "profile";
        public static final String ID = "id";
        public static final String MOTHER = "mother";
        public static final String AVATAR = "avatar";
        public static final String PAYMENTS = "payments";
        public static final String SOCIAL_DATA = "social_data";

    }

    public static interface PARAM {
        public static final String USER_EMAIL = "user[email];";
        public static final String USER_PASSWORD = "user&[password]";
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
        public static final String OFFSET = "offset";
        public static final String LIMIT = "limit";
        public static final String FILE = "file";
        public static final String UPLOAD_ID = "upload_id";
        public static final String COMMENT = "comment";
        public static final String TEXT = "text";
    }

    public static interface HEADER {
        public static final String AUTHORIZATION = "authorization";
    }

    public static interface SERVER {
        public static final String TEST = "http://77.120.117.47";
        public static final String RELISE = "http://ya.ru";
    }

}
