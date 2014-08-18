package com.itech.bookagoo.service;

import android.app.IntentService;
import android.content.Intent;
import com.itech.bookagoo.App;
import com.itech.bookagoo.tool.Log;
import com.itech.bookagoo.tool.errors.ApiException;
import com.itech.bookagoo.tool.errors.NetworkDisabledException;
import com.itech.bookagoo.work.BookAgooApi;
import com.itech.bookagoo.work.Profile;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

/**
 * Created by Artem on 21.05.14.
 */
public class ApiService extends IntentService {

    private static final String LOG_TAG = ApiService.class.getName();
    public static final String COMMAND = "command";
    public static final String IM = "im";
    public static final String FIRST_NAME ="first_name";
    public static final String EMAIL = "email";
    public static final String PASS ="pass";
    public static final String NEW_PASS = "new_pass";
    public static final String RESULT = "result";

    public static final String ACTION_RESULT = "com.itech.bookagoo.ACTION_RESULT";

    public static interface COMMANDS {
        public static final int GET_WALL = 0;
        public static final int PUT_USER_DATA = 1;
        public static final int POST_WALL = 2;
        public static final int GET_USER_DATA = 3;
    }

    public static interface QWERYS {
        public static final int GENERAL = 0;
        public static final int BABY = 1;
        public static final int FAMILY = 2;
    }

    public ApiService() {
        super("ApiService");
    }

    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(LOG_TAG, "onHandleIntent BEGIN");

        int apiComand = intent.getIntExtra(COMMAND, -1);
        switch (apiComand) {
            case COMMANDS.GET_WALL:
                Log.i(LOG_TAG, "COMMAND -> GET_WALL");
                break;
            case COMMANDS.PUT_USER_DATA:
                Log.i(LOG_TAG, "COMMAND -> PUT_USER_DATA");




//                BookAgooApi.getInstance().putUserData(
//                        Profile.getInstance().getUserId(),
//                        intent.getStringExtra(IM),
//                        intent.getStringExtra(FIRST_NAME),
//                        intent.getStringExtra(EMAIL),
//                        intent.getStringExtra(PASS),
//                        intent.getStringExtra(NEW_PASS));
                break;
            case COMMANDS.POST_WALL:
                Log.i(LOG_TAG, "COMMAND -> POST_WALL");
                break;
            case COMMANDS.GET_USER_DATA:
                Log.i(LOG_TAG, "COMMAND -> GET_USER_DATA");

                Intent intentResponse = new Intent();
                intentResponse.setAction(ACTION_RESULT);
                intentResponse.addCategory(Intent.CATEGORY_DEFAULT);
                intentResponse.putExtra(COMMAND, COMMANDS.GET_USER_DATA);
                intentResponse.putExtra(RESULT, qGetUserData());
                App.getContext().sendBroadcast(intentResponse);

                break;

        }

