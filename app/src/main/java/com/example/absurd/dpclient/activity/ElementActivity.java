package com.example.absurd.dpclient.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.absurd.dpclient.application.DBHelper;
import com.example.absurd.dpclient.R;
import com.example.absurd.dpclient.containers.ActivContainer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ElementActivity extends AppCompatActivity {
    static final int GALLERY_REQUEST = 1;
    ImageView imageView;
    EditText editTextName;
    EditText editCode;
    EditText editTextShtrihCode;
    Button loadImage;

    String code;
    String name;
    String shtrih;
    boolean flag;
    DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(this);
        dbHelper.create_db();

        setContentView(R.layout.activ_element);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageView = (ImageView) findViewById(R.id.imageView3);
        editCode = (EditText) findViewById(R.id.editCodeActiv);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextShtrihCode = (EditText) findViewById(R.id.editTextShtrihCode);
        loadImage = (Button) findViewById(R.id.loadImg);

        loadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
               // Toast.makeText(this,convertBitmapToBase64String(bitmap),Toast.LENGTH_SHORT);
            }
        });
        getInfoElement();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_menu_save);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.open();
                Drawable drawable = getResources().getDrawable(R.drawable.nophoto);
                    if(!imageView.getDrawable().equals(drawable)) {
                        Bitmap bitmap =  ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                        int optimalSize = 500;
                        double optimalWidth;
                        double optimalHeight;

                        if (bitmap.getWidth()>bitmap.getHeight()){
                            double x = bitmap.getWidth()/optimalSize;
                            optimalWidth = optimalSize;
                            optimalHeight = bitmap.getHeight()/x;
                        }
                        else {
                            double x = bitmap.getHeight()/optimalSize;
                            optimalHeight = optimalSize;
                            optimalWidth = bitmap.getWidth()/x;
                        }
                        bitmap = Bitmap.createScaledBitmap( bitmap,(int)optimalWidth ,
                                (int)optimalHeight, false);

                        String str = convertBitmapToBase64String(bitmap);

                        if(flag) {
                            ActivContainer activContainer =
                                    new ActivContainer(editCode.getText().toString(),
                                            editTextName.getText().toString(),
                                            "hz",
                                            editTextShtrihCode.getText().toString(),
                                            str);
                            dbHelper.addActiv(activContainer);
                        }
                        else {
                            ActivContainer activContainer =
                                    new ActivContainer(code,
                                            name,
                                            "hz",
                                            shtrih,
                                            str);
                            dbHelper.updateActiv(activContainer);
                        }
                    }

                dbHelper.close();

                Snackbar.make(view, "Изменения успешно сохранены", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                finish();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        Bitmap bitmap = null;

        EditText editTextName = (EditText) findViewById(R.id.editTextName);
        switch(requestCode) {
            case GALLERY_REQUEST:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        convertBitmapToBase64String(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageView.setImageBitmap(bitmap);
                }
              convertBitmapToBase64String(bitmap);
        }


    }

    public void getInfoElement(){
        Intent intent = getIntent();
        name = intent.getStringExtra("nameActiv");
        shtrih = intent.getStringExtra("shtrihCode");
        code = intent.getStringExtra("code");
        flag = intent.getBooleanExtra("flag",true);
        editCode.setText(code);
        editTextName.setText(name);
        editTextShtrihCode.setText(shtrih);
        if(intent.getStringExtra("photo")!=null) {
            imageView.setImageBitmap(convertBase64StringToBitmap(intent.getStringExtra("photo")));
        }
        else {
            imageView.setImageResource(R.drawable.nophoto);
        }
    }

    public static  Bitmap convertBase64StringToBitmap(String source) {
        byte[] rawBitmap = Base64.decode(source.getBytes(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(rawBitmap, 0, rawBitmap.length);
        return bitmap;
    }
    public static String convertBitmapToBase64String(Bitmap source) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        source.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] image = stream.toByteArray();
        String encodedImage = Base64.encodeToString(image, Base64.DEFAULT);
        return encodedImage;
    }
}
