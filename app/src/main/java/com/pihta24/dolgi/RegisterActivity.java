package com.pihta24.dolgi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    TextView nick_text;
    TextView name_text;
    TextView last_name_text;
    TextView password_text;
    TextView password_repeat_text;
    TextView email_text;
    TextView head;
    EditText nick;
    EditText name;
    EditText last_name;
    EditText password;
    EditText password_repeat;
    EditText email;
    CheckBox agreement;
    Button to_login;
    Button register;
    CardView info_card;
    CardView buttons_card;
    RelativeLayout layout;
    int primaryColor;
    int invertedColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

        SQLiteDatabase database = new MyDatabase(this).getReadableDatabase();

        Cursor cursor = database.query("settings", new String[]{"value"}, "parameter = 'colorPrimary'", null, null, null, null);
        cursor.moveToFirst();
        primaryColor = Integer.parseInt(cursor.getString(cursor.getColumnIndex("value")));
        cursor = database.query("settings", new String[]{"value"}, "parameter = 'colorInverted'", null, null, null, null);
        cursor.moveToFirst();
        invertedColor = Integer.parseInt(cursor.getString(cursor.getColumnIndex("value")));


        nick_text = findViewById(R.id.registerNickText);
        name_text = findViewById(R.id.registerNameText);
        last_name_text = findViewById(R.id.registerLastNameText);
        password_text = findViewById(R.id.registerPasswordText);
        password_repeat_text = findViewById(R.id.registerPasswordRepeatText);
        email_text = findViewById(R.id.registerEmailText);
        head = findViewById(R.id.registerHead);
        nick = findViewById(R.id.registerNick);
        name = findViewById(R.id.registerName);
        last_name = findViewById(R.id.registerLastName);
        password = findViewById(R.id.registerPassword);
        password_repeat = findViewById(R.id.registerPasswordRepeat);
        email = findViewById(R.id.registerEmail);
        agreement = findViewById(R.id.agreement);
        to_login = findViewById(R.id.buttonToLogin);
        register = findViewById(R.id.buttonRegister);
        info_card = findViewById(R.id.registerInfoCard);
        buttons_card = findViewById(R.id.registerButtonsCard);
        layout = findViewById(R.id.register_layout);

        nick_text.setTextColor(invertedColor);
        name_text.setTextColor(invertedColor);
        last_name_text.setTextColor(invertedColor);
        password_text.setTextColor(invertedColor);
        password_repeat_text.setTextColor(invertedColor);
        email_text.setTextColor(invertedColor);
        head.setTextColor(invertedColor);
        nick.setTextColor(invertedColor);
        name.setTextColor(invertedColor);
        last_name.setTextColor(invertedColor);
        password.setTextColor(invertedColor);
        password_repeat.setTextColor(invertedColor);
        email.setTextColor(invertedColor);
        agreement.setTextColor(invertedColor);
        to_login.setTextColor(invertedColor);
        register.setTextColor(invertedColor);
        layout.setBackgroundColor(primaryColor);
        if (primaryColor < 0xffeeeeee) {
            buttons_card.setCardBackgroundColor(primaryColor + 0xff111111);
            info_card.setCardBackgroundColor(primaryColor + 0xff111111);
            register.setBackgroundColor(primaryColor + 0xff050505);
            to_login.setBackgroundColor(primaryColor + 0xff050505);
        }else{
            buttons_card.setCardBackgroundColor(primaryColor - 0x00111111);
            info_card.setCardBackgroundColor(primaryColor - 0x00111111);
            register.setBackgroundColor(primaryColor - 0x00050505);
            to_login.setBackgroundColor(primaryColor - 0x00050505);
        }

        register.setOnClickListener(this);
        to_login.setOnClickListener(this);
        cursor.close();
        database.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonRegister:{
                if (nick.getText().toString().length() > 0 &&
                        name.getText().toString().length() > 0 &&
                        last_name.getText().toString().length() > 0 &&
                        email.getText().toString().length() > 0 &&
                        password.getText().toString().length() > 0 &&
                        password_repeat.getText().toString().length() > 0){
                    if (agreement.isChecked()){
                        if(password.getText().toString().equals(password_repeat.getText().toString())) {
                            Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.1.37:5050/api/method/").addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build();
                            MyAPI.MyRegisterUserBody body = new MyAPI.MyRegisterUserBody();
                            body.email = email.getText().toString();
                            body.name = name.getText().toString();
                            body.last_name = last_name.getText().toString();
                            body.nick = nick.getText().toString();
                            body.password = password.getText().toString();
                            retrofit.create(MyAPI.class).register_user(body).enqueue(new Callback<MyAPI.MyResponse>() {
                                @Override
                                public void onResponse(Call<MyAPI.MyResponse> call, Response<MyAPI.MyResponse> response) {
                                    switch (response.body().response) {
                                        case "ok": {
                                            Toast.makeText(RegisterActivity.this, "Аккаунт зарегестрирован", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getBaseContext(), LoginActivity.class));
                                            break;
                                        }
                                        case "this email has been already taken": {
                                            Toast.makeText(RegisterActivity.this, "Данный e-mail занят", Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                        case "this nick has been already taken": {
                                            Toast.makeText(RegisterActivity.this, "Данное имя пользователя занято", Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                        case "not email": {
                                            Toast.makeText(RegisterActivity.this, "Пожалуйста введите e-mail", Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                        case "nick length less than 3": {
                                            Toast.makeText(RegisterActivity.this, "Длинна имени пользователя должна быть больше 3", Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                        default: {
                                            Toast.makeText(RegisterActivity.this, "Произошла ошибка на стороне сервера", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyAPI.MyResponse> call, Throwable t) {

                                }
                            });
                        }else
                            Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(this, "Прииете пользовательское соглашение и политику конфиденциальности", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.buttonToLogin:{
                startActivity(new Intent(this, LoginActivity.class));
                break;
            }
        }
    }
}
