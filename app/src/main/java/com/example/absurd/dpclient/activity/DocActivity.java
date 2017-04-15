package com.example.absurd.dpclient.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.absurd.dpclient.R;
import com.example.absurd.dpclient.application.DBHelper;
import com.example.absurd.dpclient.containers.DocContainer;

public class DocActivity extends AppCompatActivity {
    DBHelper dbHelper;
    Button saveMessage;
    String code;
    String avtor;
    String message;
    EditText codeDoc = null;
    EditText messageDoc;
    EditText avtorDoc;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_element);
        dbHelper = new DBHelper(this);
        saveMessage = (Button) findViewById(R.id.saveMessage);
        codeDoc =(EditText) findViewById(R.id.codeDoc);
        messageDoc = (EditText) findViewById(R.id.messageDoc);
        avtorDoc = (EditText) findViewById(R.id.avtorDoc);
        //DocContainer docContainer = dbHelper.getAllDocuments();
        InitializationVariable();
        saveMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DocActivity.this, code, Toast.LENGTH_SHORT).show();
                if(code==null) {
                    dbHelper.addDocument(new DocContainer(codeDoc.getText().toString(), avtorDoc.getText().toString(), messageDoc.getText().toString()));
                }
                else {
                    dbHelper.updateDocument(new DocContainer(codeDoc.getText().toString(), avtorDoc.getText().toString(), messageDoc.getText().toString()));
                }
                finish();
            }
        });

    }
    public void InitializationVariable(){
        code = getIntent().getStringExtra("codeDoc");
        avtor = getIntent().getStringExtra("nameDoc");
        message = getIntent().getStringExtra("textDoc");
        codeDoc.setText(code);
        avtorDoc.setText(avtor);
        messageDoc.setText(message);
    }
}
