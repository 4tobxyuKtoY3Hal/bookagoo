package com.itech.bookagoo;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.itech.bookagoo.tool.Log;
import com.itech.bookagoo.tool.Toast;
import com.itech.bookagoo.tool.errors.ApiException;
import com.itech.bookagoo.tool.errors.NetworkDisabledException;
import com.itech.bookagoo.work.BookAgooApi;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URISyntaxException;

/**
 * Created by Artem on 01.06.14.
 */
public abstract class AbstrTabAddContentFragment extends Fragment implements View.OnClickListener {

    public static final String LOG_TAG = "AbstrTabAddContentFragment";

    private Handler mHandler = new Handler();
    ProgressDialog mProgressDialog = null;

    private TextView txtTagGroup;
    private ImageView imgTagGroup;
    private View mRiad1;
    private View mRiad2;
    private View mBtnOk;
    private EditText mEdDate;
    private EditText mEdLocation;


    private View cbCouple2;
    private View cbDating2;
    private View cbHowParentsMet2;
    private View cbDaddy2;
    private View cbMammy2;
    private View cbSmiling;
    private View cbBathTime;
    private View cbEngegement;
    private View cbMemories;
    private View cbHoldingHead;
    private View cbMammy;

    private static final Drawable DRAWABLE_ON = App.getContext().getResources().getDrawable(R.drawable.bt_normal);
    private static final Drawable DRAWABLE_OFF = App.getContext().getResources().getDrawable(R.drawable.bt_blocked);

    private boolean mIsOpenTagGroup = false;

    abstract public View getV();

    abstract public int getIdTitle();

    abstract public int getType();

    abstract public ContentData getContData();


    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        View v = getView();

        v.findViewById(R.id.includeAddContent_View_tagGroup).setOnClickListener(this);

        mEdDate = (EditText) v.findViewById(R.id.includeAddContent_EditText_date);
        mEdLocation = (EditText) v.findViewById(R.id.includeAddContent_EditText_location);
        mBtnOk = v.findViewById(R.id.includeAddContent_Button_ok);
        mBtnOk.setOnClickListener(this);
        txtTagGroup = (TextView) v.findViewById(R.id.includeAddContent_TextView_tagGroup);
        imgTagGroup = (ImageView) v.findViewById(R.id.includeAddContent_ImageView_tagGroup);

        mRiad1 = v.findViewById(R.id.includeAddContent_LinearLayout_riad1);
        mRiad2 = v.findViewById(R.id.includeAddContent_LinearLayout_riad2);

        cbCouple2 = v.findViewById(R.id.includeAddContent_TextView_couple2);
        cbDating2 = v.findViewById(R.id.includeAddContent_TextView_dating2);
        cbHowParentsMet2 = v.findViewById(R.id.includeAddContent_TextView_howParentsMet2);
        cbDaddy2 = v.findViewById(R.id.includeAddContent_TextView_daddy2);
        cbMammy2 = v.findViewById(R.id.includeAddContent_TextView_mammy2);
        cbSmiling = v.findViewById(R.id.includeAddContent_TextView_smiling);
        cbBathTime = v.findViewById(R.id.includeAddContent_TextView_bathTime);
        cbEngegement = v.findViewById(R.id.includeAddContent_TextView_engegement);
        cbMemories = v.findViewById(R.id.includeAddContent_TextView_memories);
        cbHoldingHead = v.findViewById(R.id.includeAddContent_TextView_holdingHead);
        cbMammy = v.findViewById(R.id.includeAddContent_TextView_mammy);

        cbCouple2.setOnClickListener(this);
        cbDating2.setOnClickListener(this);
        cbHowParentsMet2.setOnClickListener(this);
        cbDaddy2.setOnClickListener(this);
        cbMammy2.setOnClickListener(this);
        cbSmiling.setOnClickListener(this);
        cbBathTime.setOnClickListener(this);
        cbEngegement.setOnClickListener(this);
        cbMemories.setOnClickListener(this);
        cbHoldingHead.setOnClickListener(this);
        cbMammy.setOnClickListener(this);


