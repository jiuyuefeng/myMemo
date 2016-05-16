package com.vlife.mymemo.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vlife.mymemo.adapter.Notepad;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/27 0027.
 */
public class changeSqlite {
    public static String table = "table_notepad";

    public long add(SQLiteDatabase paramSQLiteDatabase, Notepad paramNotepad) {
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("title", paramNotepad.getTitle());
        localContentValues.put("date", paramNotepad.getDate());
        localContentValues.put("content", paramNotepad.getContent());
        localContentValues.put("background",paramNotepad.getBackground());
        long l = paramSQLiteDatabase.insert(table, null, localContentValues);
        paramSQLiteDatabase.close();
        return l;
    }

    public void delete(SQLiteDatabase paramSQLiteDatabase, Notepad paramNotepad) {
        paramSQLiteDatabase.delete(table, "id=" + paramNotepad.getId(), null);
        paramSQLiteDatabase.close();
    }

    public ArrayList<Notepad> query(SQLiteDatabase paramSQLiteDatabase) {
        ArrayList<Notepad> localArrayList = new ArrayList<>();
        Cursor localCursor = paramSQLiteDatabase.query(table, new String[] {
                        "id", "title", "content", "date", "background"}, null, null, null, null,
                null);
        while(localCursor.moveToNext()){
            Notepad localNotepad = new Notepad();
            localNotepad.setId(localCursor.getString(localCursor
                    .getColumnIndex("id")));
            localNotepad.setTitle(localCursor.getString(localCursor
                    .getColumnIndex("title")));
            localNotepad.setContent(localCursor.getString(localCursor
                    .getColumnIndex("content")));
            localNotepad.setDate(localCursor.getString(localCursor
                    .getColumnIndex("date")));
            localNotepad.setBackground(localCursor.getInt(localCursor
                    .getColumnIndex("background")));
            localArrayList.add(localNotepad);
            paramSQLiteDatabase.close();
        }
        paramSQLiteDatabase.close();
        localCursor.close();
        return localArrayList;
//        while (true) {
//            if (!localCursor.moveToNext()) {
//                paramSQLiteDatabase.close();
//                return localArrayList;
//            }
//            Notepad localNotepad = new Notepad();
//            localNotepad.setId(localCursor.getString(localCursor
//                    .getColumnIndex("id")));
//            localNotepad.setTitle(localCursor.getString(localCursor
//                    .getColumnIndex("title")));
//            localNotepad.setContent(localCursor.getString(localCursor
//                    .getColumnIndex("content")));
//            localNotepad.setDate(localCursor.getString(localCursor
//                    .getColumnIndex("date")));
//            localNotepad.setBackground(localCursor.getInt(localCursor
//                    .getColumnIndex("background")));
//            localArrayList.add(localNotepad);
//            paramSQLiteDatabase.close();
//        }
//        localCursor.close();
    }


    public void update(SQLiteDatabase paramSQLiteDatabase, Notepad paramNotepad) {
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("title", paramNotepad.getTitle());
        localContentValues.put("content", paramNotepad.getContent());
        localContentValues.put("date", paramNotepad.getDate());
        localContentValues.put("background",paramNotepad.getBackground());
        paramSQLiteDatabase.update(table, localContentValues, "id="
                + paramNotepad.getId(), null);
        paramSQLiteDatabase.close();
    }

}
