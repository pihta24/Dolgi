package com.pihta24.dolgi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Entrance extends AppCompatActivity implements View.OnClickListener {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);

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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button0 :{

                break;
            }
            case R.id.button1 :{

                break;
            }
            case R.id.button2 :{

                break;
            }
            case R.id.button3 :{

                break;
            }
            case R.id.button4 :{

                break;
            }
            case R.id.button5 :{

                break;
            }
            case R.id.button6 :{

                break;
            }
            case R.id.button7 :{

                break;
            }
            case R.id.button8 :{

                break;
            }
            case R.id.button9 :{

                break;
            }
            case R.id.button_cancel :{

                break;
            }
            case R.id.button_delete :{

                break;
            }
        }
    }
}
