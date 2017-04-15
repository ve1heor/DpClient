package com.example.absurd.dpclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.absurd.dpclient.R;
import com.example.absurd.dpclient.application.DBHelper;


/**
 * Created by Vadim on 29.03.2017.
 */
public class Sign_in extends Activity {
    Intent intent;
    Intent settingIntent;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Button sign = (Button) findViewById(R.id.sign_in);
        Button setting = (Button) findViewById(R.id.setting);
        dbHelper = new DBHelper(this);
        dbHelper.create_db();
        dbHelper.open();
        settingIntent = new Intent(this, Setting.class);
        settingIntent.putExtra("IP",dbHelper.getIP());
        final TextView editTextLogin = (TextView) findViewById(R.id.editTextLogin);
        intent = new Intent(this, MainActivity.class);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("login", editTextLogin.getText().toString());
                startActivity(intent);

            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(settingIntent);
            }
        });
    }
}

