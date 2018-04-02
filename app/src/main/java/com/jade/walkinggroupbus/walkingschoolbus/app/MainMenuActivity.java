package com.jade.walkinggroupbus.walkingschoolbus.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jade.walkinggroupbus.walkingschoolbus.R;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;

/**
 * Main menu allows current user to :
 * navigate to dashboard
 * navigate to the groups section of the app
 * logout
 */

public class MainMenuActivity extends AppCompatActivity {


    // Tags for Shared Preferences
    public static final String SHAREDPREFERENCES_EMAIL = "Email";
    public static final String SHAREDPREFERENCES_LOGIN_EMAIL = "LoginEmail";
    public static final String SHAREDPREFERENCES_PASSWORD = "Name";
    public static final String SHAREDPREFERENCES_LOGIN_PASSWORD = "Login Name";

    // Singleton
    private UserInfo userInfo;
    Boolean preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Singleton
        userInfo = UserInfo.userInfo();

        // Welcome message, displays the user's name
        TextView text_displayName = (TextView) findViewById(R.id.text_name);
        text_displayName.setText(userInfo.getName());

        setButtons();
    }

    private void setButtons() {
        Button action_logOut = (Button) findViewById(R.id.button_logOut);
        Button btnMonitorUsers = (Button) findViewById(R.id.button_monitorGroup);
        Button btnWalkingGroups = (Button) findViewById(R.id.button_walkingGroup);
        Button btnRewardsCentre = (Button) findViewById(R.id.MMA_rewards_centre);

        // Checks to see if the user is trying to preview a theme
        Intent intent = getIntent();
        preview = intent.getBooleanExtra("preview", false);

        if (preview){
            action_logOut.setVisibility(View.GONE);
            btnMonitorUsers.setVisibility(View.GONE);
            btnWalkingGroups.setVisibility(View.GONE);
            btnRewardsCentre.setVisibility(View.GONE);
        }
        // Else, resume application functionality.
        else {
            // Log out button
            action_logOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Wipe saved user data
                    storeEmail_sharedPreferences();
                    storePassword_sharedPreferences();

                    // Returns user to the login screen
                    Intent logInScreen = new Intent(MainMenuActivity.this, LoginActivity.class);
                    startActivity(logInScreen);
                    finish();
                }
            });

            btnMonitorUsers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = DashBoardActivity.makeIntent(MainMenuActivity.this);
                    startActivity(intent);
                }
            });

            btnWalkingGroups.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = WalkingGroupsActivity.makeIntent(MainMenuActivity.this);
                    startActivity(intent);
                }
            });

            btnRewardsCentre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = RewardsCentreActivity.makeIntent(MainMenuActivity.this);
                    startActivity(intent);
                }
            });
        }
    }

    private void storeEmail_sharedPreferences(){
        // Wipes user email
        String extractedEmail = null;

        // Stores user email to be null
        SharedPreferences emailPref = getSharedPreferences(SHAREDPREFERENCES_EMAIL, MODE_PRIVATE);
        SharedPreferences.Editor emailEditor = emailPref.edit();
        emailEditor.putString(SHAREDPREFERENCES_LOGIN_EMAIL, extractedEmail);
        emailEditor.commit();
    }

    private void storePassword_sharedPreferences() {
        // Wipes user password
        String extractedPassword = null;

        // Stores user password to be null
        SharedPreferences passwordPref = getSharedPreferences(SHAREDPREFERENCES_PASSWORD, MODE_PRIVATE);
        SharedPreferences.Editor passwordEditor = passwordPref.edit();
        passwordEditor.putString(SHAREDPREFERENCES_LOGIN_PASSWORD, extractedPassword);
        passwordEditor.commit();
    }

    // Ensure main menu operates after preview finishes
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(preview) {
            preview = false;
        }
    }

    // Passes the preview value that determines whether buttons should be disabled or not.
    public static Intent makeIntent(Context context, Boolean preview){
        Intent intent = new Intent(context, MainMenuActivity.class);
        intent.putExtra("preview", preview);
        return intent;
    }

}
