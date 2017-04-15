package com.example.absurd.dpclient.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;

import com.example.absurd.dpclient.R;
import com.example.absurd.dpclient.adapters.OTaskAdapter;
import com.example.absurd.dpclient.application.DBHelper;
import com.example.absurd.dpclient.containers.TaskContainer;

public class TaskActivity extends AppCompatActivity {

    EditText editCode;
    EditText editName;
    EditText editContractor;
    EditText editDate;
    CheckBox checkComplite;
    ListView listViewOTask;
    DBHelper dbHelper;
    Button buttonSave;
    String code;
    String name;
    String contractor;
    String Date;
    Boolean isComplite=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_tasks);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper = new DBHelper(this);

        TabHost tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator("Вкладка 1");
        tabSpec.setContent(R.id.linearLayout2);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator("Вкладка 2");
        tabSpec.setContent(R.id.linearLayout3);
        tabHost.addTab(tabSpec);

        editCode = (EditText) findViewById(R.id.editCodeTask);
        editName = (EditText) findViewById(R.id.editNameTask);
        editContractor = (EditText) findViewById(R.id.editContractorTask);
        editDate = (EditText) findViewById(R.id.editDateTask);
        checkComplite = (CheckBox) findViewById(R.id.CompliteTask);
        InitializationVariable();
        buttonSave = (Button) findViewById(R.id.bSaveTask);
        listViewOTask = (ListView) findViewById(R.id.listViewOTask);
        editCode.setText(code);
        editName.setText(name);
        editContractor.setText(contractor);
        editDate.setText(Date);
        //checkComplite.setChecked();
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

            }
        });
        if(getIntent().getStringExtra("codeTask")!=null) {
            TaskContainer taskContainer = dbHelper.getAllTasks(getIntent().getStringExtra("codeTask")).get(0);
            listViewOTask.setAdapter(new OTaskAdapter(this, taskContainer.getOtasks()));
        }
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveChanges();
                dbHelper.updateTask(new TaskContainer(code,name,contractor,Date,""));
            }
        });

    }
    public void InitializationVariable(){
        code = getIntent().getStringExtra("codeTask");
        name = getIntent().getStringExtra("nameTask");
        contractor = getIntent().getStringExtra("contractorTask");
        Date = getIntent().getStringExtra("dateTask");
        //isComplite =
    }
    public void SaveChanges(){
        code = editCode.getText().toString();
        name = editName.getText().toString();
        contractor = editContractor.getText().toString();
        Date = editDate.getText().toString();
    }
}
