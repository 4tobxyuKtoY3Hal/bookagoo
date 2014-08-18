package com.itech.bookagoo.work;

import com.itech.bookagoo.Build;
import com.itech.bookagoo.tool.FileBodyProgress;
import com.itech.bookagoo.tool.Log;
import com.itech.bookagoo.tool.Network;
import com.itech.bookagoo.tool.errors.ApiException;
import com.itech.bookagoo.tool.errors.NetworkDisabledException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by Artem on 27.03.14.
 */
public class BookAgooApi extends BaseApi {


    private static final String LOG_TAG = "BookAgooApi";
    public static final int WALL_LIMIT = 25;
    private static BookAgooApi sBookAgooApi = null;
    private static String mAutoToken = null;

    private BookAgooApi() {
        mAutoToken = Profile.getInstance().getAuthToken();
    }

    static public BookAgooApi getInstance() {
        if (sBookAgooApi == null) {
            sBookAgooApi = new BookAgooApi();
        }
        return sBookAgooApi;
    }

    public void setAutoToken(String autoToken) {
        mAutoToken = autoToken;
    }

    public JSONObject registration(String email, String password, String firstName, boolean babyBorn,
                                   String babyFirstName, String babySex, long babyBirthDateUnix)
            throws JSONException, NetworkDisabledException, URISyntaxException, ApiException {

        String url = Build.BUUK_AGOO_API_SERVER + COMMAND.USERS.command;

        HashMap<String, String> query = new HashMap<String, String>();
        query.put(PARAM.USER_EMAIL, email);
        query.put(PARAM.USER_PASSWORD, password);
        query.put(PARAM.USER_BABY_BIRTH_DATE_UNIX, Long.toString(babyBirthDateUnix));
        query.put(PARAM.USER_BABY_BORN, Boolean.toString(babyBorn));
        if (firstName != null) query.put(PARAM.USER_FIRST_NAME, firstName);
        if (babyFirstName != null) query.put(PARAM.USER_BABY_FIRST_NAME, babyFirstName);
        if (babySex != null) query.put(PARAM.USER_BABY_SEX, babySex);

        return sendCommand(url, COMMAND.USERS.method, query, null);
    }

    public JSONObject getBookPublish() throws JSONException, NetworkDisabledException,
            URISyntaxException, ApiException {

        String url = Build.BUUK_AGOO_API_SERVER + COMMAND.GET_BOOK_PUBLISH.command;

        Map<String, String> query = new HashMap<String, String>();

        Map<String, String> headers = new HashMap<String, String>();
        if (mAutoToken != null) {
            headers.put(HEADER.AUTHORIZATION, mAutoToken);
        }

        return sendCommand(url, COMMAND.GET_BOOK_PUBLISH.method, query, headers);

    }

    public JSONObject login(String email, String password) throws JSONException, NetworkDisabledException,
            URISyntaxException, ApiException {

        String url = Build.BUUK_AGOO_API_SERVER + COMMAND.USERS_SIGN_IN.command;

        Map<String, String> query = new HashMap<String, String>();
        query.put(PARAM.EMAIL, email);
        query.put(PARAM.PASSWORD, password);

        return sendCommand(url, COMMAND.USERS.method, query, null);

    }

    public JSONArray getWall(int offset, int limit) throws JSONException,
            NetworkDisabledException, URISyntaxException, ApiException {

        String url = Build.BUUK_AGOO_API_SERVER
                + String.format(COMMAND.GET_WALL.command);
        Map<String, String> headers = new HashMap<String, String>();
        if (mAutoToken != null) {
            headers.put(HEADER.AUTHORIZATION, mAutoToken);
        }
        Map<String, String> query = new HashMap<String, String>();
        query.put(PARAM.OFFSET, "" + offset);
        query.put(PARAM.LIMIT, "" + limit);


        return sendCommandArr(url, COMMAND.GET_WALL.method, query, headers);
    }


    public void putUserData(String id, String im, String firstName, String email, String pass, String newPass)
            throws JSONException, NetworkDisabledException, URISyntaxException, ApiException {

        String url = Build.BUUK_AGOO_API_SERVER
                + String.format(
                COMMAND.GET_API_USERS_ID.command,
                id);

        Map<String, String> headers = new HashMap<String, String>();
        if (mAutoToken != null) {
            headers.put(HEADER.AUTHORIZATION, mAutoToken);
        }

        Map<String, String> query = new HashMap<String, String>();
        query.put(PARAM.USER_FIRST_NAME, firstName);
        query.put(PARAM.USER_EMAIL, email);
        query.put(PARAM.USER_FAMILY_STATUS, im);

        sendPutCommand(url, COMMAND.GET_API_USERS_ID.method, query, headers);

    }

