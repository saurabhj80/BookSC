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
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.starter.Model.Book;
import com.parse.starter.Model.User;
import com.parse.starter.Utilities.ParseManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreateBook extends AppCompatActivity {

    private int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 10;
    private byte[] imageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_book);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int result = saveBook(view);
                if(result == 0){
                    Snackbar.make(view, "Book Saved", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    finish();
                }
                else if(result ==1 ){

                    Snackbar.make(view, "Enter valid book title", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else if(result ==2){
                    Snackbar.make(view, "Enter valid Author", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else if(result == 3){
                    Snackbar.make(view, "Enter valid ISBN", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else if(result == 4){
                    Snackbar.make(view, "Enter valid Price", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }

            }
        });

        bookName = (EditText) this.findViewById(R.id.book_name);
        bookAuthor = (EditText) this.findViewById(R.id.book_author);
        bookISBN = (EditText) this.findViewById(R.id.book_ISBN);
        bookPrice = (EditText) this.findViewById(R.id.book_price);
        conditionSpinner = (Spinner) this.findViewById(R.id.condition_spinner);
        imgButton = (ImageButton) findViewById(R.id.book_image);

        imgButton.setClickable(true);

        String bookId = getIntent().getStringExtra("book");
        if (bookId != null) {
            Book book = ParseObject.createWithoutData(Book.class, bookId);
            try {
                book.fetchIfNeeded();
                editingBook = book;
                isEditing = true;
                updateBook(book);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isEditing = false;
    private Book editingBook = null;

    private EditText bookName;
    private EditText bookAuthor;
    private EditText bookISBN;
    private EditText bookPrice;
    private Spinner conditionSpinner;
    private ImageButton imgButton;


    public void updateBook(Book book) {

        bookName.setText(book.getTitle());
        bookAuthor.setText(book.getAuthor());
        bookISBN.setText(book.getiSBN());
        bookPrice.setText(book.getPrice());

        int position = 0;
        if (book.getQuality().equals("Good")) {
            position = 1;
        } else if (book.getQuality().equals("Fair")) {
            position = 2;
        }
        conditionSpinner.setSelection(position);
        imgButton.setClickable(false);

    }

    public int saveBook(View view) {

        if (isEditing) {

            editingBook.setTitle(bookName.getText().toString());
            editingBook.setAuthor(bookAuthor.getText().toString());
            editingBook.setiSBN(bookISBN.getText().toString());
            editingBook.setPrice(bookPrice.getText().toString());

            if (bookName.getText().toString().equals("")) {
                return 1;
            }
            if (bookAuthor.getText().toString().equals("")) {
                return 2;
            }
            try {
                Integer.parseInt(bookPrice.getText().toString());
            } catch (NumberFormatException nfe) {
                return 4;
            }

            String quality = conditionSpinner.getSelectedItem().toString();
            editingBook.setQuality(quality);

            editingBook.saveInBackground();

            return 0;
        } else {

            String quality = conditionSpinner.getSelectedItem().toString();
            boolean status = getIntent().getBooleanExtra("Sell", true);


            if (bookName.getText().toString().equals("")) {
                return 1;
            }
            if (bookAuthor.getText().toString().equals("")) {
                return 2;
            }

            try {
                Integer.parseInt(bookPrice.getText().toString());
            } catch (NumberFormatException nfe) {
                return 4;
            }


            Book book = new Book();
            book.setTitle(bookName.getText().toString());
            book.setAuthor(bookAuthor.getText().toString());
            book.setiSBN(bookISBN.getText().toString());
            book.setPrice(bookPrice.getText().toString());
            book.setQuality(quality);
            book.setOwner((User) ParseUser.getCurrentUser());
            book.setStatus(status);

            if (imageData != null) {
                ParseFile file = new ParseFile("bookImg.PNG", imageData);
                book.setPicture(file);
            }


            ParseManager.createBook(book);

            return 0;
        }


        //finish();

    }

    public void takePhoto(View view) {
        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //change drastically
        // startActivity(photoIntent);
        startActivityForResult(photoIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

            if (data != null && data.getExtras() != null) {
                Bitmap getPhoto = (Bitmap) data.getExtras().get("data"); //this data might be off
                //store this bitmap into profile image in parse
                ImageButton imgButton = (ImageButton) findViewById(R.id.book_image);

                if (getPhoto != null) {
                    int width = getPhoto.getWidth();
                    int height = getPhoto.getHeight();
                    float scaleWidth = ((float) imgButton.getWidth()) / width;
                    float scaleHeight = ((float) imgButton.getHeight()) / height;
                    Matrix matrix = new Matrix();
                    matrix.postScale(scaleWidth, scaleHeight);
                    Bitmap resizedBitmap = Bitmap.createBitmap(getPhoto, 0, 0, width, height, matrix, false);
                    imgButton.setImageBitmap(resizedBitmap);

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    resizedBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
                    imageData = bos.toByteArray();                      //this is the bitmap we put in parse
                }
            }


        } else {
            // Image capture failed
            View view = getLayoutInflater().inflate(R.layout.activity_create_book,null);
            Snackbar.make(view, "Oops something went wrong", Snackbar.LENGTH_LONG).setAction("Action",null).show();

        }
    }


}