        Log.d(LOG_TAG, "onHandleIntent END");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }



    public static void queryGetUserData() {
        Intent intent = new Intent(App.getContext(), ApiService.class);
        intent.putExtra(COMMAND, COMMANDS.GET_USER_DATA);
        App.getContext().startService(intent);
    }

    public static void queryPutGeneral(String im, String name, String email, String pass, String newPass) {
        Profile profile = Profile.getInstance();
        Intent intent = new Intent(App.getContext(), ApiService.class);
        intent.putExtra(COMMAND, COMMANDS.PUT_USER_DATA);
        if(im != null) intent.putExtra(IM, im);
        if(!name.equals("") && !name.equals(profile.getFirstName())) intent.putExtra(FIRST_NAME, name);
        if(!email.equals("") && !name.equals(profile.getEmail())) intent.putExtra(EMAIL, email);
        if(!pass.equals("")) intent.putExtra(PASS, pass);
        if(!newPass.equals("")) intent.putExtra(NEW_PASS, newPass);
        App.getContext().startService(intent);
    }

    private boolean qGetUserData() {

        boolean luck = false;

        try {
            BookAgooApi api = BookAgooApi.getInstance();
            Profile profile = Profile.getInstance();
            JSONObject jsObj = null;

            jsObj = api.userData(profile.getUserId());

                                            /*
                                            {
                                                "subscription_plan":{
                                                    "active":true,
                                                    "customized":false,
                                                    "space":"1073741824.0",
                                                    "valid_til_unix":1400187600,
                                                    "type":"basic",
                                                    "valid_til":"2014-05-16"
                                                },
                                                "newsletter":true,
                                                "social_data":{
                                                    "twitter":null,
                                                    "facebook":null
                                                },
                                                "pdf_status":"not_requested",
                                                "father":{
                                                    "last_name":null,
                                                    "first_name":null,
                                                    "avatar":{
                                                        "original":null,
                                                        "profile":null
                                                    },
                                                    "birth_date":null,
                                                    "birth_date_unix":null
                                                },
                                                "country":null,
                                                "total_space":0,
                                                "id":"537a73586b733130f9460100",
                                                "first_name":"Артём",
                                                "mother":{
                                                    "last_name":null,
                                                    "first_name":null,
                                                    "avatar":{
                                                        "original":null,
                                                        "profile":null
                                                    },
                                                    "birth_date":null,
                                                    "birth_date_unix":null
                                                },
                                                "address":null,
                                                "email":"art7384@gmail.com",
                                                "pdf_link":null,
                                                "payments":[],
                                                "last_name":null,
                                                "created_at":"2014-05-19T22:10:48.630+01:00",
                                                "email_notifications":true,
                                                "baby":{
                                                    "born":true,
                                                    "first_name":"Анна",
                                                    "sex":"female",
                                                    "birth_date":"1970-01-01T00:00:00.000+00:00",
                                                    "middle_name":null,
                                                    "last_name":null,
                                                    "avatar":{
                                                        "original":null,
                                                        "profile":null
                                                    },
                                                    "birth_date_unix":0,
                                                    "place_of_birth":null
                                                },
                                                "family_status":null,
                                                "created_at_unix":1400533848,
                                                "site_notifications":true
                                            }
                                            */
            Log.v(LOG_TAG, jsObj.toString());

            //                  General:
            JSONArray jsonArrPayments = jsObj.getJSONArray(BookAgooApi.JSON.PAYMENTS);
            profile.setFamilyStatus(jsObj.getString(BookAgooApi.JSON.FAMILY_STATUS));
            profile.setFirstName(jsObj.getString(BookAgooApi.JSON.FIRST_NAME));
            profile.setEmail(jsObj.getString(BookAgooApi.JSON.EMAIL));
            profile.setSocialData(jsObj.getString(BookAgooApi.JSON.SOCIAL_DATA));

            //                  Baby:
            JSONObject jsObjBaby = jsObj.getJSONObject(BookAgooApi.JSON.BABY);
            profile.setBabyBorn(jsObjBaby.getBoolean(BookAgooApi.JSON.BORN));
            JSONObject jsObjAva = jsObjBaby.getJSONObject(BookAgooApi.JSON.AVATAR);
            profile.setBabyAvaProfile(jsObjAva.getString(BookAgooApi.JSON.PROFILE));
            profile.setBabyFirstName(jsObjBaby.getString(BookAgooApi.JSON.FIRST_NAME));
            profile.setBabyMidleName(jsObjBaby.getString(BookAgooApi.JSON.MIDDLE_NAME));
            profile.setBabyLastName(jsObjBaby.getString(BookAgooApi.JSON.LAST_NAME));
            String date = jsObjBaby.getString(BookAgooApi.JSON.BIRTH_DATE_UNIX);
            if (date != null && !date.equals("null")) {
                profile.setBabyBirthDateUnix(Long.parseLong(date));
            }

            //                  Family:
            //                  MOTHER
            JSONObject jsObjMother = jsObj.getJSONObject(BookAgooApi.JSON.MOTHER);
            jsObjAva = jsObjMother.getJSONObject(BookAgooApi.JSON.AVATAR);
            profile.setMotherAvaProfile(jsObjAva.getString(BookAgooApi.JSON.PROFILE));
            profile.setMotherFirstName(jsObjMother.getString(BookAgooApi.JSON.FIRST_NAME));
            profile.setMotherLastName(jsObjMother.getString(BookAgooApi.JSON.LAST_NAME));
            date = jsObjMother.getString(BookAgooApi.JSON.BIRTH_DATE_UNIX);
            if (date != null && !date.equals("null")) {
                profile.setMotherBirtDateUnix(Long.parseLong(date));
            }
            //                  FATHER
            JSONObject jsObjFather = jsObj.getJSONObject(BookAgooApi.JSON.FATHER);
            jsObjAva = jsObjFather.getJSONObject(BookAgooApi.JSON.AVATAR);
            profile.setFatherAvaProfile(jsObjAva.getString(BookAgooApi.JSON.PROFILE));
            profile.setFatherFirstName(jsObjFather.getString(BookAgooApi.JSON.FIRST_NAME));
            profile.setFatherLastName(jsObjFather.getString(BookAgooApi.JSON.LAST_NAME));
            date = jsObjMother.getString(BookAgooApi.JSON.BIRTH_DATE_UNIX);
            if (date != null && !date.equals("null")) {
                profile.setFatherBirtDateUnix(Long.parseLong(date));
            }

            luck = true;


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NetworkDisabledException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ApiException e) {
            e.printStackTrace();
        }

        return luck;
    }

}
