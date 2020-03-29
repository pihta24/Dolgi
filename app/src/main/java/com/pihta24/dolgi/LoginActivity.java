package com.pihta24.dolgi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        SQLiteDatabase database = new MyDatabase(this).getReadableDatabase();

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
                break;
            }
            case R.id.buttonToRegister:{
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            }
        }
    }
}
