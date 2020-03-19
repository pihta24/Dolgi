package com.pihta24.dolgi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Settings extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    Button pin;
    FloatingActionButton confirm;
    FloatingActionButton exit;
    SeekBar seekBarRGB;
    SeekBar seekBarRed;
    SeekBar seekBarBlue;
    SeekBar seekBarGreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        pin = findViewById(R.id.pin_setting);
        confirm = findViewById(R.id.confirm);
        exit = findViewById(R.id.exit);
        seekBarRGB = findViewById(R.id.seekBarRGB);
        seekBarRed = findViewById(R.id.seekBarRed);
        seekBarGreen = findViewById(R.id.seekBarGreen);
        seekBarBlue = findViewById(R.id.seekBarBlue);

        SQLiteDatabase database = new MyDatabase(this).getReadableDatabase();
        Cursor cursor = database.query("settings", new String[]{"value"}, "parameter = 'progressRGB'", null, null, null, null);
        cursor.moveToFirst();
        seekBarRGB.setProgress(cursor.getInt(cursor.getColumnIndex("value")));
        cursor = database.query("settings", new String[]{"value"}, "parameter = 'progressRed'", null, null, null, null);
        cursor.moveToFirst();
        seekBarRed.setProgress(cursor.getInt(cursor.getColumnIndex("value")));
        cursor = database.query("settings", new String[]{"value"}, "parameter = 'progressGreen'", null, null, null, null);
        cursor.moveToFirst();
        seekBarGreen.setProgress(cursor.getInt(cursor.getColumnIndex("value")));
        cursor = database.query("settings", new String[]{"value"}, "parameter = 'progressBlue'", null, null, null, null);
        cursor.moveToFirst();
        seekBarBlue.setProgress(cursor.getInt(cursor.getColumnIndex("value")));
        database.close();

        pin.setOnClickListener(this);
        confirm.setOnClickListener(this);
        exit.setOnClickListener(this);
        seekBarRGB.setOnSeekBarChangeListener(this);
        seekBarRed.setOnSeekBarChangeListener(this);
        seekBarGreen.setOnSeekBarChangeListener(this);
        seekBarBlue.setOnSeekBarChangeListener(this);


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
                String colorPrimary = Integer.toString( 0xff000000 + (int)(seekBarRed.getProgress() * 2.55) * 0x10000 + (int)(seekBarGreen.getProgress() * 2.55) * 0x100 + (int)(seekBarBlue.getProgress() * 2.55));
                String colorInverted = Integer.toString( 0xff000000 + (255-(int)(seekBarRed.getProgress() * 2.55)) * 0x10000 + (255-(int)(seekBarGreen.getProgress() * 2.55)) * 0x100 + (255-(int)(seekBarBlue.getProgress() * 2.55)));
                content.put("value", colorPrimary);
                database.update("settings", content, "parameter = 'colorPrimary'",null);
                content.clear();
                content.put("value", seekBarRGB.getProgress());
                database.update("settings", content, "parameter = 'progressRGB'",null);
                content.clear();
                content.put("value", seekBarRed.getProgress());
                database.update("settings", content, "parameter = 'progressRed'",null);
                content.clear();
                content.put("value", seekBarGreen.getProgress());
                database.update("settings", content, "parameter = 'progressGreen'",null);
                content.clear();
                content.put("value", seekBarBlue.getProgress());
                database.update("settings", content, "parameter = 'progressBlue'",null);
                content.clear();
                content.put("value", colorInverted);
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

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar.getId() == R.id.seekBarRGB){
            seekBarRed.setProgress(progress);
            seekBarGreen.setProgress(progress);
            seekBarBlue.setProgress(progress);
            findViewById(R.id.settings_layout).setBackgroundColor(0xff000000 + (int)(progress*2.55) * 0x10000 + (int)(progress*2.55) * 0x100 + (int)(progress*2.55));
        }else {
            findViewById(R.id.settings_layout).setBackgroundColor(0xff000000 + (int)(seekBarRed.getProgress()*2.55) * 0x10000 + (int)(seekBarGreen.getProgress()*2.55) * 0x100 + (int)(seekBarBlue.getProgress()*2.55));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
