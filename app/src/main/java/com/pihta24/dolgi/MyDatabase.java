package com.pihta24.dolgi;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDatabase extends SQLiteOpenHelper {

    public final static String DB_NAME = "debts.db";
    public final static int DB_VERSION = 7;

    public final static String TB_DEB_NAME = "debts";
    public final static String COL_ID = "debt_id";
    public final static String COL_NAME = "name";
    public final static String COL_SUM = "sum";
    public final static String COL_LAST_NAME = "lastname";

    public MyDatabase(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ TB_DEB_NAME +" (" +
                COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NAME+" VARCHAR," +
                COL_LAST_NAME+" VARCHAR," +
                COL_SUM+" REAL" +
                ")");
        db.execSQL("CREATE TABLE settings(parameter VARCHAR(50), value VARCHAR(50))");
        ContentValues content = new ContentValues();
        content.put("parameter", "colorPrimary");
        content.put("value", "0x00000000");
        db.insert("settings", null, content);
        content.clear();
        content.put("parameter", "colorInverted");
        content.put("value", "0x00000000");
        db.insert("settings", null, content);
        content.clear();
        content.put("parameter", "pin_activated");
        content.put("value", "false");
        db.insert("settings", null, content);
    }

    private void upgrade(SQLiteDatabase db){
        db.execSQL("CREATE TABLE "+ TB_DEB_NAME +" (" +
                COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NAME+" VARCHAR," +
                COL_LAST_NAME+" VARCHAR," +
                COL_SUM+" REAL" +
                ")");
        ContentValues content = new ContentValues();
        content.put("parameter", "colorPrimary");
        content.put("value", "0x00000000");
        db.insert("settings", null, content);
        content.clear();
        content.put("parameter", "colorInverted");
        content.put("value", "0x00000000");
        db.insert("settings", null, content);
        content.clear();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE "+ TB_DEB_NAME);
        upgrade(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE "+ TB_DEB_NAME);
        upgrade(db);
    }
}