    public void putUserData(String id, String firstName, String lastName, String date, boolean isFater)
            throws JSONException, NetworkDisabledException, URISyntaxException, ApiException {

        String url = Build.BUUK_AGOO_API_SERVER
                + String.format(
                COMMAND.GET_API_USERS_ID.command,
                id);

        Map<String, String> headers = new HashMap<String, String>();
        if (mAutoToken != null) {
            headers.put(HEADER.AUTHORIZATION, mAutoToken);
        }

        Map<String, String> query = new HashMap<String, String>();
        if (isFater) {
            query.put(PARAM.USER_FATER_FIRST_NAME, firstName);
            query.put(PARAM.USER_FATER_LAST_NAME, lastName);
            query.put(PARAM.USER_FATER_BIRTH_DATE_UNIX, date);
        } else {
            query.put(PARAM.USER_MOTHER_FIRST_NAME, firstName);
            query.put(PARAM.USER_MOTHER_LAST_NAME, lastName);
            query.put(PARAM.USER_MOTHER_BIRTH_DATE_UNIX, date);
        }

        sendPutCommand(url, COMMAND.GET_API_USERS_ID.method, query, headers);

    }

    public JSONObject userData(String id) throws JSONException, NetworkDisabledException,
            URISyntaxException, ApiException {

        String url = Build.BUUK_AGOO_API_SERVER
                + String.format(COMMAND.GET_API_USERS_ID.command, id);

        Map<String, String> headers = new HashMap<String, String>();
        if (mAutoToken != null) {
            headers.put(HEADER.AUTHORIZATION, mAutoToken);
        }
        return sendCommand(url, COMMAND.GET_API_USERS_ID.method, null, headers);
    }

    public JSONObject uploads(File file) throws JSONException, NetworkDisabledException,
            URISyntaxException, ApiException {

        if (!Network.isOnline()) throw new NetworkDisabledException();

        String strUrl = Build.BUUK_AGOO_API_SERVER
                + String.format(COMMAND.API_UPLOADS.command);


        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, Network.CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParameters, Network.SOCKET_TIMEOUT);
        final HttpClient client = new DefaultHttpClient(httpParameters);

        final HttpPost httpPost = new HttpPost(strUrl);

        MultipartEntity entity = new MultipartEntity();
//        entit.addPart(PARAM.FILE, new FileBody(file));
        FileBodyProgress contentFile = new FileBodyProgress(file);
        entity.addPart(PARAM.FILE, contentFile);
        contentFile.setProgressListener(mProgressListener);
        httpPost.addHeader(HEADER.AUTHORIZATION, mAutoToken);
        httpPost.setEntity(entity);

        HttpResponse response = null;
        final HttpResponse[] _response = new HttpResponse[]{null};

        if (com.itech.bookagoo.Build.SEND_REPORT) {
            Log.i(LOG_TAG, ">>>>> QUERTY <<<<<");
            Log.i(LOG_TAG, "// URI: " + strUrl);
            Log.i(LOG_TAG, "// METOD: POST");
            Log.i(LOG_TAG, "// FLE: " + file.getAbsolutePath());

        }


