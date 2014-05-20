package com.itech.bookagoo.work;

import com.itech.bookagoo.Build;
import com.itech.bookagoo.tool.errors.ApiException;
import com.itech.bookagoo.tool.errors.NetworkDisabledException;
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
    private static String mAutoToken = null;

    private BookAgooApi() {
        mAutoToken = Profile.getInstance().getAutoToken();
    }

    static public BookAgooApi getInstance() {
        if (sBookAgooApi == null) {
            sBookAgooApi = new BookAgooApi();
        }
        return sBookAgooApi;
    }

    public void getAutoToken(String autoToken){
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
        if(firstName != null) query.put(PARAM.USER_FIRST_NAME, firstName);
        if(babyFirstName != null) query.put(PARAM.USER_BABY_FIRST_NAME, babyFirstName);
        if(babySex != null) query.put(PARAM.USER_BABY_SEX, babySex);

        return sendCommand(url, COMMAND.USERS.method, query, null);
    }

    public JSONObject login(String email, String password) throws JSONException, NetworkDisabledException,
                    URISyntaxException, ApiException {

        String url = Build.BUUK_AGOO_API_SERVER + COMMAND.USERS_SIGN_IN.command;

        Map<String, String> query = new HashMap<String, String>();
        query.put(PARAM.EMAIL, email);
        query.put(PARAM.PASSWORD, password);

        return sendCommand(url, COMMAND.USERS.method, query, null);

    }

    public JSONObject userData(String id) throws JSONException, NetworkDisabledException,
                    URISyntaxException, ApiException  {

        String url = Build.BUUK_AGOO_API_SERVER
                + String.format(COMMAND.GET_API_USERS_ID.command, id);

        Map<String, String> headers = new HashMap<String, String>();
        if(mAutoToken != null) {
                headers.put(HEADER.AUTHORIZATION, mAutoToken);
        }
        return sendCommand(url, COMMAND.GET_API_USERS_ID.method, null, headers);
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

    public static interface SEX {
        public static final String MALE = "male";
        public static final String FEMALE = "female";
    }

    public static interface JSON {
        public static final String USER_ID = "user_id";
        public static final String AUTH_TOKEN = "auth_token";
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
        public static final String USER_EMAIL = "user[email]";
        public static final String USER_PASSWORD = "user[password]";
        public static final String USER_FIRST_NAME = "user[first_name]";
        public static final String USER_BABY_BORN = "user[baby][born]";
        public static final String USER_BABY_FIRST_NAME = "user[baby][first_name]";
        public static final String USER_BABY_SEX = "user[baby][sex]";
        public static final String USER_BABY_BIRTH_DATE_UNIX = "user[baby][birth_date_unix]";


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
        public static final String RELISE = "http://ya.ru";
    }

}
