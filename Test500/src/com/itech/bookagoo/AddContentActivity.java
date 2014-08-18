package com.itech.bookagoo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.actionbarsherlock.app.ActionBar;
import com.itech.bookagoo.tool.Log;
import com.itech.bookagoo.tool.errors.ApiException;
import com.itech.bookagoo.tool.errors.NetworkDisabledException;
import com.itech.bookagoo.work.BookAgooApi;
import com.viewpagerindicator.TabPageIndicator;
import org.json.JSONArray;
import org.json.JSONException;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artem on 05.04.14.
 */
public class AddContentActivity extends BaseFragmentActivity implements View.OnClickListener {

    private static final String LOG_TAG = "AddContentActivity";
    private List<String[]> mArrTag = new ArrayList<String[]>();
    private Handler mHandler = new Handler();
//    private View mContentrProgress = null;
    private AlbumAsyncTask mAlbumAsyncTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_content);

        mAlbumAsyncTask = new AlbumAsyncTask(this);
        mAlbumAsyncTask.execute();

        FragmentPagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.ab_add_context_activity);
        actionBar.getCustomView().findViewById(R.id.absAddContentActivity_ImageView_back).setOnClickListener(this);

//        mContentrProgress = findViewById(R.id.activityAddContent_View_progress);

    }

    @Override
    public void onResume(){
        super.onResume();
        if(mAlbumAsyncTask != null) mAlbumAsyncTask.setActivity(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.absAddContentActivity_ImageView_back:
                finish();
                break;
        }
    }

    private void visibilityContent() {
//        mContentrProgress.setVisibility(View.GONE);
    }

    class AlbumAsyncTask extends AsyncTask<Void, Void, List<String[]>> {

        AddContentActivity mActivity;

        public AlbumAsyncTask(AddContentActivity activity) {
            mActivity = activity;
        }

        public void setActivity(AddContentActivity activity){
            mActivity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<String[]> doInBackground(Void... params) {
            BookAgooApi api = BookAgooApi.getInstance();
            List<String[]> listAlbum = new ArrayList<String[]>();
            try {
                JSONArray jsArr = api.getAllAlbums();

                Log.v(LOG_TAG, ">" + jsArr.toString());
                //                            [
                //                                {
                //                                    "position":1,
                //                                    "id":"534d0f7e6b733110e3080000",
                //                                    "user_added":false,
                //                                    "title":"Expecting me",
                //                                    "editable":true,
                //                                    "pinned":false
                //                                },{"position":2,"id":"534d0f7e6b733110e3090000","user_added":false,"title":"Who do I look like?","editable":true,"pinned":false},{"position":3,"id":"534d0f7e6b733110e30a0000","user_added":false,"title":"My family before me","editable":true,"pinned":false},{"position":4,"id":"534d0f7e6b733110e30b0000","user_added":false,"title":"Hi, here I am","editable":true,"pinned":false},{"position":5,"id":"534d0f7e6b733110e30c0000","user_added":false,"title":"Growing up","editable":true,"pinned":false},{"position":6,"id":"534d0f7e6b733110e30d0000","user_added":false,"title":"Where I live","editable":true,"pinned":false},{"position":7,"id":"534d0f7e6b733110e30e0000","user_added":false,"title":"What I love","editable":true,"pinned":false},{"position":8,"id":"534d0f7e6b733110e30f0000","user_added":false,"title":"Hurray! Party time!","editable":true,"pinned":false},{"position":9,"id":"534d0f7e6b733110e3100000","user_added":false,"title":"Time capsule","editable":true,"pinned":false}]

//                String[] ids = new String[jsArr.length()];

                for (int j = 0; j < jsArr.length(); j++) {
                    String[] ss = new String[]{
                            jsArr.getJSONObject(j).getString(BookAgooApi.JSON.ID),
                            jsArr.getJSONObject(j).getString(BookAgooApi.JSON.TITLE)
                    };
                    int position = jsArr.getJSONObject(j).getInt(BookAgooApi.JSON.POSITION);

                    while (listAlbum.size() <= position) {
                        listAlbum.add(null);
                    }
                    listAlbum.remove(position);
                    listAlbum.add(position, ss);


                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NetworkDisabledException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (ApiException e) {
                e.printStackTrace();
            }

            return listAlbum;
        }

        @Override
        protected void onPostExecute(List<String[]> result) {
            super.onPostExecute(result);

            if (mActivity == null) return;

            mActivity.mArrTag = result;
            mActivity.visibilityContent();
        }

    }

    private class PagerAdapter extends FragmentPagerAdapter {

        private AbstrTabAddContentFragment[] mFragment = new AbstrTabAddContentFragment[]{
                new AddContentVideoFragment(),
                new AddContentPhotoFragment(),
                new AddContentSoundFragment()
        };

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return mFragment[position % mFragment.length];

        }

        @Override
        public int getCount() {
            return mFragment.length;
        }

        public CharSequence getPageTitle(int position) {
            return getString(mFragment[position % mFragment.length].getIdTitle());
        }

    }

}
