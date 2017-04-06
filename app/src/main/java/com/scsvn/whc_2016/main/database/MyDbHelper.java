package com.scsvn.whc_2016.main.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.scsvn.whc_2016.main.giaonhanhoso.DSDispatchingOrdersInfo;


public class MyDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "whc-2016.db";
    private static final int VERSION = 1;
    private static final String SQL_CREATE_DSDISPATCHINGORDERS = "CREATE TABLE " +
            DSDispatchingOrdersInfo.TABLE_NAME +" ("
            ;

    public MyDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
