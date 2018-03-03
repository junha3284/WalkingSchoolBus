package com.jade.walkinggroupbus.walkingschoolbus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;

public class MainMenuActivity extends AppCompatActivity {


    public static final String SHAREDPREFERENCES_EMAIL = "Email";
    public static final String SHAREDPREFERENCES_LOGIN_EMAIL = "LoginEmail";
    public static final String SHAREDPREFERENCES_PASSWORD = "Name";
    public static final String SHAREDPREFERENCES_LOGIN_PASSWORD = "Login Name";

    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        userInfo = UserInfo.userInfo();

        TextView text_displayName = (TextView) findViewById(R.id.name);
        text_displayName.setText(userInfo.getName());

        Button action_logOut = (Button) findViewById(R.id.logOut);
        action_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userInfo.setName(null);
                storeEmail_sharedPreferences();
                storePassword_sharedPreferences();

                Intent logInScreen = new Intent(MainMenuActivity.this, LoginActivity.class);
                startActivity(logInScreen);
                finish();
            }
        });
    }

    private void storeEmail_sharedPreferences(){
        String extractedEmail = null;

        SharedPreferences emailPref = getSharedPreferences(SHAREDPREFERENCES_EMAIL, MODE_PRIVATE);
        SharedPreferences.Editor emailEditor = emailPref.edit();
        emailEditor.putString(SHAREDPREFERENCES_LOGIN_EMAIL, extractedEmail);
        emailEditor.commit();
    }

    private void storePassword_sharedPreferences() {
        String extractedPassword = null;

        SharedPreferences passwordPref = getSharedPreferences(SHAREDPREFERENCES_PASSWORD, MODE_PRIVATE);
        SharedPreferences.Editor passwordEditor = passwordPref.edit();
        passwordEditor.putString(SHAREDPREFERENCES_LOGIN_PASSWORD, extractedPassword);
        passwordEditor.commit();
    }
}
