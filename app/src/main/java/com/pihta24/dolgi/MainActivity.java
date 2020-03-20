package com.pihta24.dolgi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnSystemUiVisibilityChangeListener {

    MyDatabase myDatabase;
    MyAdapter adapter;
    FloatingActionButton add;
    RelativeLayout layout;
    FloatingActionButton settings_button;
    int primaryColor;
    int invertedColor;
    int cardColor;
    int textColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        add = findViewById(R.id.add);
        layout = findViewById(R.id.layout_main);
        settings_button = findViewById(R.id.settings);

        Intent info = getIntent();

        myDatabase = new MyDatabase(this);

        SQLiteDatabase database = myDatabase.getReadableDatabase();

        Cursor cursor = database.query("settings", new String[]{"value"}, "parameter = 'colorPrimary'", null, null, null, null);
        cursor.moveToFirst();
        primaryColor = Integer.parseInt(cursor.getString(cursor.getColumnIndex("value")));
        if (primaryColor < 0xffeeeeee) cardColor = Integer.parseInt(cursor.getString(cursor.getColumnIndex("value"))) + 0xff111111;
        else cardColor = Integer.parseInt(cursor.getString(cursor.getColumnIndex("value"))) + 0xff111111;
        cursor = database.query("settings", new String[]{"value"}, "parameter = 'colorInverted'", null, null, null, null);
        cursor.moveToFirst();
        invertedColor = Integer.parseInt(cursor.getString(cursor.getColumnIndex("value")));
        if (invertedColor < 0xff888888)
            settings_button.setImageResource(R.drawable.ic_settings_white_24dp);
        textColor = invertedColor;

        adapter = new MyAdapter(this);

        getSupportActionBar().hide();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            hideSystemUI();
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(this);

        add.setOnClickListener(this);
        settings_button.setOnClickListener(this);

        RecyclerView rw = findViewById(R.id.recycler);
        rw.setLayoutManager(new LinearLayoutManager(this));
        rw.setAdapter(adapter);

        layout.setBackgroundColor(primaryColor);
        settings_button.setBackgroundTintList(ColorStateList.valueOf(invertedColor));
        adapter.update(cardColor, textColor);

        switch (info.getIntExtra("exit_code", -2)) {
            case -2: {
                cursor = database.query("settings", new String[]{"value"}, "parameter = 'pin_activated'", null, null, null, null);
                cursor.moveToFirst();
                if (cursor.getString(cursor.getColumnIndex("value")).equals("true")) {
                    cursor = database.query("settings", new String[]{"value"}, "parameter = 'pin_code'", null, null, null, null);
                    cursor.moveToFirst();
                    Intent intent = new Intent(this, Entrance.class);
                    intent.putExtra("pin_code_hash", cursor.getString(cursor.getColumnIndex("value")));
                    startActivity(intent);
                }
                break;
            }
            case 0: {
                Toast.makeText(this, "Изменения отменены", Toast.LENGTH_SHORT).show();
                break;
            }
            case 1: {
                Toast.makeText(this, "Изменения сохранены", Toast.LENGTH_SHORT).show();
                break;
            }
            case 2: {
                Toast.makeText(this, "Запись удалена", Toast.LENGTH_SHORT).show();
                break;
            }
        }
        info.removeExtra("exit_code");
        cursor.close();
    }

    @Override
    protected void onPause() {
        super.onPause();
        myDatabase.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings: {
                Intent intent = new Intent(this, Settings.class);
                startActivity(intent);
                break;
            }
            case R.id.add: {
                Intent intent = new Intent(this, EditDebtActivity.class);
                intent.putExtra("id", -1);
                startActivity(intent);
                break;
            }
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    public void onSystemUiVisibilityChange(int visibility) {
        if (visibility == View.SYSTEM_UI_FLAG_VISIBLE && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        Thread.sleep(3000);
                        publishProgress();
                    } catch (InterruptedException e) {
                    }
                    return null;
                }

                @Override
                protected void onProgressUpdate(Void... values) {
                    hideSystemUI();
                }
            }.execute();
        }
    }

    @Override
    public void onBackPressed() {
    }
}
