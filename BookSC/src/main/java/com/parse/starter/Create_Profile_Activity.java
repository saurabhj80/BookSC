package com.parse.starter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.starter.Model.User;
import com.parse.starter.Utilities.ParseManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Create_Profile_Activity extends AppCompatActivity {
    private int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void createProfileAction(View view){

        EditText firstname = (EditText)findViewById(R.id.textfield_firstname);
        EditText lastname = (EditText)findViewById(R.id.textfield_lastname);
        EditText phone = (EditText)findViewById(R.id.textfield_phone);

        String fullname = firstname.getText().toString()+" "+lastname.getText().toString();

        ParseFile file = null;

        if (imageData != null) {
            file = new ParseFile("bookImg.PNG", imageData);
        }

        ParseManager.editUser(fullname, phone.getText().toString(), file);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void takePhoto(View view) {
        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(photoIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    private byte[] imageData;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //receive the data and put it on the image view. . . or button
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

            Bitmap getPhoto = (Bitmap) data.getExtras().get("data"); //this data might be off //this is null
            //store this bitmap into profile image in parse
            ImageButton imgButton = (ImageButton) findViewById(R.id.imageButton);



            int width = getPhoto.getWidth();
            int height = getPhoto.getHeight();
            float scaleWidth = ((float) imgButton.getWidth()) / width;
            float scaleHeight = ((float) imgButton.getHeight()) / height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            Bitmap resizedBitmap = Bitmap.createBitmap(getPhoto, 0, 0, width, height, matrix, false);
            imgButton.setImageBitmap(resizedBitmap);
            Context context = this;
            File f = new File(context.getCacheDir(), "BookImg");
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            imageData = bos.toByteArray();                      //this is the bitmap we put in parse

//            FileOutputStream fos = null;
//            try {
//                fos = new FileOutputStream(f);
//                fos.write(bitmapdata);
//                fos.flush();
//                fos.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


        } else {
            // Image capture failed
        }
    }


}
