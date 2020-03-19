package com.pihta24.dolgi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Settings extends AppCompatActivity implements View.OnClickListener {

    Button pin;
    FloatingActionButton confirm;
    FloatingActionButton exit;
    Spinner spinner;
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        pin = findViewById(R.id.pin_setting);
        confirm = findViewById(R.id.confirm);
        exit = findViewById(R.id.exit);
        spinner = findViewById(R.id.spinner);
        seekBar = findViewById(R.id.seekBar);

        SQLiteDatabase database = new MyDatabase(this).getReadableDatabase();
        Cursor cursor = database.query("settings", new String[]{"value"}, "parameter = 'theme'", null, null, null, null);
        cursor.moveToFirst();
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
                content.put("value", Integer.toHexString(0xff000000 + (int)(seekBar.getProgress() * 2.55) * 0x10000 + (int)(seekBar.getProgress() * 2.55) * 0x100 + (int)(seekBar.getProgress() * 2.55)));
                database.update("settings", content, "parameter = 'colorPrimary'",null);
                content.clear();
                content.put("value", Integer.toHexString(0xff000000 + (255-(int)(seekBar.getProgress() * 2.55)) * 0x10000 + (255-(int)(seekBar.getProgress() * 2.55)) * 0x100 + (255-(int)(seekBar.getProgress() * 2.55))));
                database.update("settings", content, "parameter = 'colorInverted'",null);
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
