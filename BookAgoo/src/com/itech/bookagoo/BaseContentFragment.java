package com.itech.bookagoo;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.itech.bookagoo.tool.Log;

/**
 * Created by Artem on 13.03.14.
 */
public abstract class BaseContentFragment extends Fragment {

    abstract public int getIdTitle();
    abstract public String getNameTitle();
    abstract public String getEmail();
    abstract public int getIdIco();
    abstract public int getIdIcoTop();
    //abstract public int getIdIcoBar();
    abstract public String getUrlIco();

}