        // This hack is for timout during looking up by sucking DNS
        Future<?> future = Network.TIMEOUT_EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    _response[0] = client.execute(httpPost);
                } catch (Exception e) {
                }
            }
        });

        try {
//            future.get(Network.CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
            future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
//        } catch (TimeoutException e) {
//            e.printStackTrace();
        }
        response = _response[0];

        if (response == null) return null;
        int statusCode = response.getStatusLine().getStatusCode();

        HttpEntity httpEntity = null;
        InputStream is = null;


        Log.i(LOG_TAG, ">>>>> END QUERTY <<<<<");
        Log.i(LOG_TAG, "// STATUS: " + statusCode);
        Log.i(LOG_TAG, "// MESS: " + response.getStatusLine().getReasonPhrase());

        try {
            if (statusCode != 200 && statusCode != 201) {

                httpEntity = response.getEntity();
                if (httpEntity != null) {
                    is = httpEntity.getContent();
                    if (is != null) {
                        try {
                            JSONObject jsObj = inputStreamToJSONObject(is);
                            Log.e(LOG_TAG, "// CONTENT: " + jsObj.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }


                throw new ApiException(response.getStatusLine().getReasonPhrase(), statusCode);
            }

            httpEntity = response.getEntity();

            if (httpEntity == null) return null;


            is = httpEntity.getContent();


        } catch (IOException e) {
            e.printStackTrace();
        }

        return inputStreamToJSONObject(is);
    }

    private FileBodyProgress.OnProgressListener mProgressListener = new FileBodyProgress.OnProgressListener() {
        @Override
        public void onStart(FileBodyProgress sender) {
            Log.d(LOG_TAG, "onStart uploads");
        }

        @Override
        public synchronized void onSend(FileBodyProgress sender, long loaded, long length) {

            Log.d(LOG_TAG, "loaded=" + loaded);
            Log.d(LOG_TAG, "length=" + length);
            Log.i(LOG_TAG, "uploads " + Math.round(loaded * 100 / length) + "%");

    /*
                ContentValues values = new ContentValues();
                values.put(MediaProvider.Tables.Media.PROGRESS, loaded);
                values.put(MediaProvider.Tables.Media.PROGRESS_CEIL, length);
                mUpdatesDb.put(getMediaId(), values);

                if (loaded < length) {
                    if (System.currentTimeMillis() - mLastUpdateDb < 500) {
                        return;
                    }
                }
                mLastUpdateDb = System.currentTimeMillis();
                Set<Long> arranged = new HashSet<Long>(mUpdatesDb.keySet());
                for (Long id : arranged) {
                    values = mUpdatesDb.remove(id);
                    if (values == null) continue;
                    Connection.getMediaResolver().update(
                            MediaProvider.URI_MEDIA,
                            values, MediaProvider.Tables.Media.ID + "=" +
                            id, null);
                }*/
        }

        @Override
        public void onFinish(FileBodyProgress sender) {
            Log.d(LOG_TAG, "onFinish uploads");
        }
    };


    public JSONObject postWall(String text, String[] arrId, String upload_id) throws JSONException,
            NetworkDisabledException, URISyntaxException, ApiException {

        String url = Build.BUUK_AGOO_API_SERVER
                + String.format(COMMAND.API_WALL.command);

        Map<String, String> headers = new HashMap<String, String>();
        if (mAutoToken != null) {
            headers.put(HEADER.AUTHORIZATION, mAutoToken);
        }
        Map<String, String> query = new HashMap<String, String>();

        String strArrId = "[";

        for (String id : arrId) {
            strArrId += id + ",";
        }
        strArrId.length();
        strArrId.substring(0, strArrId.length() - 1);
        strArrId += "]";

        query.put(PARAM.CONTENT_IDS, strArrId);

        if (upload_id != null) {
            query.put(PARAM.UPLOAD_ID, upload_id);
            query.put(PARAM.COMMENT, text);
        } else {
            query.put(PARAM.TEXT, text);
        }

        return sendCommand(url, COMMAND.API_WALL.method, query, headers);
    }

    public void delUploads(String upload_id) {

        String url = Build.BUUK_AGOO_API_SERVER
                + String.format(
                COMMAND.DELETE_API_UPLOADS_UPLOAD_ID.command,
                upload_id);
        Map<String, String> headers = new HashMap<String, String>();
        if (mAutoToken != null) {
            headers.put(HEADER.AUTHORIZATION, mAutoToken);
        }

    }


    public void getTariff() {
    }


    public void loadFile(File file) {
    }

    public JSONArray getAllAlbums() throws JSONException,
            NetworkDisabledException, URISyntaxException, ApiException {

        String url = Build.BUUK_AGOO_API_SERVER
                + String.format(COMMAND.GET_API_ALBUMS.command);
        Map<String, String> headers = new HashMap<String, String>();
        if (mAutoToken != null) {
            headers.put(HEADER.AUTHORIZATION, mAutoToken);
        }

        return sendCommandArr(url, COMMAND.GET_API_ALBUMS.method, null, headers);
    }

    public JSONObject getListAlbumFields(String id) throws JSONException,
            NetworkDisabledException, URISyntaxException, ApiException {
        String url = Build.BUUK_AGOO_API_SERVER
                + String.format(
                COMMAND.GET_API_ALBOMS_ID_FIELDS.command,
                id);
        Map<String, String> headers = new HashMap<String, String>();
        if (mAutoToken != null) {
            headers.put(HEADER.AUTHORIZATION, mAutoToken);
        }
        return sendCommand(url, COMMAND.GET_API_ALBOMS_ID_FIELDS.method, null, headers);
    }

    public void newAlbum(String title, String position) {
    }

    public void getAlbum() {
    }


    public void addDataAlbums(String lbum_id, String id, String wall_id) {
    }

    private static enum COMMAND {
        GET_BOOK_PUBLISH("/api/book/publish", METHOD.GET),
        USERS("/users"),
        USERS_SIGN_IN("/users/sign_in"),
        GET_API_USERS_ID("/api/users/%s", METHOD.GET),//id
        API_USERS_ID("/api/users/%s"),//id
        GET_USERS_ID_PLAN("/api/users/%s/plan", METHOD.GET),//id
        GET_USER_ID_PAYMENTS("/api/users/%s/payments", METHOD.GET),//id
        GET_USERS_ID_BABY("/api/users/%s/baby", METHOD.GET),//id
        API_WALL("/api/wall"),
        GET_WALL("/api/wall", METHOD.GET),
        API_UPLOADS("/api/uploads"),
        API_UPLOADS_UPLOAD_ID("/api/uploads/%s"),//upload_id
        DELETE_API_UPLOADS_UPLOAD_ID("/api/uploads/%s", METHOD.DELETE),//upload_id
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

    public static interface FAMILY_STATUS {
        public static final String MAMMY = "mammy";
        public static final String DADDY = "daddy";
        public static final String OTHER = "other";
    }

    public static interface TYPE {
        public static final String IMAGE = "image";
        public static final String VIDEO = "video";
        public static final String AUDIO = "audio";
    }

    public static interface SEX {
        public static final String MALE = "male";
        public static final String FEMALE = "female";
    }

    public static interface CURRENT_TYPE {
        public static final String TEXT = "text";
        public static final String VIDEO = "video";
        public static final String IMAGE = "image";
        public static final String AUDIO = "audio";
    }

    public static interface JSON {
        public static final String SUBDOMAIN ="subdomain";
        public static final String PUBLISHED ="published";
        public static final String NAME = "name";
        public static final String JPG100 = "jpg100";
        public static final String JPG50 = "jpg50";
        public static final String JPG25 = "jpg25";
        public static final String JPG = "jpg";
        public static final String MP4 = "mp4";
        public static final String MP3 = "mp3";
        public static final String POSTER = "poster";
        public static final String CONTENT = "content";
        public static final String STATUS = "status";
        public static final String ATTACHED_FILES = "attached_files";
        public static final String TITLE = "title";
        public static final String POSITION = "position";
        public static final String CONTENTS = "contents";
        public static final String CREATED_AT_UNIX = "created_at_unix";
        public static final String COMMENT = "comment";
        public static final String TEXT = "text";
        public static final String CURRENT_TYPE = "current_type";
        public static final String USER_ID = "user_id";
        public static final String AUTH_TOKEN = "auth_token";
        public static final String ADDRESS = "address";
        public static final String BABY = "baby";
        public static final String AVTAR = "avatar";
        public static final String BIRTH_DATE_UNIX = "birth_date_unix";
        public static final String BORN = "born";
        public static final String FIRST_NAME = "first_name";
        public static final String LAST_NAME = "last_name";
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
        public static final String CONTENT_IDS = "content_ids";
        public static final String USER_EMAIL = "user[email]";
        public static final String USER_FAMILY_STATUS = "user[family_status]";
        public static final String USER_PASSWORD = "user[password]";
        public static final String USER_FIRST_NAME = "user[first_name]";
        public static final String USER_BABY_BORN = "user[baby][born]";
        public static final String USER_BABY_FIRST_NAME = "user[baby][first_name]";
        public static final String USER_BABY_SEX = "user[baby][sex]";
        public static final String USER_BABY_BIRTH_DATE_UNIX = "user[baby][birth_date_unix]";
        public static final String USER_FATER_FIRST_NAME = "user[father][first_name]";
        public static final String USER_FATER_LAST_NAME = "user[father][last_name]";
        public static final String USER_FATER_BIRTH_DATE_UNIX = "user[father][birth_date_unix]";
        public static final String USER_MOTHER_FIRST_NAME = "user[mother][first_name]";
        public static final String USER_MOTHER_LAST_NAME = "user[mother][last_name]";
        public static final String USER_MOTHER_BIRTH_DATE_UNIX = "user[mother][birth_date_unix]";


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
        public static final String AUTHORIZATION = "Authorization";
    }

    public static interface SERVER {
        public static final String TEST = "http://77.120.117.47";
        public static final String RELISE = "http://bookagoo.com";
    }

}
