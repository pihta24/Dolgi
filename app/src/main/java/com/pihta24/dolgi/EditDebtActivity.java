package com.pihta24.dolgi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.GsonBuilder;

import java.net.ConnectException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditDebtActivity extends AppCompatActivity implements View.OnClickListener{

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

    MyAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        api = new Retrofit.Builder().baseUrl(getString(R.string.api_address)).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build().create(MyAPI.class);

        getSupportActionBar().hide();

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
                MyAPI.MyGetOneBody body = new MyAPI.MyGetOneBody();
                body.id = info.getIntExtra("id", -1);
                cursor = database_r.query("settings", new String[]{"value"}, "parameter = 'token'", null, null, null, null);
                cursor.moveToFirst();
                body.id_token = cursor.getString(cursor.getColumnIndex("value"));
                cursor.close();
                api.get_one(body).enqueue(new Callback<MyAPI.MyDatabaseResponse>() {
                    @Override
                    public void onResponse(Call<MyAPI.MyDatabaseResponse> call, Response<MyAPI.MyDatabaseResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body().response.equals("ok") && response.body().data != null) {
                                name.setText(response.body().data.get(0).name);
                                lastName.setText(response.body().data.get(0).lastname);
                                debt.setText(Double.toString(response.body().data.get(0).debt));
                            } else if (response.body().response.equals("not valid token")) {
                                Toast.makeText(EditDebtActivity.this, "Ошибка авторизации\nВойдите заново", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<MyAPI.MyDatabaseResponse> call, Throwable t) {
                        if (t instanceof ConnectException){
                            Toast.makeText(getBaseContext(), "Нет подключения к интернету", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getBaseContext(), NoConnection.class));
                        }
                    }
                });
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
                    MyAPI.MyAddToDatabaseBody body = new MyAPI.MyAddToDatabaseBody();
                    Cursor cursor = database_r.query("settings", new String[]{"value"}, "parameter = 'token'", null, null, null, null);
                    cursor.moveToFirst();
                    body.id_token = cursor.getString(cursor.getColumnIndex("value"));
                    cursor.close();
                    body.data = MyAPI.MyDatabaseNode.create(0, name.getText().toString(), lastName.getText().toString(), "none", Double.parseDouble(debt.getText().toString()), "to_me");
                    if (id.getText().toString().equals("Новый долг")) {
                        api.add_to_database(body).enqueue(new Callback<MyAPI.MyResponse>() {
                            @Override
                            public void onResponse(Call<MyAPI.MyResponse> call, Response<MyAPI.MyResponse> response) {
                                if (response.isSuccessful()) {
                                    if (response.body().response.equals("not valid token")) {
                                        Toast.makeText(EditDebtActivity.this, "Ошибка авторизации\nВойдите заново", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            @Override
                            public void onFailure(Call<MyAPI.MyResponse> call, Throwable t) {
                                if (t instanceof ConnectException){
                                    Toast.makeText(getBaseContext(), "Нет подключения к интернету", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getBaseContext(), NoConnection.class)); }
                            }
                        });
                    }else{
                        body.data.id = Integer.parseInt(id.getText().toString().split(" ")[1]);
                        api.update_database(body).enqueue(new Callback<MyAPI.MyResponse>() {
                            @Override
                            public void onResponse(Call<MyAPI.MyResponse> call, Response<MyAPI.MyResponse> response) {
                                if (response.isSuccessful()) {
                                    if (response.body().response.equals("not valid token")) {
                                        Toast.makeText(EditDebtActivity.this, "Ошибка авторизации\nВойдите заново", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            @Override
                            public void onFailure(Call<MyAPI.MyResponse> call, Throwable t) {
                                if (t instanceof ConnectException){
                                    Toast.makeText(getBaseContext(), "Нет подключения к интернету", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getBaseContext(), NoConnection.class)); }
                            }
                        });
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
                        MyAPI.MyGetOneBody body = new MyAPI.MyGetOneBody();
                        body.id = Integer.parseInt(id.getText().toString().split(" ")[1]);
                        Cursor cursor = database_r.query("settings", new String[]{"value"}, "parameter = 'token'", null, null, null, null);
                        cursor.moveToFirst();
                        body.id_token = cursor.getString(cursor.getColumnIndex("value"));
                        cursor.close();
                        api.delete_from_database(body).enqueue(new Callback<MyAPI.MyDatabaseResponse>() {
                            @Override
                            public void onResponse(Call<MyAPI.MyDatabaseResponse> call, Response<MyAPI.MyDatabaseResponse> response) {
                                if (response.isSuccessful()) {
                                    if (response.body().response.equals("not valid token")) {
                                        Toast.makeText(EditDebtActivity.this, "Ошибка авторизации\nВойдите заново", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            @Override
                            public void onFailure(Call<MyAPI.MyDatabaseResponse> call, Throwable t) {
                                if (t instanceof ConnectException){
                                    Toast.makeText(getBaseContext(), "Нет подключения к интернету", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getBaseContext(), NoConnection.class)); }
                            }
                        });
                        intent.putExtra("exit_code", 2);
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

    @Override
    public void onBackPressed() {
    }
}
