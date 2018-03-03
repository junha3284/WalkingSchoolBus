package com.jade.walkinggroupbus.walkingschoolbus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;

public class LoginActivity extends AppCompatActivity {

    public static final String SHAREDPREFERENCES_EMAIL = "Email";
    public static final String SHAREDPREFERENCES_LOGIN_EMAIL = "LoginEmail";
    public static final String SHAREDPREFERENCES_PASSWORD = "Name";
    public static final String SHAREDPREFERENCES_LOGIN_PASSWORD = "Login Name";

    // Singleton Pattern implementation
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

            userInfo = UserInfo.userInfo();

            action_checkAutoLogin();
            Log.i("Login", "" + userInfo.getEmail());
            Log.i("Login", "" + userInfo.getPassword());

            // Launches Register activity
            Button button_btn = (Button) findViewById(R.id.register);
            button_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            });

            // Handles Login information
            Button mainmenu = (Button) findViewById(R.id.login);
            mainmenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Check fields for user input
                    EditText edit_password = (EditText) findViewById(R.id.passField);
                    EditText edit_email = (EditText) findViewById(R.id.userfield);

                    String inputPassword = edit_password.getText().toString();
                    String inputEmail = edit_email.getText().toString();

                    // Check to see if any fields were left blank
                    if (edit_password.length() < 1 || edit_email.length() < 1){
                        action_errorMessage();
                    }
                    else if (inputEmail.equals(userInfo.getEmail())){
                        if (inputPassword.equals(userInfo.getPassword())){

                            // Pass login to Singleton for the rest of the application to use
                            userInfo.setEmail(inputEmail);
                            userInfo.setPassword(inputPassword);

                            storeEmail_sharedPreferences();
                            storePassword_sharedPreferences();
                            // Login
                           action_mainMenu();
                        }


                    // Handles incorrect user information
                        else{
                            action_errorMessage();
                        }
                    }
                }
            });

        }

    private void action_checkAutoLogin() {

        userInfo.setEmail(action_getEmail());
        userInfo.setPassword(action_getPassword());

        if (userInfo.getEmail() != null){
            if (userInfo.getPassword() != null){
                action_mainMenu();
            }
        }
    }

    private void action_errorMessage(){
            TextView text_error = (TextView) findViewById(R.id.errorLogin);
            String message = "Error, there is an incorrect field.";
            text_error.setText(message);
        }

    private void action_mainMenu(){
        // Launch Main Menu activity
        Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
        startActivity(intent);
        finish();
    }

    private String action_getEmail() {
        SharedPreferences emailPref = getSharedPreferences(SHAREDPREFERENCES_EMAIL, MODE_PRIVATE);
        String extractEmail = emailPref.getString(SHAREDPREFERENCES_LOGIN_EMAIL, null);
        return extractEmail;
    }

    private String action_getPassword() {
        SharedPreferences passwordPref = getSharedPreferences(SHAREDPREFERENCES_PASSWORD, MODE_PRIVATE);
        String extractPassword = passwordPref.getString(SHAREDPREFERENCES_LOGIN_PASSWORD, null);
        return extractPassword;
    }

    private void storeEmail_sharedPreferences(){
        String extractedEmail = userInfo.getEmail();

        SharedPreferences emailPref = getSharedPreferences(SHAREDPREFERENCES_EMAIL, MODE_PRIVATE);
        SharedPreferences.Editor emailEditor = emailPref.edit();
        emailEditor.putString(SHAREDPREFERENCES_LOGIN_EMAIL, extractedEmail);
        emailEditor.commit();
    }

    private void storePassword_sharedPreferences() {
        String extractedPassword = userInfo.getPassword();

        SharedPreferences passwordPref = getSharedPreferences(SHAREDPREFERENCES_PASSWORD, MODE_PRIVATE);
        SharedPreferences.Editor passwordEditor = passwordPref.edit();
        passwordEditor.putString(SHAREDPREFERENCES_LOGIN_PASSWORD, extractedPassword);
        passwordEditor.commit();
    }

}
