package com.example.absurd.dpclient.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.absurd.dpclient.application.DBHelper;
import com.example.absurd.dpclient.fragments.DocFragment;
import com.example.absurd.dpclient.fragments.TaskFragment;
import com.example.absurd.dpclient.R;
import com.example.absurd.dpclient.fragments.ActivFragment;
import com.example.absurd.dpclient.containers.TaskContainer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Socket client;
    ArrayList<TaskContainer> tasks;
    DBHelper dbHelper;
    //InetConnection inetConnection;
    ObjectInputStream inputStream = null;
    ObjectOutputStream outputStream = null;
    DataInputStream in = null;
    DataOutputStream out = null;
    Intent intent;
    Intent Auth;
    public boolean isConnected;
    TaskFragment taskFragment;
    ActivFragment activFragment;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tasks = new ArrayList<>();
        dbHelper = new DBHelper(this);
        dbHelper.create_db();
        dbHelper.open();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Auth = getIntent();

        taskFragment = new TaskFragment();
        activFragment = new ActivFragment();

        fab = (FloatingActionButton) findViewById(R.id.fab);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //runTimer();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        TextView textLogin = (TextView) findViewById(R.id.textLogin);
        textLogin.setText(Auth.getStringExtra("login"));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            dbHelper.deleteAll();
        }

        return super.onOptionsItemSelected(item);
    }

    Fragment fragment = null;
    String currFragment = "";
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.tasks) {
            runFragment("TaskFragment");

        } else if (id == R.id.activs) {
            runFragment("ActivFragment");
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.documents) {
            runFragment("DocFragment");
        } else if (id == R.id.nav_update) {
        }
        item.setChecked(true);
        setTitle(item.getTitle());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    void runFragment(String currFragment){
        this.currFragment = currFragment;
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (currFragment){
            case "TaskFragment":
                isConnected = isNetworkAvailable();
                fragment = new TaskFragment();
                currFragment = "TaskFragment";
                fragmentTransaction.replace(R.id.fragment,fragment);
                fragmentTransaction.commit();
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent = new Intent(MainActivity.this, TaskActivity.class);
                        intent.putExtra("flag",true);
                        startActivity(intent);
                    }
                    // }
                });
                break;
            case "ActivFragment":
                isConnected = isNetworkAvailable();
                fragment = new ActivFragment();
                currFragment = "ActivFragment";
                fragmentTransaction.replace(R.id.fragment,fragment);
                fragmentTransaction.commit();
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent = new Intent(MainActivity.this, ElementActivity.class);
                        intent.putExtra("flag",true);
                        startActivity(intent);
                    }
                    // }
                });
                break;
            case "DocFragment":
                isConnected = isNetworkAvailable();
                fragment = new DocFragment();
                currFragment = "DocFragment";
                fragmentTransaction.replace(R.id.fragment,fragment);
                fragmentTransaction.commit();
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent = new Intent(MainActivity.this, DocActivity.class);
                        intent.putExtra("flag",true);
                        startActivity(intent);
                    }
                    // }
                });
                break;
        }
    }

    /*void runTimer(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                inetConnection = new InetConnection();
                inetConnection.execute();
                Toast.makeText(MainActivity.this, "Connection!", Toast.LENGTH_SHORT).show();
                handler.postDelayed(this, 60000);
            }
        });
    }*/

    @Override
    protected void onResume() {
        runFragment(currFragment);
        super.onResume();
    }
}
