package com.pihta24.dolgi;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

import androidx.annotation.Nullable;

public class MyDatabase extends SQLiteOpenHelper {

    public final static String DB_NAME = "debts.db";
    public final static int DB_VERSION = 1;

    public MyDatabase(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE settings(parameter VARCHAR(50), value TEXT)");
        ContentValues content = new ContentValues();
        content.put("parameter", "colorPrimary");
        content.put("value", ""+ Color.argb(255,0,0,0));
        db.insert("settings", null, content);
        content.clear();
        content.put("parameter", "colorInverted");
        content.put("value", ""+ Color.argb(255,255,255,255));
        db.insert("settings", null, content);
        content.clear();
        content.put("parameter", "progressRGB");
        content.put("value", "0");
        db.insert("settings", null, content);
        content.clear();
        content.put("parameter", "progressRed");
        content.put("value", "0");
        db.insert("settings", null, content);
        content.clear();
        content.put("parameter", "progressGreen");
        content.put("value", "0");
        db.insert("settings", null, content);
        content.clear();
        content.put("parameter", "progressBlue");
        content.put("value", "0");
        db.insert("settings", null, content);
        content.clear();
        content.put("parameter", "pin_activated");
        content.put("value", "false");
        db.insert("settings", null, content);
        content.clear();
        content.put("parameter", "token");
        content.put("value", "0");
        db.insert("settings", null, content);
    }

    private void upgrade(SQLiteDatabase db){
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        upgrade(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
