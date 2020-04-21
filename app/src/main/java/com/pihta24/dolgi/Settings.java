package com.pihta24.dolgi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Settings extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener{

    RelativeLayout layout;
    CardView pin_settings;
    CardView theme_settings;
    CardView account_settings;
    CardView pin_button;
    CardView pin_button_delete;
    CardView to_login_button;
    CardView to_register_button;
    FloatingActionButton confirm;
    FloatingActionButton exit;
    TextView text_RGB;
    TextView text_red;
    TextView text_green;
    TextView text_blue;
    TextView text_pin;
    TextView text_theme;
    TextView pin_button_text;
    TextView pin_button_delete_text;
    TextView to_login_button_text;
    TextView to_register_button_text;
    SeekBar seekBarRGB;
    SeekBar seekBarRed;
    SeekBar seekBarBlue;
    SeekBar seekBarGreen;
    int primaryColor;
    int invertedColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().hide();

        layout = findViewById(R.id.settings_layout);
        pin_settings = findViewById(R.id.pin_setting_cardView);
        theme_settings = findViewById(R.id.theme_setting_cardView);
        account_settings = findViewById(R.id.account_settings_cardView);
        pin_button = findViewById(R.id.pin_button);
        pin_button_delete = findViewById(R.id.pin_button_delete);
        to_login_button = findViewById(R.id.to_login_button);
        to_register_button = findViewById(R.id.to_register_button);
        confirm = findViewById(R.id.confirm);
        exit = findViewById(R.id.exit);
        seekBarRGB = findViewById(R.id.seekBarRGB);
        seekBarRed = findViewById(R.id.seekBarRed);
        seekBarGreen = findViewById(R.id.seekBarGreen);
        seekBarBlue = findViewById(R.id.seekBarBlue);
        text_blue = findViewById(R.id.b);
        text_red = findViewById(R.id.r);
        text_green = findViewById(R.id.g);
        text_pin = findViewById(R.id.pin_text);
        text_theme = findViewById(R.id.text_theme);
        text_RGB = findViewById(R.id.rgb);
        pin_button_text = findViewById(R.id.pin_button_text);
        pin_button_delete_text = findViewById(R.id.pin_button_delete_text);
        to_login_button_text = findViewById(R.id.to_login_button_text);
        to_register_button_text = findViewById(R.id.to_register_button_text);

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
        cursor = database.query("settings", new String[]{"value"}, "parameter = 'colorPrimary'", null, null, null, null);
        cursor.moveToFirst();
        primaryColor = Integer.parseInt(cursor.getString(cursor.getColumnIndex("value")));
        cursor = database.query("settings", new String[]{"value"}, "parameter = 'colorInverted'", null, null, null, null);
        cursor.moveToFirst();
        invertedColor = Integer.parseInt(cursor.getString(cursor.getColumnIndex("value")));

        cursor = database.query("settings", new String[]{"value"}, "parameter = 'pin_activated'", null, null, null, null);
        cursor.moveToFirst();

        if(cursor.getString(cursor.getColumnIndex("value")).equals("true")){
            pin_button_delete.setVisibility(View.VISIBLE);
            pin_button_text.setText("Изменить");
        }

        cursor.close();
        database.close();

        layout.setBackgroundColor(primaryColor);
        if (primaryColor < 0xffeeeeee) {
            pin_settings.setCardBackgroundColor(primaryColor + 0xff111111);
            theme_settings.setCardBackgroundColor(primaryColor + 0xff111111);
            account_settings.setCardBackgroundColor(primaryColor + 0xff111111);
        }else{
            pin_settings.setCardBackgroundColor(primaryColor - 0x00111111);
            theme_settings.setCardBackgroundColor(primaryColor - 0x00111111);
            account_settings.setCardBackgroundColor(primaryColor - 0x00111111);
        }
        text_RGB.setTextColor(invertedColor);
        text_red.setTextColor(invertedColor);
        text_green.setTextColor(invertedColor);
        text_blue.setTextColor(invertedColor);
        text_pin.setTextColor(invertedColor);
        text_theme.setTextColor(invertedColor);
        pin_button.setCardBackgroundColor(invertedColor);
        pin_button_text.setTextColor(primaryColor);
        pin_button_delete.setCardBackgroundColor(invertedColor);
        pin_button_delete_text.setTextColor(primaryColor);
        to_login_button.setCardBackgroundColor(invertedColor);
        to_login_button_text.setTextColor(primaryColor);
        to_register_button.setCardBackgroundColor(invertedColor);
        to_register_button_text.setTextColor(primaryColor);

        pin_button_delete.setOnClickListener(this);
        pin_button.setOnClickListener(this);
        to_register_button.setOnClickListener(this);
        to_login_button.setOnClickListener(this);
        confirm.setOnClickListener(this);
        exit.setOnClickListener(this);
        seekBarRGB.setOnSeekBarChangeListener(this);
        seekBarRed.setOnSeekBarChangeListener(this);
        seekBarGreen.setOnSeekBarChangeListener(this);
        seekBarBlue.setOnSeekBarChangeListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.to_login_button:{
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            }
            case R.id.to_register_button:{
                startActivity(new Intent(this, RegisterActivity.class));
                finish();
                break;
            }
            case R.id.pin_button: {
                Intent intent = new Intent(this, Entrance.class);
                intent.putExtra("code", 1);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.pin_button_delete: {
                Intent intent = new Intent(this, Entrance.class);
                intent.putExtra("code", 0);
                Cursor cursor = new MyDatabase(this).getReadableDatabase().query("settings", new String[]{"value"}, "parameter = 'pin_code'", null, null, null, null);
                cursor.moveToFirst();
                intent.putExtra("pin_code_hash", cursor.getString(cursor.getColumnIndex("value")));
                cursor.close();
                startActivity(intent);
                finish();
                break;
            }
            case R.id.confirm: {
                Intent intent = new Intent(this, MainActivity.class);
                SQLiteDatabase database = new MyDatabase(this).getWritableDatabase();
                ContentValues content = new ContentValues();
                String colorPrimary = Integer.toString(0xff000000 + (int) (seekBarRed.getProgress() * 2.54) * 0x10000 + (int) (seekBarGreen.getProgress() * 2.54) * 0x100 + (int) (seekBarBlue.getProgress() * 2.54));
                String colorInverted = Integer.toString(0xff000000 + (254 - (int) (seekBarRed.getProgress() * 2.54)) * 0x10000 + (254 - (int) (seekBarGreen.getProgress() * 2.54)) * 0x100 + (254 - (int) (seekBarBlue.getProgress() * 2.54)));
                content.put("value", colorPrimary);
                database.update("settings", content, "parameter = 'colorPrimary'", null);
                content.clear();
                content.put("value", seekBarRGB.getProgress());
                database.update("settings", content, "parameter = 'progressRGB'", null);
                content.clear();
                content.put("value", seekBarRed.getProgress());
                database.update("settings", content, "parameter = 'progressRed'", null);
                content.clear();
                content.put("value", seekBarGreen.getProgress());
                database.update("settings", content, "parameter = 'progressGreen'", null);
                content.clear();
                content.put("value", seekBarBlue.getProgress());
                database.update("settings", content, "parameter = 'progressBlue'", null);
                content.clear();
                content.put("value", colorInverted);
                database.update("settings", content, "parameter = 'colorInverted'", null);
                intent.putExtra("exit_code", -1);
                database.close();
                startActivity(intent);
                finish();
                break;
            }
            case R.id.exit: {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("exit_code", -1);
                startActivity(intent);
                finish();
                break;
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar.getId() == R.id.seekBarRGB) {
            seekBarRed.setProgress(progress);
            seekBarGreen.setProgress(progress);
            seekBarBlue.setProgress(progress);
            layout.setBackgroundColor(0xff000000 + (int) (progress * 2.54) * 0x10000 + (int) (progress * 2.54) * 0x100 + (int) (progress * 2.54));
        } else {
            int primaryColor = 0xff000000 + (int) (seekBarRed.getProgress() * 2.54) * 0x10000 + (int) (seekBarGreen.getProgress() * 2.54) * 0x100 + (int) (seekBarBlue.getProgress() * 2.54);
            int invertedColor = 0xff000000 + (254 - (int) (seekBarRed.getProgress() * 2.54)) * 0x10000 + (254 - (int) (seekBarGreen.getProgress() * 2.54)) * 0x100 + (254 - (int) (seekBarBlue.getProgress() * 2.54));
            layout.setBackgroundColor(primaryColor);
            if (primaryColor < 0xffeeeeee) {
                pin_settings.setCardBackgroundColor(primaryColor + 0xff111111);
                theme_settings.setCardBackgroundColor(primaryColor + 0xff111111);
                account_settings.setCardBackgroundColor(primaryColor + 0xff111111);
            }else{
                pin_settings.setCardBackgroundColor(primaryColor - 0x00111111);
                theme_settings.setCardBackgroundColor(primaryColor - 0x00111111);
                account_settings.setCardBackgroundColor(primaryColor - 0x00111111);
            }
            text_RGB.setTextColor(invertedColor);
            text_red.setTextColor(invertedColor);
            text_green.setTextColor(invertedColor);
            text_blue.setTextColor(invertedColor);
            text_pin.setTextColor(invertedColor);
            text_theme.setTextColor(invertedColor);
            pin_button.setCardBackgroundColor(invertedColor);
            pin_button_text.setTextColor(primaryColor);
            pin_button_delete.setCardBackgroundColor(invertedColor);
            pin_button_delete_text.setTextColor(primaryColor);
            to_login_button.setCardBackgroundColor(invertedColor);
            to_login_button_text.setTextColor(primaryColor);
            to_register_button.setCardBackgroundColor(invertedColor);
            to_register_button_text.setTextColor(primaryColor);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onBackPressed() {
    }
}
