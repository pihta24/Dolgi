package com.pihta24.dolgi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;

import java.net.ConnectException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView nick_text;
    TextView password_text;
    TextView head;
    EditText nick;
    EditText password;
    Button login;
    Button to_register;
    CardView info_card;
    CardView buttons_card;
    RelativeLayout layout;
    int primaryColor;
    int invertedColor;

    Retrofit retrofit;
    MyAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        SQLiteDatabase database = new MyDatabase(this).getReadableDatabase();
        retrofit = new Retrofit.Builder().baseUrl(getString(R.string.api_address)).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build();
        api = retrofit.create(MyAPI.class);

        Cursor cursor = database.query("settings", new String[]{"value"}, "parameter = 'colorPrimary'", null, null, null, null);
        cursor.moveToFirst();
        primaryColor = Integer.parseInt(cursor.getString(cursor.getColumnIndex("value")));
        cursor = database.query("settings", new String[]{"value"}, "parameter = 'colorInverted'", null, null, null, null);
        cursor.moveToFirst();
        invertedColor = Integer.parseInt(cursor.getString(cursor.getColumnIndex("value")));


        nick_text = findViewById(R.id.loginNickText);
        password_text = findViewById(R.id.loginPasswordText);
        head = findViewById(R.id.loginHead);
        nick = findViewById(R.id.loginNick);
        password = findViewById(R.id.loginPassword);
        login = findViewById(R.id.buttonLogin);
        to_register = findViewById(R.id.buttonToRegister);
        info_card = findViewById(R.id.loginInfoCard);
        buttons_card = findViewById(R.id.loginButtonsCard);
        layout = findViewById(R.id.login_layout);

        nick_text.setTextColor(invertedColor);
        password_text.setTextColor(invertedColor);
        head.setTextColor(invertedColor);
        nick.setTextColor(invertedColor);
        password.setTextColor(invertedColor);
        login.setTextColor(invertedColor);
        to_register.setTextColor(invertedColor);
        layout.setBackgroundColor(primaryColor);
        if (primaryColor < 0xffeeeeee) {
            buttons_card.setCardBackgroundColor(primaryColor + 0xff111111);
            info_card.setCardBackgroundColor(primaryColor + 0xff111111);
            to_register.setBackgroundColor(primaryColor + 0xff050505);
            login.setBackgroundColor(primaryColor + 0xff050505);
        }else{
            buttons_card.setCardBackgroundColor(primaryColor - 0x00111111);
            info_card.setCardBackgroundColor(primaryColor - 0x00111111);
            to_register.setBackgroundColor(primaryColor - 0x00050505);
            login.setBackgroundColor(primaryColor - 0x00050505);
        }

        to_register.setOnClickListener(this);
        login.setOnClickListener(this);
        cursor.close();
        database.close();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonLogin:{
                if (password.getText().toString().length() > 0 &&
                    nick.getText().toString().length() > 0) {
                    MyAPI.MyGetTokenBody body = new MyAPI.MyGetTokenBody();
                    body.email_nick = nick.getText().toString();
                    body.password = password.getText().toString();
                    api.get_access_token(body).enqueue(new Callback<MyAPI.MyResponse>() {
                        @Override
                        public void onResponse(Call<MyAPI.MyResponse> call, Response<MyAPI.MyResponse> response) {
                            if (response.isSuccessful()) {
                                switch (response.body().response) {
                                    case "user not found": {
                                        Toast.makeText(LoginActivity.this, "Неверный логин", Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                    case "access denied":{
                                        Toast.makeText(LoginActivity.this, "Неверный пароль", Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                    default: {
                                        SQLiteDatabase database = new MyDatabase(getBaseContext()).getWritableDatabase();
                                        ContentValues content = new ContentValues();
                                        content.put("value", response.body().response);
                                        database.update("settings", content, "parameter = 'token'", null);
                                        database.close();
                                        startActivity(new Intent(getBaseContext(), MainActivity.class));
                                    }
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<MyAPI.MyResponse> call, Throwable t) {
                            if (t instanceof ConnectException){
                                Toast.makeText(getBaseContext(), "Нет подключения к интернету", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getBaseContext(), NoConnection.class));
                            }
                        }
                    });
                }else
                    Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.buttonToRegister:{
                startActivity(new Intent(this, RegisterActivity.class));
                finish();
                break;
            }
        }
    }
}
