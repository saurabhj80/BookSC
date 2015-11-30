package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.parse.starter.R;
import com.parse.starter.Utilities.ParseManager;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    // Register Action
    public void registerAction(View view){

        EditText email = (EditText)findViewById(R.id.textfield_email);
        EditText password = (EditText)findViewById(R.id.textfield_password);



        if(TextUtils.isEmpty(email.getText().toString())){

            Snackbar.make(view, "Do you even have an email?", Snackbar.LENGTH_LONG).setAction("Action",null).show();
            return;
        }
        else if(!email.getText().toString().contains("@usc.edu")){
            Snackbar.make(view, "You better have a USC email", Snackbar.LENGTH_LONG).setAction("Action",null).show();
            return;
        }
        else if(TextUtils.isEmpty(password.getText().toString())){
            Snackbar.make(view, "No password is not a good idea", Snackbar.LENGTH_LONG).setAction("Action",null).show();
            return;
        }

        if (ParseManager.createUser(email.getText().toString(), password.getText().toString())) {
            Intent intent = new Intent(this, Create_Profile_Activity.class);
            startActivity(intent);
        }
        else{
            Snackbar.make(view, "Don't know what happened ", Snackbar.LENGTH_LONG).setAction("Action",null).show();
            return;
        }

    }

}
