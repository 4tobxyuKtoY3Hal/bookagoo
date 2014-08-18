package com.itech.bookagoo.work;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artem on 16.08.14.
 */
public class BaseManager {

    private static final String DB_NAME = "mydb";
    private static final int DB_VERSION = 1;
    private static final String LOG_TAG = "BaseManager";
    private static BaseManager sBaseManager = null;

    public static DataBase newDataBase(Context cnx) {
        return new DataBase(cnx, DB_NAME, null, DB_VERSION);
    }

    public static BaseManager getInstance() {
        if (sBaseManager == null) {
            sBaseManager = new BaseManager();
        }
        return sBaseManager;
    }

    public long addAlbum(SQLiteDatabase sdb, int position, String idAlbum, String title) {

        Log.d(LOG_TAG, "ADD_ALBUM "
                + " position=" + position
                + " idAlbum=" + idAlbum
                + " title=" + title);

//Если альбом с таким id уже есть, то удоляем его
        Cursor cursor = sdb.query(TABLE.ALBUM, new String[]{"_id"}, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                sdb.delete(TABLE.ALBUM, "_id  = " + cursor.getInt(0), null);
            }
            cursor.close();
        }

//Добовляем новый альбом
        ContentValues cv = new ContentValues();
        cv.put(ALBUM.POSITION, position);
        cv.put(ALBUM.ID_ALBUM, idAlbum);
        cv.put(ALBUM.TITLE, title);

        return sdb.insert(TABLE.ALBUM, null, cv);
    }


    public List<String[]> getAllAlbum(SQLiteDatabase db) {
        List<String[]> listAlbum = new ArrayList<String[]>();

        Cursor cursor = db.query(TABLE.ALBUM, new String[]{
                ALBUM.ID_ALBUM,
                ALBUM.TITLE},
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                ALBUM.POSITION // The sort order ORDER BY
        );
        if (cursor != null) {
            while (cursor.moveToNext()) {
                listAlbum.add(
                        new String[]{
                                cursor.getString(cursor.getColumnIndex(ALBUM.ID_ALBUM)),
                                cursor.getString(cursor.getColumnIndex(ALBUM.TITLE))
                        }
                );
            }
            cursor.close();
        }

        return listAlbum;

    }

    public static interface TABLE {
        public static final String ALBUM = "album";
    }

    public static interface ALBUM {
        public static final String POSITION = "position";
        public static final String ID_ALBUM = "id_album";
        public static final String TITLE = "title";
    }


}
