package com.itech.bookagoo.work;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Artem on 16.08.14.
 */
public class DataBase extends SQLiteOpenHelper {

    private static final String LOG_TAG = "DataBase";

    private static final String CREATE_ALBUM =
            "create table " + BaseManager.TABLE.ALBUM + "(" +
                    "_id integer primary key autoincrement, " +
                    BaseManager.ALBUM.POSITION + " integer, " +
                    BaseManager.ALBUM.ID_ALBUM + " text, " +
                    BaseManager.ALBUM.TITLE + " text);";

    public DataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_ALBUM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

}
