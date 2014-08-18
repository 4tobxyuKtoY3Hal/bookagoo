package com.itech.bookagoo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;

import android.view.*;
import android.widget.*;
import com.itech.bookagoo.tool.ImageManager;
import com.itech.bookagoo.tool.Log;
import com.itech.bookagoo.tool.Toast;
import com.itech.bookagoo.tool.errors.ApiException;
import com.itech.bookagoo.tool.errors.NetworkDisabledException;
import com.itech.bookagoo.work.BookAgooApi;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Artem on 02.03.14.
 */
public class WallFragment extends Fragment implements MainActivity.IContentFragment, View.OnClickListener {

    private final static String LOG_TAG = "WallFragment";
    private Handler mHandler = new Handler();
    private ListView mListView;
    private GridView mGridView;
    private List<ItemWall> mArrItemWall = new ArrayList<ItemWall>();
    private Adapter mAdapter;
    private boolean mIsDownload = false;
    private View mAddContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_wall, container, false);
        assert rootView != null;
        //RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        mAddContent = rootView.findViewById(R.id.fragmentWall_View_addContent);
        rootView.findViewById(R.id.fragmentWall_View_blankWall).setOnClickListener(this);


        mAdapter = new Adapter(mArrItemWall);

        if (App.getInstance().isTablet()) {
            mGridView = (GridView) rootView.findViewById(R.id.fragmentWall_GridView_wall);
            mGridView.setVisibility(View.VISIBLE);
            mGridView.setAdapter(mAdapter);
            mGridView.setNumColumns(2);
        } else {
            mListView = (ListView) rootView.findViewById(R.id.fragmentWall_ListView_wall);
            mListView.setVisibility(View.VISIBLE);
            mListView.setAdapter(mAdapter);
        }

        //queryContent(mArrItemWall.size());

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragmentWall_View_blankWall:
                getActivity().startActivity(new Intent(getActivity(), AddContentActivity.class));
                break;
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        update();

        ((MainActivity) getActivity()).visibleIcoAdd();

    }

    @Override
    public void onStop() {
        super.onStop();
//        while (mArrItemWall.size() > 0) {
//            mArrItemWall.remove(0);
//        }
//        mAdapter.notifyDataSetChanged();

    }

    @Override
    public int getIdTitle() {
        return R.string.title_wall;
    }

    @Override
    public String getNameTitle() {
        return App.getContext().getString(R.string.title_wall);
    }

    @Override
    public int getIdIco() {
        return R.drawable.ic_menu1;
    }

    @Override
    public int getIdIcoTop() {
        return R.drawable.ic_menu1_tap;
    }

    @Override
    public String getUrlIco() {
        return null;
    }

    private void update() {
        while (mArrItemWall.size() > 0) {
            mArrItemWall.remove(0);
        }
        queryContent(0);
    }

    private void queryContent(int offset) {
        new ContentAsyncTask(mHandler, offset, BookAgooApi.WALL_LIMIT).execute();
    }

    class ContentAsyncTask extends AsyncTask<Void, Void, Boolean> {

        private Handler mHandler;
        private final int mOffset;
        private final int mLimit;

        public ContentAsyncTask(Handler h, int offset, int limit) {
            mIsDownload = true;
            mHandler = h;
            mOffset = offset;
            mLimit = limit > BookAgooApi.WALL_LIMIT ? BookAgooApi.WALL_LIMIT : limit;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            int n = mArrItemWall.size() - 1;
            if (n > 0) {
                if (mArrItemWall.get(n).isProgress()) {
                    mArrItemWall.remove(n);
                }
            }

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            boolean isVisibleProgress = false;

            try {
                BookAgooApi api = BookAgooApi.getInstance();
                JSONArray jsArr = api.getWall(mOffset, mLimit);

                Log.v(LOG_TAG, "jsArr=" + jsArr);
                Log.v(LOG_TAG, ">" + jsArr.toString());

                /*
                [
                    {
                        "id":"53bd1566626f6f15ab350000",
                        "current_type":"video",
                        "text":null,
                        "contents":[
                            {
                                "album_title":"Time capsule",
                                "album_id":"53bd1566626f6f15ab2a0000",
                                "id":"53bd1566626f6f15ab290000",
                                "title":"A message from Brother"
                            },
                            {
                                "album_title":"Time capsule",
                                "album_id":"53bd1566626f6f15ab2a0000",
                                "id":"53bd1566626f6f15ab2b0000",
                                "title":"A message from Grandma"
                            },
                            {
                                "album_title":"Time capsule",
                                "album_id":"53bd1566626f6f15ab2a0000",
                                "id":"53bd1566626f6f15ab2c0000",
                                "title":"A message from Grandpa"
                            },
                            {
                                "album_title":"Time capsule",
                                "album_id":"53bd1566626f6f15ab2a0000",
                                "id":"53bd1566626f6f15ab2d0000",
                                "title":"Key events of the year"
                            }
                        ],
                        "attachment_size":17300807,
                        "content_ids":[
                            "53bd1566626f6f15ab290000",
                            "53bd1566626f6f15ab2b0000",
                            "53bd1566626f6f15ab2c0000",
                            "53bd1566626f6f15ab2d0000"
                        ],
                        "attached_files":[
                            {
                                "content":{
                                    "poster":"\/uploads\/53a46bdc626f6f4382740000\/video\/140490061622y6adgv-VID_20140610_195846-edited.jpg",
                                    "mp4":"\/uploads\/53a46bdc626f6f4382740000\/video\/140490061622y6adgv-VID_20140610_195846-edited.mp4",
                                    "webm":"\/uploads\/53a46bdc626f6f4382740000\/video\/140490061622y6adgv-VID_20140610_195846-edited.webm",
                                    "flv":"\/uploads\/53a46bdc626f6f4382740000\/video\/140490061622y6adgv-VID_20140610_195846-edited.flv",
                                    "ogv":"\/uploads\/53a46bdc626f6f4382740000\/video\/140490061622y6adgv-VID_20140610_195846-edited.ogv"
                                },
                                "id":"53bd1566626f6f15ab340000",
                                "type":"video",
                                "status":"ready",
                                "url":"\/uploads\/53a46bdc626f6f4382740000\/video\/140490061622y6adgv-VID_20140610_195846.3gp",
                                "name":"140490061622y6adgv-VID_20140610_195846.3gp",
                                "size":17300807
                            }
                        ],
                        "created_at":"2014-07-09T10:11:50.622+00:00",
                        "comment":"Тестовое видео, с 4мя тегами",
                        "created_at_unix":1404900710
                    },

                    {
                        "id":"53bd0232626f6f35013d0300",
                        "current_type":"audio",
                        "text":null,
                        "contents":[
                            {
                                "album_title":"Before me",
                                "album_id":"53bd0232626f6f35012e0300",
                                "id":"53bd0232626f6f3501330300",
                                "title":"Other"
                            }
                        ],
                        "attachment_size":5463273,
                        "content_ids":["53bd0232626f6f3501330300"],
                        "attached_files":[
                            {
                                "content":{
                                    "m4a":"\/uploads\/53a46bdc626f6f4382740000\/audio\/14048957688ouuvlz8-splin_-_vykhoda_net__zaycev.net_-edited.m4a",
                                    "mp3":"\/uploads\/53a46bdc626f6f4382740000\/audio\/14048957688ouuvlz8-splin_-_vykhoda_net__zaycev.net_-edited.mp3",
                                    "ogg":"\/uploads\/53a46bdc626f6f4382740000\/audio\/14048957688ouuvlz8-splin_-_vykhoda_net__zaycev.net_-edited.ogg"
                                },
                                "id":"53bd0232626f6f35013c0300",
                                "type":"audio",
                                "status":"ready",
                                "url":"\/uploads\/53a46bdc626f6f4382740000\/audio\/14048957688ouuvlz8-splin_-_vykhoda_net__zaycev.net_.mp3",
                                "name":"14048957688ouuvlz8-splin_-_vykhoda_net__zaycev.net_.mp3",
                                "size":5463273
                            }
                        ],
                        "created_at":"2014-07-09T08:49:54.859+00:00",
                        "comment":"тестовая запись",
                        "created_at_unix":1404895794
                    },

                    {
                        "id":"53bd0094626f6f3501ba0200",
                        "current_type":"image",
                        "text":null,
                        "contents":[
                            {
                                "album_title":"Expecting me",
                                "album_id":"53bd0094626f6f3501ae0200",
                                "id":"53bd0094626f6f3501b40200",
                                "title":"Pregnant mommy"
                            },
                            {
                                "album_title":"Expecting me",
                                "album_id":"53bd0094626f6f3501ae0200",
                                "id":"53bd0094626f6f3501b50200",
                                "title":"Ultrasonic"
                            },
                            {
                                "album_title":"Expecting me",
                                "album_id":"53bd0094626f6f3501ae0200",
                                "id":"53bd0094626f6f3501b60200",
                                "title":"Parents expecting me"
                            }
                        ],
                        "attachment_size":2175684,
                        "content_ids":[
                            "53bd0094626f6f3501b40200",
                            "53bd0094626f6f3501b50200",
                            "53bd0094626f6f3501b60200"
                        ],
                        "attached_files":[
                            {
                                "content":{
                                    "jpg":"\/uploads\/53a46bdc626f6f4382740000\/image\/1404895329xiiaaq7t-____________004-edited.jpg",
                                    "jpg100":"\/uploads\/53a46bdc626f6f4382740000\/image\/1404895329xiiaaq7t-____________004-edited100.jpg",
                                    "jpg50":"\/uploads\/53a46bdc626f6f4382740000\/image\/1404895329xiiaaq7t-____________004-edited50.jpg",
                                    "jpg25":"\/uploads\/53a46bdc626f6f4382740000\/image\/1404895329xiiaaq7t-____________004-edited25.jpg"
                                },
                                "id":"53bd0094626f6f3501b90200",
                                "type":"image",
                                "status":"ready",
                                "url":"\/uploads\/53a46bdc626f6f4382740000\/image\/1404895329xiiaaq7t-____________004.jpg",
                                "name":"1404895329xiiaaq7t-____________004.jpg",
                                "size":2175684
                            }
                        ],
                        "created_at":"2014-07-09T08:43:00.836+00:00",
                        "comment":"Какой-то теккст",
                        "created_at_unix":1404895380
                    }
                ]
                */

                if (jsArr.length() == BookAgooApi.WALL_LIMIT) {
                    isVisibleProgress = true;
                }

                for (int i = 0; i < jsArr.length(); i++) {
                    JSONObject jsObj = jsArr.getJSONObject(i);
                    final ItemWall iw = new ItemWall();
                    iw.setCurrentType(jsObj.getString(BookAgooApi.JSON.CURRENT_TYPE));
                    if (iw.isText()) {
                        iw.comment = jsObj.getString(BookAgooApi.JSON.TEXT);
                    } else {
                        iw.comment = jsObj.getString(BookAgooApi.JSON.COMMENT);
                    }
                    iw.createdAtUnix = jsObj.getLong(BookAgooApi.JSON.CREATED_AT_UNIX);
                    JSONArray jsArrContents = jsObj.getJSONArray(BookAgooApi.JSON.CONTENTS);
                    for (int j = 0; j < jsArrContents.length(); j++) {
                        JSONObject jsObjContents = jsArrContents.getJSONObject(j);
                        iw.arrTitle.add(jsObjContents.getString(BookAgooApi.JSON.TITLE));
                    }

                    /*
                    {
                    "id":"53c4477f626f6f28e9960a00",
                    "current_type":"text",
                    "text":"укввепаав",
                    "contents":[],
                    "attachment_size":0,
                    "content_ids":[],
                    "attached_files":[],
                    "created_at":"2014-07-14T21:11:27.898+00:00",
                    "comment":null,"created_at_unix":1405372287},{"id":"53c44767626f6f28e9950a00","current_type":"text","text":"укввепаав","contents":[],"attachment_size":0,"content_ids":[],"attached_files":[],"created_at":"2014-07-14T21:11:03.370+00:00","comment":null,"created_at_unix":1405372263},{"id":"53c4423b626f6f28e9890a00","current_type":"image","text":null,"contents":[{"album_title":"Hurray! Party time!","album_id":"53c4423b626f6f28e96c0a00","id":"53c4423b626f6f28e9740a00","title":"Birthday wishes"}],"attachment_size":65407,"content_ids":["53c4423b626f6f28e9740a00","53c4423b626f6f28e9740a00"],"attached_files":[{"content":{"jpg":"\/uploads\/53c441d1626f6f28e93b0a00\/image\/1405370934flg36qwm-BeFunky_zzzzzzzzz.jpg-edited.jpg","jpg100":"\/uploads\/53c441d1626f6f28e93b0a00\/image\/1405370934flg36qwm-BeFunky_zzzzzzzzz.jpg-edited100.jpg","jpg50":"\/uploads\/53c441d1626f6f28e93b0a00\/image\/1405370934flg36qwm-BeFunky_zzzzzzzzz.jpg-edited50.jpg","jpg25":"\/uploads\/53c441d1626f6f28e93b0a00\/image\/1405370934flg36qwm-BeFunky_zzzzzzzzz.jpg-edited25.jpg"},"id":"53c4423b626f6f28e9880a00","type":"image","status":"ready","url":"\/uploads\/53c441d1626f6f28e93b0a00\/image\/1405370934flg36qwm-BeFunky_zzzzzzzzz.jpg.jpg","name":"1405370934flg36qwm-BeFunky_zzzzzzzzz.jpg.jpg","size":65407}],"created_at":"2014-07-14T20:48:59.923+00:00","comment":null,"created_at_unix":1405370939},{"id":"53c4422e626f6f28e95f0a00","current_type":"image","text":null,"contents":[{"album_title":"What I love","album_id":"53c4422e626f6f28e9510a00","id":"53c4422e626f6f28e95a0a00","title":"Clothes I hate"}],"attachment_size":223111,"content_ids":["53c4422e626f6f28e95a0a00","53c4422e626f6f28e95a0a00"],"attached_files":[{"content":{"jpg":"\/uploads\/53c441d1626f6f28e93b0a00\/image\/1405370916my6bylh3-9pB7BRXRvMU-edited.jpg","jpg100":"\/uploads\/53c441d1626f6f28e93b0a00\/image\/1405370916my6bylh3-9pB7BRXRvMU-edited100.jpg","jpg50":"\/uploads\/53c441d1626f6f28e93b0a00\/image\/1405370916my6bylh3-9pB7BRXRvMU-edited50.jpg","jpg25":"\/uploads\/53c441d1626f6f28e93b0a00\/image\/1405370916my6bylh3-9pB7BRXRvMU-edited25.jpg"},"id":"53c4422e626f6f28e95e0a00","type":"image","status":"ready","url":"\/uploads\/53c441d1626f6f28e93b0a00\/image\/1405370916my6bylh3-9pB7BRXRvMU.jpg","name":"1405370916my6bylh3-9pB7BRXRvMU.jpg","size":223111}],"created_at":"2014-07-14T20:48:46.582+00:00","comment":"егщн","created_at_unix":1405370926}]
                    */
                    JSONArray jsArrAttachedFiles = jsObj.getJSONArray(BookAgooApi.JSON.ATTACHED_FILES);
                    if (jsArrAttachedFiles.length() > 0) {
                        JSONObject jsObjFile = jsArrAttachedFiles.getJSONObject(0);

                        iw.status = jsObjFile.getString(BookAgooApi.JSON.STATUS);

                        if (iw.status.equals("ready")) {
                            JSONObject jsObjContent = jsObjFile.getJSONObject(BookAgooApi.JSON.CONTENT);
                            if (iw.isVideo()) {
                                iw.strUriPoster = jsObjContent.getString(BookAgooApi.JSON.POSTER);
                                iw.strUriMp4 = jsObjContent.getString(BookAgooApi.JSON.MP4);
                            } else if (iw.isAudio()) {
                                iw.strUriMp3 = jsObjContent.getString(BookAgooApi.JSON.MP3);
                                iw.fileName = jsObjFile.getString(BookAgooApi.JSON.NAME);
                            } else if (iw.isImage()) {
                                iw.strUriPoster = jsObjContent.getString(BookAgooApi.JSON.JPG25);
                            }
                        }
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mArrItemWall.add(iw);
                            if (App.getInstance().isTablet()) {

                            }

                        }
                    });
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
                switch (e.getCode()) {
                    default:

                }

            }
            return isVisibleProgress;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                mArrItemWall.add(new ItemWall());
            }
            if (mArrItemWall.size() == 0) {
                if (mAddContent.getVisibility() != View.VISIBLE) mAddContent.setVisibility(View.VISIBLE);
            } else {
                if (mAddContent.getVisibility() != View.GONE) mAddContent.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();
            }
            mAdapter.notifyDataSetChanged();
            mIsDownload = false;
        }

    }

    class ItemWall {

        public long createdAtUnix = 0;
        public String comment = null;
        public List<String> arrTitle = new ArrayList<String>();
        public String strUriPoster = null;
        public String strUriMp4 = null;
        public String strUriMp3 = null;
        public String status = null;
        public String fileName = null;

        private boolean mIsVideo = false;
        private boolean mIsAudio = false;
        private boolean mIsImage = false;
        private boolean mIsText = false;
        private boolean mIsProgress = true;

        public void setCurrentType(String currentType) {
            if (currentType.equals(BookAgooApi.CURRENT_TYPE.AUDIO)) {
                mIsAudio = true;
                mIsVideo = false;
                mIsImage = false;
                mIsProgress = false;
            } else if (currentType.equals(BookAgooApi.CURRENT_TYPE.VIDEO)) {
                mIsAudio = false;
                mIsVideo = true;
                mIsImage = false;
                mIsProgress = false;
            } else if (currentType.equals(BookAgooApi.CURRENT_TYPE.IMAGE)) {
                mIsAudio = false;
                mIsVideo = false;
                mIsImage = true;
                mIsProgress = false;
            } else if (currentType.equals(BookAgooApi.CURRENT_TYPE.TEXT)) {
                mIsAudio = false;
                mIsVideo = false;
                mIsImage = false;
                mIsProgress = false;
                mIsText = true;
            }

        }

        public boolean isVideo() {
            return mIsVideo;
        }

        public boolean isText() {
            return mIsText;
        }

        public boolean isAudio() {
            return mIsAudio;
        }

        public boolean isImage() {
            return mIsImage;
        }

        public boolean isProgress() {
            return mIsProgress;
        }

    }

    class Adapter extends BaseAdapter {

        private List<ItemWall> mArrItemWall;

        public Adapter(List<ItemWall> arrIw) {
            mArrItemWall = arrIw;
        }

        @Override
        public int getCount() {
            return mArrItemWall.size();
        }

        @Override
        public Object getItem(int position) {
            if (position >= mArrItemWall.size()) {
                position = mArrItemWall.size() - 1;
            }
            return mArrItemWall.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;
            LayoutInflater layout = (LayoutInflater) App.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ItemWall item = (ItemWall) getItem(position);

            View viewImg;

            if (v == null) {
                v = layout.inflate(R.layout.item_wall, parent, false);

                viewImg = v.findViewById(R.id.itemWall_View_foto);

                if (App.getInstance().isTablet()) {
                    v.setMinimumHeight(Math.round(parent.getWidth() / 2));
                } else {
                    v.setMinimumHeight(parent.getWidth());
                }
            } else {
                viewImg = v.findViewById(R.id.itemWall_View_foto);
            }


            TextView txtDate = (TextView) v.findViewById(R.id.itemWall_TextView_date);
            TextView txtTag = (TextView) v.findViewById(R.id.itemWall_TextView_tag);
            ImageView imageFoto = (ImageView) v.findViewById(R.id.itemWall_ImageView_foto);
            TextView txtCommitImg = (TextView) v.findViewById(R.id.itemWall_TextView_commentImg);
            View btnVideo = v.findViewById(R.id.itemWall_Button_playVideo);
            View viewAudio = v.findViewById(R.id.itemWall_View_audio);
            TextView txtCommit = (TextView) v.findViewById(R.id.itemWall_TextView_comment);
            View viewProgress = v.findViewById(R.id.itemWall_View_progress);
            View viewAudioText = v.findViewById(R.id.itemWall_View_audioText);
            View viewDate = v.findViewById(R.id.itemWall_View_date);

            if (item.isProgress()) {
                if (viewDate.getVisibility() != View.GONE) viewDate.setVisibility(View.GONE);
                if (viewProgress.getVisibility() != View.VISIBLE) viewProgress.setVisibility(View.VISIBLE);
                if (txtCommit.getVisibility() != View.GONE) txtCommit.setVisibility(View.GONE);
                if (viewAudio.getVisibility() != View.GONE) viewAudio.setVisibility(View.GONE);
                if (btnVideo.getVisibility() != View.GONE) btnVideo.setVisibility(View.GONE);
                if (txtCommitImg.getVisibility() != View.GONE) txtCommitImg.setVisibility(View.GONE);
                if (viewImg.getVisibility() != View.GONE) viewImg.setVisibility(View.GONE);
                if (txtTag.getVisibility() != View.GONE) txtTag.setVisibility(View.GONE);
                if (txtDate.getVisibility() != View.GONE) txtDate.setVisibility(View.GONE);
                if (viewAudioText.getVisibility() != View.GONE) viewAudioText.setVisibility(View.GONE);
                if (!mIsDownload) {
                    queryContent(mArrItemWall.size() - 1);
                }
            } else {
                Date d = new Date(item.createdAtUnix * 1000);
                ImageManager im = ImageManager.getInstance();
                ((TextView) (v.findViewById(R.id.itemWall_TextView_date))).setText(
                        "" + d.getDate()
                                + "." + (d.getMonth() < 9 ? "0" + (d.getMonth() + 1) : "" + (d.getMonth() + 1))
                                + "." + (1900 + d.getYear()));

                if (item.arrTitle.size() > 0) {
                    String str = item.arrTitle.get(0);
                    for (int i = 1; i < item.arrTitle.size(); i++) {
                        str += ", " + item.arrTitle.get(i);
                    }
                    ((TextView) v.findViewById(R.id.itemWall_TextView_tag)).setText(str);
                }
                if (txtTag.getVisibility() != View.VISIBLE) txtTag.setVisibility(View.VISIBLE);
                if (txtDate.getVisibility() != View.VISIBLE) txtDate.setVisibility(View.VISIBLE);
                if (viewDate.getVisibility() != View.VISIBLE) viewDate.setVisibility(View.VISIBLE);

                if (item.isImage()) {
                    if (viewAudioText.getVisibility() != View.GONE) viewAudioText.setVisibility(View.GONE);
                    if (viewProgress.getVisibility() != View.GONE) viewProgress.setVisibility(View.GONE);
                    if (txtCommit.getVisibility() != View.GONE) txtCommit.setVisibility(View.GONE);
                    if (viewAudio.getVisibility() != View.GONE) viewAudio.setVisibility(View.GONE);
                    if (btnVideo.getVisibility() != View.GONE) btnVideo.setVisibility(View.GONE);
                    if (item.strUriPoster != null) {
                        if (viewImg.getVisibility() != View.VISIBLE) viewImg.setVisibility(View.VISIBLE);
                        im.fetchImage(
                                App.getContext(),
                                3600,
                                Build.BUUK_AGOO_API_SERVER + item.strUriPoster.replace("\\", ""),
                                imageFoto
                        );
                    }

                    if (item.comment != null && !item.comment.equals("null") && item.comment.length() > 0) {
                        if (txtCommitImg.getVisibility() != View.VISIBLE) txtCommitImg.setVisibility(View.VISIBLE);
                        txtCommitImg.setText(item.comment);
                    } else {
                        if (txtCommitImg.getVisibility() != View.GONE) txtCommitImg.setVisibility(View.GONE);
                    }

                } else if (item.isAudio()) {
                    if (viewAudioText.getVisibility() != View.VISIBLE) viewAudioText.setVisibility(View.VISIBLE);
                    if (viewProgress.getVisibility() != View.GONE) viewProgress.setVisibility(View.GONE);
                    if (item.comment != null && !item.comment.equals("null") && item.comment.length() > 0) {
                        if (txtCommit.getVisibility() != View.VISIBLE) txtCommit.setVisibility(View.VISIBLE);
                        txtCommit.setText(item.comment);
                    } else {
                        if (txtCommit.getVisibility() != View.GONE) txtCommit.setVisibility(View.GONE);
                    }
                    if (viewAudio.getVisibility() != View.VISIBLE) viewAudio.setVisibility(View.VISIBLE);
                    if (btnVideo.getVisibility() != View.GONE) btnVideo.setVisibility(View.GONE);
                    if (txtCommitImg.getVisibility() != View.GONE) txtCommitImg.setVisibility(View.GONE);
                    if (viewImg.getVisibility() != View.GONE) viewImg.setVisibility(View.GONE);
                    ((TextView) v.findViewById(R.id.itemWall_TextView_name)).setText(item.fileName);
                    final Uri uriMp3 = Uri.parse(Build.BUUK_AGOO_API_SERVER + item.strUriMp3);
                    v.findViewById(R.id.itemWall_Button_playAudio).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(uriMp3, "audio/mp3");
                            startActivity(intent);
                        }
                    });
                } else if (item.isVideo()) {
                    if (viewAudioText.getVisibility() != View.GONE) viewAudioText.setVisibility(View.GONE);
                    if (viewProgress.getVisibility() != View.GONE) viewProgress.setVisibility(View.GONE);
                    if (txtCommit.getVisibility() != View.GONE) txtCommit.setVisibility(View.GONE);
                    if (viewAudio.getVisibility() != View.GONE) viewAudio.setVisibility(View.GONE);
                    if (btnVideo.getVisibility() != View.VISIBLE) btnVideo.setVisibility(View.VISIBLE);
                    if (viewImg.getVisibility() != View.VISIBLE) viewImg.setVisibility(View.VISIBLE);
                    imageFoto.setImageResource(0);
                    if (item.strUriPoster != null) {
                        im.fetchImage(
                                App.getContext(),
                                3600,
                                Build.BUUK_AGOO_API_SERVER + item.strUriPoster.replace("\\", ""),
                                imageFoto
                        );
                    }

                    final Uri uriMp4 = Uri.parse(Build.BUUK_AGOO_API_SERVER + item.strUriMp4);
                    btnVideo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(uriMp4, "video/mp4");
                            startActivity(intent);
                        }
                    });
                    if (item.comment != null && !item.comment.equals("null") && item.comment.length() > 0) {
                        if (txtCommitImg.getVisibility() != View.VISIBLE) txtCommitImg.setVisibility(View.VISIBLE);
                        txtCommitImg.setText(item.comment);
                    } else {
                        if (txtCommitImg.getVisibility() != View.GONE) txtCommitImg.setVisibility(View.GONE);
                    }
                } else if (item.isText()) {
                    if (viewAudioText.getVisibility() != View.VISIBLE) viewAudioText.setVisibility(View.VISIBLE);
                    if (viewProgress.getVisibility() != View.GONE) viewProgress.setVisibility(View.GONE);
                    if (item.comment != null && !item.comment.equals("null") && item.comment.length() > 0) {
                        if (txtCommit.getVisibility() != View.VISIBLE) txtCommit.setVisibility(View.VISIBLE);
                        txtCommit.setText(item.comment);
                    } else {
                        if (txtCommit.getVisibility() != View.GONE) txtCommit.setVisibility(View.GONE);
                    }
                    if (viewAudio.getVisibility() != View.GONE) viewAudio.setVisibility(View.GONE);
                    if (btnVideo.getVisibility() != View.GONE) btnVideo.setVisibility(View.GONE);
                    if (txtCommitImg.getVisibility() != View.GONE) txtCommitImg.setVisibility(View.GONE);
                    if (viewImg.getVisibility() != View.GONE) viewImg.setVisibility(View.GONE);
                } else {
                    if (viewProgress.getVisibility() != View.GONE) viewProgress.setVisibility(View.GONE);
                    if (txtCommit.getVisibility() != View.GONE) txtCommit.setVisibility(View.GONE);
                    if (viewAudio.getVisibility() != View.GONE) viewAudio.setVisibility(View.GONE);
                    if (btnVideo.getVisibility() != View.GONE) btnVideo.setVisibility(View.GONE);
                    if (txtCommitImg.getVisibility() != View.GONE) txtCommitImg.setVisibility(View.GONE);
                    if (viewImg.getVisibility() != View.GONE) viewImg.setVisibility(View.GONE);
                }

            }
            return v;
        }
    }

}
