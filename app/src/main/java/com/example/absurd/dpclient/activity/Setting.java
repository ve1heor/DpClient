package com.example.absurd.dpclient.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.absurd.dpclient.R;
import com.example.absurd.dpclient.application.DBHelper;


public class Setting extends Activity {
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(this);
        dbHelper.create_db();
        dbHelper.open();
        setContentView(R.layout.setting);
        final EditText editIP = (EditText) findViewById(R.id.editIP);
        final EditText editPort = (EditText) findViewById(R.id.editPort);
        Button saveSetting = (Button) findViewById(R.id.saveSetting);
        String ipServer = dbHelper.getIP();
        ipServer.indexOf(":");
        String ip = ipServer.substring(0,ipServer.indexOf(":"));
        String port = ipServer.substring(ipServer.indexOf(":")+1,ipServer.indexOf(":")+5);
        editIP.setText(ip);
        editPort.setText(port);
        saveSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.saveIP(editIP.getText().toString(),editPort.getText().toString());
                Toast.makeText(getApplicationContext(),"Изменения успешно сохранены", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
