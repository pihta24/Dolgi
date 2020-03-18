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
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnSystemUiVisibilityChangeListener {

    MyDatabase myDatabase;
    MyAdapter adapter;
    FloatingActionButton add;
    FloatingActionButton theme;
    ImageView background;
    RelativeLayout layout;
    FloatingActionButton settings_button;
    int cardColor = Color.parseColor("#C0C0C0");
    int textColor = Color.parseColor("#000000");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent info = getIntent();

        myDatabase = new MyDatabase(this);
        SQLiteDatabase database = myDatabase.getReadableDatabase();
        Cursor cursor = database.query("settings", new String[]{"value"}, "parameter = 'theme'", null, null, null, null);
        cursor.moveToFirst();

        adapter=new MyAdapter(this);

        getSupportActionBar().hide();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) hideSystemUI();
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(this);

        setContentView(R.layout.activity_main);

        add = findViewById(R.id.add);
        theme = findViewById(R.id.theme);
        background = findViewById(R.id.background);
        layout = findViewById(R.id.layout_main);
        settings_button = findViewById(R.id.settings);

        add.setOnClickListener(this);
        theme.setOnClickListener(this);
        settings_button.setOnClickListener(this);

        RecyclerView rw = findViewById(R.id.recycler);
        rw.setLayoutManager(new LinearLayoutManager(this));
        rw.setAdapter(adapter);



        if (cursor.getString(cursor.getColumnIndex("value")).equals("white")){
            theme.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#333333")));
            background.setImageResource(R.drawable.ic_android_black_24dp);
            layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            cardColor = Color.parseColor("#333333");
            textColor = Color.parseColor("#FFFFFF");
        }
        adapter.update(cardColor, textColor);
        cursor.close();
        switch (info.getIntExtra("exit_code", -2)){
            case -2:{
                cursor = database.query("settings", new String[]{"value"}, "parameter = 'pin_activated'", null, null, null, null);
                cursor.moveToFirst();
                if(cursor.getString(cursor.getColumnIndex("value")).equals("true")){
                    cursor = database.query("settings", new String[]{"value"}, "parameter = 'pin_code'", null, null, null, null);
                    cursor.moveToFirst();
                    Intent intent = new Intent(this, Entrance.class);
                    intent.putExtra("pin_code_hash", cursor.getString(cursor.getColumnIndex("value")));
                    startActivity(intent);
                }
                break;
            }
            case 0:{
                Toast.makeText(this, "Изменения отменены", Toast.LENGTH_SHORT).show();
                break;
            }
            case 1:{
                Toast.makeText(this, "Изменения сохранены", Toast.LENGTH_SHORT).show();
                break;
            }
            case 2:{
                Toast.makeText(this, "Запись удалена", Toast.LENGTH_SHORT).show();
                break;
            }
        }
        info.removeExtra("exit_code");
    }

    @Override
    protected void onPause() {
        super.onPause();
        myDatabase.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.settings:{
                Intent intent = new Intent(this, Settings.class);
                startActivity(intent);
                break;
            }
            case R.id.add: {
                Intent intent = new Intent(this,EditDebtActivity.class);
                intent.putExtra("id", -1);
                startActivity(intent);
                break;
            }
            case R.id.theme:{
                SQLiteDatabase databaser = myDatabase.getReadableDatabase();
                SQLiteDatabase databasew = myDatabase.getWritableDatabase();
                ContentValues content = new ContentValues();
                Cursor cursor = databaser.query("settings", new String[]{"value"}, "parameter = 'theme'", null, null, null, null);
                cursor.moveToFirst();
                switch (cursor.getString(cursor.getColumnIndex("value"))){
                    case "white":{
                        content.put("value", "black");
                        databasew.update("settings", content, "parameter = 'theme'", null);
                        theme.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                        background.setImageResource(R.drawable.ic_android_light_24dp);
                        layout.setBackgroundColor(Color.parseColor("#333333"));
                        cardColor = Color.parseColor("#C0C0C0");
                        textColor = Color.parseColor("#000000");
                        break;
                    }
                    case "black":{
                        content.put("value", "white");
                        databasew.update("settings", content, "parameter = 'theme'", null);
                        theme.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#333333")));
                        background.setImageResource(R.drawable.ic_android_black_24dp);
                        layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        cardColor = Color.parseColor("#333333");
                        textColor = Color.parseColor("#FFFFFF");
                        break;
                    }
                }
                adapter.update(cardColor, textColor);
                cursor.close();
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
        if(visibility == View.SYSTEM_UI_FLAG_VISIBLE && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            new AsyncTask<Void, Void, Void>(){
                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        Thread.sleep(3000);
                        publishProgress();
                    } catch (InterruptedException e) { }
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
