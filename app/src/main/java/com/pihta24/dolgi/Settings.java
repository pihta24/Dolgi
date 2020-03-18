package com.pihta24.dolgi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Settings extends AppCompatActivity implements View.OnClickListener {

    Button pin;
    FloatingActionButton confirm;
    FloatingActionButton exit;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        pin = findViewById(R.id.pin_setting);
        confirm = findViewById(R.id.confirm);
        exit = findViewById(R.id.exit);
        spinner = findViewById(R.id.spinner);

        SQLiteDatabase database = new MyDatabase(this).getReadableDatabase();
        Cursor cursor = database.query("settings", new String[]{"value"}, "parameter = 'theme'", null, null, null, null);
        cursor.moveToFirst();
        switch (cursor.getString(cursor.getColumnIndex("value"))){
            case "white":
                spinner.setSelection(0);
                break;
            case "black":
                spinner.setSelection(1);
        }
        database.close();

        pin.setOnClickListener(this);
        confirm.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pin_setting:{
                Intent intent = new Intent(this, Entrance.class);
                startActivity(intent);
                break;
            }
            case R.id.confirm:{
                Intent intent = new Intent(this, MainActivity.class);
                SQLiteDatabase database = new MyDatabase(this).getWritableDatabase();
                ContentValues content = new ContentValues();
                switch (spinner.getSelectedItem().toString()){
                    case "Светлая":
                        content.put("value", "white");
                        break;
                    case "Темная":
                        content.put("value", "black");
                }
                database.update("settings", content, "parameter = 'theme'", null);
                intent.putExtra("exit_code", -1);
                database.close();
                startActivity(intent);
                break;
            }
            case R.id.exit:{
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}
