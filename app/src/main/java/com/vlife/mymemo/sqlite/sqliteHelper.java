package com.vlife.mymemo.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/4/27 0027.
 */
public class sqliteHelper extends SQLiteOpenHelper {

    private static String INFONAME;
    private static String NAME;
    private static int VERSION = 1;

    static
    {
        NAME = " table_notepad";
        INFONAME = "notepad.db";
    }

    public sqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, INFONAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT,date TEXT,content TEXT,background INTEGER(10))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
