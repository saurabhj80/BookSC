package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.starter.Utilities.ParseManager;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // if we are logged in
        if (ParseUser.getCurrentUser() != null) {
            if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
                moveToGuestScreen();
            } else {
                moveToMainActivity();
            }
        }
    }

    // Button Actions

    public void loginAction(View view) {

        EditText username = (EditText) this.findViewById(R.id.textfield_email);
        EditText password = (EditText) this.findViewById(R.id.textfield_password);


        if(TextUtils.isEmpty(username.getText().toString())){

            Snackbar.make(view, "Do you even have an email?", Snackbar.LENGTH_LONG).setAction("Action",null).show();
            return;
        }
        else if(!username.getText().toString().contains("@usc.edu")){
            Snackbar.make(view, "You better have a USC email", Snackbar.LENGTH_LONG).setAction("Action",null).show();
            return;
        }
        else if(TextUtils.isEmpty(password.getText().toString())){
            Snackbar.make(view, "No password is not a good idea", Snackbar.LENGTH_LONG).setAction("Action",null).show();
            return;
        }

        // Log in
        if (ParseManager.loginUser(username.getText().toString(), password.getText().toString())) {
            moveToMainActivity();
        }
        else{
            Snackbar.make(view, "You do not exist", Snackbar.LENGTH_LONG).setAction("Action",null).show();
            return;
        }
    }

    public void signUpAction(View view) {
        moveToRegisterActivity();
    }

    // Guest Button clicked
    public void guestAction(View view) {
        ParseManager.guestLogin(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    // issues
                }
                if (user != null) {
                    moveToGuestScreen();
                }
            }
        });
    }

    // Moving

    private void moveToGuestScreen() {
        Intent intent = new Intent(this, GuestMain.class);
        startActivity(intent);
    }

    private void moveToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void moveToRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

}
