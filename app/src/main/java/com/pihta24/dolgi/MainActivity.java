package com.pihta24.dolgi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    MyDatabase myDatabase;
    MyAdapter adapter;
    FloatingActionButton add;
    RelativeLayout layout;
    FloatingActionButton settings_button;
    static SwipeRefreshLayout update;
    int primaryColor;
    int invertedColor;
    int cardColor;
    int textColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        add = findViewById(R.id.add);
        layout = findViewById(R.id.layout_main);
        settings_button = findViewById(R.id.settings);
        update = findViewById(R.id.refresh);

        Intent info = getIntent();

        myDatabase = new MyDatabase(this);
        SQLiteDatabase database = myDatabase.getReadableDatabase();

        Cursor cursor = database.query("settings", new String[]{"value"}, "parameter = 'colorPrimary'", null, null, null, null);
        cursor.moveToFirst();
        primaryColor = Integer.parseInt(cursor.getString(cursor.getColumnIndex("value")));
        if (primaryColor < 0xffeeeeee) cardColor = Integer.parseInt(cursor.getString(cursor.getColumnIndex("value"))) + 0xff111111;
        else cardColor = Integer.parseInt(cursor.getString(cursor.getColumnIndex("value"))) - 0x00111111;
        cursor = database.query("settings", new String[]{"value"}, "parameter = 'colorInverted'", null, null, null, null);
        cursor.moveToFirst();
        invertedColor = Integer.parseInt(cursor.getString(cursor.getColumnIndex("value")));
        if (invertedColor < 0xff888888)
            settings_button.setImageResource(R.drawable.ic_settings_white_24dp);
        textColor = invertedColor;

        adapter = new MyAdapter(this);

        add.setOnClickListener(this);
        settings_button.setOnClickListener(this);
        update.setOnRefreshListener(this);

        RecyclerView rw = findViewById(R.id.recycler);
        rw.setLayoutManager(new LinearLayoutManager(this));
        rw.setAdapter(adapter);

        layout.setBackgroundColor(primaryColor);
        settings_button.setBackgroundTintList(ColorStateList.valueOf(invertedColor));
        update.setColorSchemeColors(Color.RED, Color.parseColor("#FFA500"), Color.YELLOW, Color.GREEN, Color.parseColor("#42AAFF"), Color.BLUE, Color.parseColor("#8B00FF"));

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
                    intent.putExtra("code", 2);
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
        cursor = database.query("settings", new String[]{"value"}, "parameter = 'token'", null, null, null, null);
        cursor.moveToFirst();
        if (cursor.getString(cursor.getColumnIndex("value")).equals("0"))
            startActivity(new Intent(this, LoginActivity.class));
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

    @Override
    public void onRefresh() {
        adapter.update(cardColor, textColor);
    }

    @Override
    public void onBackPressed() {
    }
}
