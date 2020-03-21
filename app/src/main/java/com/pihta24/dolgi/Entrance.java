package com.pihta24.dolgi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
    TextView pin_text;
    int primaryColor;
    int invertedColor;
    private String password = "";
    private String firstPassword = "";
    int code = 0;
    int action = 0;
    String pin_code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);

        getSupportActionBar().hide();

        Intent info = getIntent();

        code = info.getIntExtra("code", -1);
        pin_code = info.getStringExtra("pin_code_hash");
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
        pin_text = findViewById(R.id.entrance_pin_text);
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
        pin_text.setTextColor(invertedColor);
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
                password();
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

    private void password(){
        switch (code){
            case 2:
                try {

                    if (Password.check(password, pin_code)){
                        startActivity(new Intent(this, MainActivity.class).putExtra("exit_code", -1));
                    }else {
                        pin_text.setText("Попробуйте снова");
                        password = "";
                        button_delete.callOnClick();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                if(action == 0){
                    firstPassword = password;
                    password = "";
                    button_delete.callOnClick();
                    pin_text.setText("Повторите пин-код");
                    action++;
                }else{
                    if(firstPassword.equals(password)){
                        Toast.makeText(this, "Пин-код сохранен", Toast.LENGTH_SHORT).show();
                        final SQLiteDatabase database = new MyDatabase(this).getWritableDatabase();
                        final ContentValues content = new ContentValues();
                        content.put("value", "true");
                        database.update("settings", content, "parameter = 'pin_activated'", null);
                        content.clear();
                        content.put("parameter", "pin_code");
                        new AsyncTask<Void, String, Void>() {
                            @Override
                            protected Void doInBackground(Void... voids) {
                                try {
                                    String hash = Password.getSaltedHash(password);
                                    publishProgress(hash);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }
                            @Override
                            protected void onProgressUpdate(String... values) {
                                content.put("value", values[0]);
                                database.insert("settings", null, content);
                                database.close();
                                finish();
                            }
                        }.execute();
                    }else{
                        action--;
                        password = "";
                        firstPassword = "";
                        pin_text.setText("Введите пин-код");
                        Toast.makeText(this, "Пин-коды не совпадают.\nПопробуйте снова", Toast.LENGTH_SHORT).show();
                        button_delete.callOnClick();
                    }
                }
                break;
            case 0:
                try {
                    if (Password.check(password, pin_code)){
                        SQLiteDatabase database = new MyDatabase(this).getWritableDatabase();
                        ContentValues content = new ContentValues();
                        content.put("value", "false");
                        database.update("settings", content, "parameter = 'pin_activated'", null);
                        database.delete("settings", "parameter = 'pin_code'", null);
                        finish();
                    }else {
                        pin_text.setText("Попробуйте снова");
                        password = "";
                        button_delete.callOnClick();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
