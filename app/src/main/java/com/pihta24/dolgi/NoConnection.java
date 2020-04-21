package com.pihta24.dolgi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class NoConnection extends AppCompatActivity {

    RelativeLayout layout;
    ImageView no_connection_image;
    TextView no_connection_text;
    int primaryColor;
    int invertedColor;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_connection);

        getSupportActionBar().hide();

        SQLiteDatabase database = new MyDatabase(this).getReadableDatabase();

        Cursor cursor = database.query("settings", new String[]{"value"}, "parameter = 'colorPrimary'", null, null, null, null);
        cursor.moveToFirst();
        primaryColor = Integer.parseInt(cursor.getString(cursor.getColumnIndex("value")));
        cursor = database.query("settings", new String[]{"value"}, "parameter = 'colorInverted'", null, null, null, null);
        cursor.moveToFirst();
        invertedColor = Integer.parseInt(cursor.getString(cursor.getColumnIndex("value")));

        layout = findViewById(R.id.no_connection_layout);
        no_connection_image = findViewById(R.id.no_connection_image);
        no_connection_text = findViewById(R.id.no_connection_text);

        if (primaryColor > 0xff888888)
            no_connection_image.setImageResource(R.drawable.ic_signal_wifi_off_black_24dp);
        layout.setBackgroundColor(primaryColor);
        no_connection_text.setTextColor(invertedColor);

        Display display = getWindowManager().getDefaultDisplay();
        final DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        new AsyncTask<Void, ViewGroup.LayoutParams, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                for (int i = 1; i <= metrics.widthPixels; i+=5) {
                    @SuppressLint("WrongThread")
                    ViewGroup.LayoutParams params = no_connection_image.getLayoutParams();
                    params.height = i;
                    params.width = i;
                    publishProgress(params);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(ViewGroup.LayoutParams... values) {
                no_connection_image.setLayoutParams(values[0]);
            }
        }.execute();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (isOnline()) finish();
                    else try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) { }
                }
            }
        }).start();
        cursor.close();
        database.close();
    }

    public boolean isOnline() {
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(sockaddr, timeoutMs);
            sock.close();

            return true;
        } catch (IOException e) { return false; }
    }

    @Override
    public void onBackPressed() {
    }
}