        setIsOpenTagGroup(false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.includeAddContent_TextView_couple2:
            case R.id.includeAddContent_TextView_dating2:
            case R.id.includeAddContent_TextView_howParentsMet2:
            case R.id.includeAddContent_TextView_daddy2:
            case R.id.includeAddContent_TextView_mammy2:
            case R.id.includeAddContent_TextView_smiling:
            case R.id.includeAddContent_TextView_bathTime:
            case R.id.includeAddContent_TextView_engegement:
            case R.id.includeAddContent_TextView_memories:
            case R.id.includeAddContent_TextView_holdingHead:
            case R.id.includeAddContent_TextView_mammy:

                if (v.getBackground().equals(DRAWABLE_OFF)) {
                    v.setBackgroundDrawable(DRAWABLE_ON);
                } else {
                    v.setBackgroundDrawable(DRAWABLE_OFF);
                }

                break;
            case R.id.includeAddContent_View_tagGroup:
                setIsOpenTagGroup(!mIsOpenTagGroup);
                break;
            case R.id.includeAddContent_Button_ok:
                new PostWallAsyncTask(mHandler).execute(getContData());
                break;
        }
    }

    protected String getRealPathFromURI(Uri contentUri) {

        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = App.getContext().getContentResolver().query(contentUri,
                proj, // Which columns to return
                null,       // WHERE clause; which rows to return (all rows)
                null,       // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    private void setIsOpenTagGroup(boolean isOpen) {

        if (isOpen) {
            mRiad1.setVisibility(View.VISIBLE);
            mRiad2.setVisibility(View.VISIBLE);
            txtTagGroup.setText(App.getContext().getString(R.string.add_content_before_me));
            txtTagGroup.setTextColor(App.getContext().getResources().getColor(R.color.txt_active));
            imgTagGroup.setImageResource(R.drawable.arrow_boyyom_on);
        } else {
            mRiad1.setVisibility(View.GONE);
            mRiad2.setVisibility(View.GONE);
            txtTagGroup.setText(App.getContext().getString(R.string.add_content_select_tag_group));
            txtTagGroup.setTextColor(App.getContext().getResources().getColor(R.color.txt_passive));
            imgTagGroup.setImageResource(R.drawable.arrow_boyyom_off);


            if (!cbCouple2.getBackground().equals(DRAWABLE_OFF)) {
                cbCouple2.setBackgroundDrawable(DRAWABLE_OFF);
            }
            if (!cbDating2.getBackground().equals(DRAWABLE_OFF)) {
                cbDating2.setBackgroundDrawable(DRAWABLE_OFF);
            }
            if (!cbHowParentsMet2.getBackground().equals(DRAWABLE_OFF)) {
                cbHowParentsMet2.setBackgroundDrawable(DRAWABLE_OFF);
            }
            if (!cbDaddy2.getBackground().equals(DRAWABLE_OFF)) {
                cbDaddy2.setBackgroundDrawable(DRAWABLE_OFF);
            }
            if (!cbMammy2.getBackground().equals(DRAWABLE_OFF)) {
                cbMammy2.setBackgroundDrawable(DRAWABLE_OFF);
            }

        }

        mIsOpenTagGroup = isOpen;

    }

    protected interface TYPES {
        public static final int MEDIA = 0;
        public static final int SOUND = 1;
        public static final int TEXT = 2;
        public static final int VIDEO = 3;
        public static final int PHOTO = 4;
    }

    public class ContentData {
        public String text = null;
        public File file = null;
    }


    class PostWallAsyncTask extends AsyncTask<ContentData, Void, Void> {

        private Handler mHandler;

        public PostWallAsyncTask(Handler h) {
            mHandler = h;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            if (mProgressDialog != null) {
//                mProgressDialog.dismiss();
//            }
//            mProgressDialog = new ProgressDialog(AbstrTabAddContentFragment.this.getActivity());
//            mProgressDialog.setMessage(getString(R.string.mess_post_wall));
//            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(ContentData... params) {
            ContentData data = params[0];
            BookAgooApi api = BookAgooApi.getInstance();
            JSONObject jsObj;
            String uploadId = null;
            try {
                JSONArray jsArr = api.getAllAlbums();
                Log.v(LOG_TAG, ">" + jsArr.toString());
                /*
                [
                    {
                        "position":1,
                        "id":"534d0f7e6b733110e3080000",
                        "user_added":false,
                        "title":"Expecting me",
                        "editable":true,
                        "pinned":false
                    },{"position":2,"id":"534d0f7e6b733110e3090000","user_added":false,"title":"Who do I look like?","editable":true,"pinned":false},{"position":3,"id":"534d0f7e6b733110e30a0000","user_added":false,"title":"My family before me","editable":true,"pinned":false},{"position":4,"id":"534d0f7e6b733110e30b0000","user_added":false,"title":"Hi, here I am","editable":true,"pinned":false},{"position":5,"id":"534d0f7e6b733110e30c0000","user_added":false,"title":"Growing up","editable":true,"pinned":false},{"position":6,"id":"534d0f7e6b733110e30d0000","user_added":false,"title":"Where I live","editable":true,"pinned":false},{"position":7,"id":"534d0f7e6b733110e30e0000","user_added":false,"title":"What I love","editable":true,"pinned":false},{"position":8,"id":"534d0f7e6b733110e30f0000","user_added":false,"title":"Hurray! Party time!","editable":true,"pinned":false},{"position":9,"id":"534d0f7e6b733110e3100000","user_added":false,"title":"Time capsule","editable":true,"pinned":false}]
                 */
                String[] ids = new String[jsArr.length()];
                for (int j = 0; j < ids.length; j++) {
                    ids[j] = jsArr.getJSONObject(j).getString(BookAgooApi.JSON.ID);
                }

                if (data.file != null) {
                    jsObj = api.uploads(data.file);
                    Log.v(LOG_TAG, jsObj.toString());
                    /*
                    {
                        "id":"538ce3e9626f6f11b1730100",
                        "type":"image",
                        "url":"\/uploads\/538cbc42626f6f11b1bb0000\/tmp\/1401742313v6nwuiik-IMAG0146.jpg",
                        "size":666845,
                        "name":"1401742313v6nwuiik-IMAG0146.jpg"
                        }
                     */
                    uploadId = jsObj.getString(BookAgooApi.JSON.ID);
                }


                jsObj = api.postWall(data.text, ids, uploadId);
                Log.v(LOG_TAG, ">" + jsObj.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NetworkDisabledException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.show(R.string.no_internet_connection);
                    }
                });
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (ApiException e) {
                Log.e(LOG_TAG, "ApiException key=" + e.getCode() + "   mess: " + e.getMessage());
            }

            if (uploadId != null) {
//                TODO удоляем файл
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        }

    }

    class GetAllAlbumsAsyncTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            BookAgooApi api = BookAgooApi.getInstance();
            String[] ids = null;
            try {
                JSONArray jsArr = api.getAllAlbums();
                Log.v(LOG_TAG, ">" + jsArr.toString());
                        /*
                        [
                            {
                                "position":1,
                                "id":"534d0f7e6b733110e3080000",
                                "user_added":false,
                                "title":"Expecting me",
                                "editable":true,
                                "pinned":false
                            },{
                                "position":2,
                                "id":"534d0f7e6b733110e3090000",
                                "user_added":false,
                                "title":"Who do I look like?",
                                "editable":true,
                                "pinned":false
                            },{
                                "position":3,
                                "id":"534d0f7e6b733110e30a0000",
                                "user_added":false,
                                "title":"My family before me",
                                "editable":true,
                                "pinned":false
                            },{
                                "position":4,
                                "id":"534d0f7e6b733110e30b0000",
                                "user_added":false,
                                "title":"Hi, here I am",
                                "editable":true,
                                "pinned":false
                            },{
                                "position":5,
                                "id":"534d0f7e6b733110e30c0000",
                                "user_added":false,
                                "title":"Growing up",
                                "editable":true,
                                "pinned":false
                            },{
                                "position":6,
                                "id":"534d0f7e6b733110e30d0000",
                                "user_added":false,
                                "title":"Where I live",
                                "editable":true,
                                "pinned":false
                            },{
                                "position":7,
                                "id":"534d0f7e6b733110e30e0000",
                                "user_added":false,
                                "title":"What I love",
                                "editable":true,
                                "pinned":false
                            },{
                                "position":8,
                                "id":"534d0f7e6b733110e30f0000",
                                "user_added":false,
                                "title":"Hurray! Party time!",
                                "editable":true,"pinned":false
                            },{
                                "position":9,
                                "id":"534d0f7e6b733110e3100000",
                                "user_added":false,
                                "title":"Time capsule",
                                "editable":true,
                                "pinned":false
                            }
                        ]
                         */
                ids = new String[jsArr.length()];
                for (int j = 0; j < ids.length; j++) {
                    ids[j] = jsArr.getJSONObject(j).getString(BookAgooApi.JSON.ID);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NetworkDisabledException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.show(R.string.no_internet_connection);
                    }
                });
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (ApiException e) {
                Log.e(LOG_TAG, "ApiException key=" + e.getCode() + "   mess: " + e.getMessage());
            }
            return ids;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
//                   TODO
        }

    }

}
