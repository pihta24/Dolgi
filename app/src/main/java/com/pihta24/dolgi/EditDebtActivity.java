package com.pihta24.dolgi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EditDebtActivity extends AppCompatActivity implements View.OnClickListener, View.OnSystemUiVisibilityChangeListener {

    SQLiteDatabase database_r;
    SQLiteDatabase database_w;
    TextView id;
    TextView head;
    TextView text_name;
    TextView text_last_name;
    TextView text_debt;
    EditText name;
    EditText lastName;
    EditText debt;
    RelativeLayout layout;
    FloatingActionButton accept;
    FloatingActionButton cancel;
    FloatingActionButton delete;
    CardView debt_card;
    int primaryColor;
    int invertedColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getSupportActionBar().hide();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            hideSystemUI();
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(this);

        super.onCreate(savedInstanceState);
        Intent info = getIntent();

        MyDatabase myDatabase = new MyDatabase(this);
        database_r = myDatabase.getReadableDatabase();
        database_w = myDatabase.getWritableDatabase();

        setContentView(R.layout.activity_edit_debt);
        id = findViewById(R.id.idE);
        name = findViewById(R.id.nameE);
        lastName = findViewById(R.id.lastName);
        debt = findViewById(R.id.debtE);
        accept = findViewById(R.id.accept);
        cancel = findViewById(R.id.cancel);
        delete = findViewById(R.id.delete);
        layout = findViewById(R.id.layout_edit_debt);
        head = findViewById(R.id.head);
        text_name = findViewById(R.id.text_name);
        text_last_name = findViewById(R.id.text_last_name);
        text_debt = findViewById(R.id.text_debt);
        debt_card = findViewById(R.id.debt_card);

        accept.setOnClickListener(this);
        cancel.setOnClickListener(this);
        delete.setOnClickListener(this);

        Cursor cursor = database_r.query("settings", new String[]{"value"}, "parameter = 'colorPrimary'", null, null, null, null);
        cursor.moveToFirst();
        primaryColor = Integer.parseInt(cursor.getString(cursor.getColumnIndex("value")));
        cursor = database_r.query("settings", new String[]{"value"}, "parameter = 'colorInverted'", null, null, null, null);
        cursor.moveToFirst();
        invertedColor = Integer.parseInt(cursor.getString(cursor.getColumnIndex("value")));
        cursor.close();


        id.setTextColor(invertedColor);
        name.setTextColor(invertedColor);
        name.setBackgroundTintList(ColorStateList.valueOf(invertedColor));
        lastName.setTextColor(invertedColor);
        lastName.setBackgroundTintList(ColorStateList.valueOf(invertedColor));
        debt.setTextColor(invertedColor);
        debt.setBackgroundTintList(ColorStateList.valueOf(invertedColor));
        text_name.setTextColor(invertedColor);
        text_last_name.setTextColor(invertedColor);
        text_debt.setTextColor(invertedColor);
        head.setTextColor(invertedColor);
        layout.setBackgroundColor(primaryColor);
        if (primaryColor < 0xffeeeeee) debt_card.setCardBackgroundColor(primaryColor + 0xff111111);
        else debt_card.setCardBackgroundColor(primaryColor - 0x00111111);


        switch (info.getIntExtra("id", -1)) {
            case -1: {
                id.setText("Новый долг");
                delete.hide();
                break;
            }
            default: {
                id.setText("ID: " + info.getIntExtra("id", -1));
                cursor = database_r.query(MyDatabase.TB_DEB_NAME, null, MyDatabase.COL_ID + " = " + info.getIntExtra("id", 1), null, null, null, null);
                cursor.moveToFirst();
                name.setText(cursor.getString(cursor.getColumnIndex(MyDatabase.COL_NAME)));
                lastName.setText(cursor.getString(cursor.getColumnIndex(MyDatabase.COL_LAST_NAME)));
                debt.setText(cursor.getDouble(cursor.getColumnIndex(MyDatabase.COL_SUM)) + "");
            }
        }
        info.removeExtra("id");
        cursor.close();
    }

    @Override
    public void onClick(View v) {
        final Intent intent = new Intent(this, MainActivity.class);
        switch (v.getId()) {
            case R.id.accept: {
                intent.putExtra("exit_code", 1);
                if (name.getText().toString().length() > 0 && lastName.getText().toString().length() > 0 && debt.getText().toString().length() > 0) {
                    intent.putExtra("exit_code", 1);
                    if (id.getText().toString().equals("Новый долг")) {
                        ContentValues content = new ContentValues();
                        content.put(MyDatabase.COL_NAME, name.getText().toString());
                        content.put(MyDatabase.COL_LAST_NAME, lastName.getText().toString());
                        content.put(MyDatabase.COL_SUM, Double.parseDouble(debt.getText().toString()));
                        database_w.insert(MyDatabase.TB_DEB_NAME, null, content);
                    } else {
                        ContentValues content = new ContentValues();
                        content.put(MyDatabase.COL_NAME, name.getText().toString());
                        content.put(MyDatabase.COL_LAST_NAME, lastName.getText().toString());
                        content.put(MyDatabase.COL_SUM, Double.parseDouble(debt.getText().toString()));
                        database_w.update(MyDatabase.TB_DEB_NAME, content, MyDatabase.COL_ID + " = " + id.getText().toString().split(" ")[1], null);
                    }
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.cancel: {
                intent.putExtra("exit_code", 0);
                startActivity(intent);
                break;
            }
            case R.id.delete: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Подтверждение");
                builder.setMessage("Вы уверены что хотите удалить запись?");
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        intent.putExtra("exit_code", 2);
                        database_w.delete(MyDatabase.TB_DEB_NAME, MyDatabase.COL_ID + " = " + id.getText().toString().split(" ")[1], null);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            }
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
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
                    } catch (InterruptedException ignored) {
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
