package com.pihta24.dolgi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Entrance extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout layout;
    FloatingActionButton button0;
    FloatingActionButton button1;
    FloatingActionButton button2;
    FloatingActionButton button3;
    FloatingActionButton button4;
    FloatingActionButton button5;
    FloatingActionButton button6;
    FloatingActionButton button7;
    FloatingActionButton button8;
    FloatingActionButton button9;
    FloatingActionButton button_cancel;
    FloatingActionButton button_delete;
    ImageView indicator1;
    ImageView indicator2;
    ImageView indicator3;
    ImageView indicator4;
    int primaryColor;
    int invertedColor;
    private String password = "";
    int code = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);

        getSupportActionBar().hide();

        Intent info = getIntent();

        code = info.getIntExtra("code", 0);
        info.removeExtra("code");

        SQLiteDatabase database = new MyDatabase(this).getReadableDatabase();

        Cursor cursor = database.query("settings", new String[]{"value"}, "parameter = 'colorPrimary'", null, null, null, null);
        cursor.moveToFirst();
        primaryColor = Integer.parseInt(cursor.getString(cursor.getColumnIndex("value")));
        cursor = database.query("settings", new String[]{"value"}, "parameter = 'colorInverted'", null, null, null, null);
        cursor.moveToFirst();
        invertedColor = Integer.parseInt(cursor.getString(cursor.getColumnIndex("value")));


        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);
        button9 = findViewById(R.id.button9);
        button_cancel = findViewById(R.id.button_cancel);
        button_delete = findViewById(R.id.button_delete);
        indicator1 = findViewById(R.id.indicator_1);
        indicator2 = findViewById(R.id.indicator_2);
        indicator3 = findViewById(R.id.indicator_3);
        indicator4 = findViewById(R.id.indicator_4);
        layout = findViewById(R.id.entrance_layout);

        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        button_delete.setOnClickListener(this);
        button_cancel.setOnClickListener(this);
        button0.setBackgroundTintList(ColorStateList.valueOf(invertedColor));
        button1.setBackgroundTintList(ColorStateList.valueOf(invertedColor));
        button2.setBackgroundTintList(ColorStateList.valueOf(invertedColor));
        button3.setBackgroundTintList(ColorStateList.valueOf(invertedColor));
        button4.setBackgroundTintList(ColorStateList.valueOf(invertedColor));
        button5.setBackgroundTintList(ColorStateList.valueOf(invertedColor));
        button6.setBackgroundTintList(ColorStateList.valueOf(invertedColor));
        button7.setBackgroundTintList(ColorStateList.valueOf(invertedColor));
        button8.setBackgroundTintList(ColorStateList.valueOf(invertedColor));
        button9.setBackgroundTintList(ColorStateList.valueOf(invertedColor));
        if (invertedColor < 0xff888888) {
            button0.setImageResource(R.drawable.number_0_white);
            button1.setImageResource(R.drawable.number_1_white);
            button2.setImageResource(R.drawable.number_2_white);
            button3.setImageResource(R.drawable.number_3_white);
            button4.setImageResource(R.drawable.number_4_white);
            button5.setImageResource(R.drawable.number_5_white);
            button6.setImageResource(R.drawable.number_6_white);
            button7.setImageResource(R.drawable.number_7_white);
            button8.setImageResource(R.drawable.number_8_white);
            button9.setImageResource(R.drawable.number_9_white);
        }
        layout.setBackgroundColor(primaryColor);
        if(primaryColor > 0xff888888){
            indicator1.setImageResource(R.drawable.ic_remove_black_24dp);
            indicator2.setImageResource(R.drawable.ic_remove_black_24dp);
            indicator3.setImageResource(R.drawable.ic_remove_black_24dp);
            indicator4.setImageResource(R.drawable.ic_remove_black_24dp);
        }
        cursor.close();
        database.close();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button0 :{
                password += "0";
                break;
            }
            case R.id.button1 :{
                password += "1";
                break;
            }
            case R.id.button2 :{
                password += "2";
                break;
            }
            case R.id.button3 :{
                password += "3";
                break;
            }
            case R.id.button4 :{
                password += "4";
                break;
            }
            case R.id.button5 :{
                password += "5";
                break;
            }
            case R.id.button6 :{
                password += "6";
                break;
            }
            case R.id.button7 :{
                password += "7";
                break;
            }
            case R.id.button8 :{
                password += "8";
                break;
            }
            case R.id.button9 :{
                password += "9";
                break;
            }
            case R.id.button_cancel :{
                switch (code){
                    case 1:
                        finish();
                        break;
                    case 2:
                        finishAffinity();
                }
                break;
            }
            case R.id.button_delete :{
                if (password.length() != 0)
                    password = password.substring(0, password.length()-1);
                break;
            }
        }
        switch (password.length()){
            default:
                password = password.substring(0, 4);
            case 4:
                if(primaryColor > 0xff888888){
                    indicator1.setImageResource(R.drawable.circle_black_24dp);
                    indicator2.setImageResource(R.drawable.circle_black_24dp);
                    indicator3.setImageResource(R.drawable.circle_black_24dp);
                    indicator4.setImageResource(R.drawable.circle_black_24dp);
                }else{
                    indicator1.setImageResource(R.drawable.circle_white_24dp);
                    indicator2.setImageResource(R.drawable.circle_white_24dp);
                    indicator3.setImageResource(R.drawable.circle_white_24dp);
                    indicator4.setImageResource(R.drawable.circle_white_24dp);
                }
                Toast.makeText(this, password, Toast.LENGTH_SHORT).show();
                finish();
                break;
            case 3:
                if(primaryColor > 0xff888888){
                    indicator1.setImageResource(R.drawable.circle_black_24dp);
                    indicator2.setImageResource(R.drawable.circle_black_24dp);
                    indicator3.setImageResource(R.drawable.circle_black_24dp);
                    indicator4.setImageResource(R.drawable.ic_remove_black_24dp);
                }else{
                    indicator1.setImageResource(R.drawable.circle_white_24dp);
                    indicator2.setImageResource(R.drawable.circle_white_24dp);
                    indicator3.setImageResource(R.drawable.circle_white_24dp);
                    indicator4.setImageResource(R.drawable.ic_remove_white_24dp);
                }
                break;
            case 2:
                if(primaryColor > 0xff888888){
                    indicator1.setImageResource(R.drawable.circle_black_24dp);
                    indicator2.setImageResource(R.drawable.circle_black_24dp);
                    indicator3.setImageResource(R.drawable.ic_remove_black_24dp);
                    indicator4.setImageResource(R.drawable.ic_remove_black_24dp);
                }else{
                    indicator1.setImageResource(R.drawable.circle_white_24dp);
                    indicator2.setImageResource(R.drawable.circle_white_24dp);
                    indicator3.setImageResource(R.drawable.ic_remove_white_24dp);
                    indicator4.setImageResource(R.drawable.ic_remove_white_24dp);
                }
                break;
            case 1:
                if(primaryColor > 0xff888888){
                    indicator1.setImageResource(R.drawable.circle_black_24dp);
                    indicator2.setImageResource(R.drawable.ic_remove_black_24dp);
                    indicator3.setImageResource(R.drawable.ic_remove_black_24dp);
                    indicator4.setImageResource(R.drawable.ic_remove_black_24dp);
                }else{
                    indicator1.setImageResource(R.drawable.circle_white_24dp);
                    indicator2.setImageResource(R.drawable.ic_remove_white_24dp);
                    indicator3.setImageResource(R.drawable.ic_remove_white_24dp);
                    indicator4.setImageResource(R.drawable.ic_remove_white_24dp);
                }
                break;
            case 0:
                if(primaryColor > 0xff888888){
                    indicator1.setImageResource(R.drawable.ic_remove_black_24dp);
                    indicator2.setImageResource(R.drawable.ic_remove_black_24dp);
                    indicator3.setImageResource(R.drawable.ic_remove_black_24dp);
                    indicator4.setImageResource(R.drawable.ic_remove_black_24dp);
                }else{
                    indicator1.setImageResource(R.drawable.ic_remove_white_24dp);
                    indicator2.setImageResource(R.drawable.ic_remove_white_24dp);
                    indicator3.setImageResource(R.drawable.ic_remove_white_24dp);
                    indicator4.setImageResource(R.drawable.ic_remove_white_24dp);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
    }
}
