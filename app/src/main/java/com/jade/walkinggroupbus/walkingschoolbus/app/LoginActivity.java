package com.jade.walkinggroupbus.walkingschoolbus.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jade.walkinggroupbus.walkingschoolbus.R;
import com.jade.walkinggroupbus.walkingschoolbus.model.SharedData;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;

import java.util.List;

import retrofit2.Call;

public class LoginActivity extends AppCompatActivity {

    public static final String SHAREDPREFERENCES_EMAIL = "Email";
    public static final String SHAREDPREFERENCES_LOGIN_EMAIL = "LoginEmail";
    public static final String SHAREDPREFERENCES_PASSWORD = "Name";
    public static final String SHAREDPREFERENCES_LOGIN_PASSWORD = "Login Name";

    // Singleton Pattern implementation
    private UserInfo userInfo;
    private SharedData sharedData;

    public String inputPassword;
    public String inputEmail;

    // Server Details
    private static final String TAG = "ServerTest";
    private long userId = 0;
    private WGServerProxy proxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

            // Singleton
            userInfo = UserInfo.userInfo();
            sharedData = SharedData.getSharedData();

            // Build the server proxy
            proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), null);

            // Performs an auto-login with data from Shared Preferences (if applicable)
            action_checkAutoLogin();

            // Displays default user email and password (if applicable) in logcat. Use tag 'Login' (without apostrophe)
            Log.i("Login", "" + userInfo.getEmail());
            Log.i("Login", "" + userInfo.getPassword());

            // Launches Register activity
            Button button_btn = (Button) findViewById(R.id.button_register);
            button_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            });

            // Handles Login information
            Button mainmenu = (Button) findViewById(R.id.button_login);
            mainmenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Check fields for user input
                    EditText edit_password = (EditText) findViewById(R.id.edit_passField);
                    EditText edit_email = (EditText) findViewById(R.id.edit_userfield);

                    // Stores input fields into variables
                    inputPassword = edit_password.getText().toString();
                    inputEmail = edit_email.getText().toString();

                    // Check to see if any fields were left blank
                    if (edit_password.length() < 1 || edit_email.length() < 1){
                        action_errorMessage();
                    }
                            // Old error checking
                             //else if (inputEmail.equals(userInfo.getEmail()))
                             //if (inputPassword.equals(userInfo.getPassword()))
                    else {

                        // Set user info to the singleton, so the server may verify
                        userInfo.setEmail(inputEmail);
                        userInfo.setPassword(inputPassword);

                        // Register for token received:
                        ProxyBuilder.setOnTokenReceiveCallback(token -> onReceiveToken(token));

                        // Make call to server
                        Call<Void> caller = proxy.login(userInfo);
                        ProxyBuilder.callProxy(LoginActivity.this, caller, returnedNothing -> response(returnedNothing));
                    }

                }
            });

        }

    private void action_checkAutoLogin() {

        // Retrieve info in Shared Preferences (if applicable)
        userInfo.setEmail(action_getEmail());
        userInfo.setPassword(action_getPassword());

        // Checks to see if any info was stored in Shared Preferences
        if (userInfo.getEmail() != null && userInfo.getPassword() != null){

            // Register for token received:
            ProxyBuilder.setOnTokenReceiveCallback(token -> onReceiveToken(token));

            // Make call to server
            Call<Void> caller = proxy.login(userInfo);
            ProxyBuilder.callProxy(LoginActivity.this, caller, returnedNothing -> response(returnedNothing));
        }
    }

    private void action_errorMessage(){
            TextView text_error = (TextView) findViewById(R.id.text_errorLogin);
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
        // Retreives stored email (if applicable)
        SharedPreferences emailPref = getSharedPreferences(SHAREDPREFERENCES_EMAIL, MODE_PRIVATE);
        String extractEmail = emailPref.getString(SHAREDPREFERENCES_LOGIN_EMAIL, null);
        return extractEmail;
    }

    private String action_getPassword() {
        // Retrieve stored password (if applicable)
        SharedPreferences passwordPref = getSharedPreferences(SHAREDPREFERENCES_PASSWORD, MODE_PRIVATE);
        String extractPassword = passwordPref.getString(SHAREDPREFERENCES_LOGIN_PASSWORD, null);
        return extractPassword;
    }

    private void onReceiveToken(String token) {
        // Replace the current proxy with one that uses the token!
        Log.w(TAG, "   --> NOW HAVE TOKEN: " + token);
        proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), token);
        sharedData.setToken(token);
    }


    private void response(Void returnedNothing) {
        // Log message to signify successful login
        Log.w(TAG, "Server replied to login request.");

        // Store user info into Shared Preferences
        storeEmail_sharedPreferences();
        storePassword_sharedPreferences();

        // Displays stored user information on Logcat for testing purposes. Use tag 'ServerTest' in Logcat without apostrophes.
        setupListUsers();

        // Set userInfo and create MainMenu Activity
        LogIn();
    }

    private void response(List<UserInfo> returnedUsers) {
        Log.w(TAG, "All Users:");
        for (UserInfo user : returnedUsers) {
            Log.w(TAG, "    User: " + user.toString());
        }
    }

    private void response(UserInfo returnedUser) {
        // store data about the user from the server in userInfo
        Log.w(TAG, "Set userInfo basic fields:");
        userInfo.setId(returnedUser.getId());
        userInfo.setName(returnedUser.getName());
        userInfo.setEmail(returnedUser.getEmail());
        userInfo.setBirthYear(returnedUser.getBirthYear());
        userInfo.setBirthMonth(returnedUser.getBirthMonth());
        userInfo.setAddress(returnedUser.getAddress());
        userInfo.setCellPhone(returnedUser.getCellPhone());
        userInfo.setHomePhone(returnedUser.getHomePhone());
        userInfo.setGrade(returnedUser.getGrade());
        userInfo.setTeacherName(returnedUser.getTeacherName());
        userInfo.setEmergencyContactInfo(returnedUser.getEmergencyContactInfo());
        userInfo.setHref(returnedUser.getHref());
        userInfo.setLeadsGroups(returnedUser.getLeadsGroups());
        userInfo.setMemberOfGroups(returnedUser.getMemberOfGroups());
        userInfo.setMonitoredByUsers(returnedUser.getMonitoredByUsers());
        userInfo.setMonitorsUsers(returnedUser.getMonitorsUsers());

        // and create MainMenuActivity;
        action_mainMenu();
    }

    private void setupListUsers() {
        // Make call
        Call<List<UserInfo>> caller = proxy.getUsers();
        ProxyBuilder.callProxy(LoginActivity.this, caller, returnedUsers -> response(returnedUsers));
    }

    private void LogIn(){
        Call<UserInfo> caller = proxy.getUserByEmail(userInfo.getEmail());
        ProxyBuilder.callProxy(caller, returnedUser -> response(returnedUser));
    }


    private void storeEmail_sharedPreferences(){
        // Get user email
        String extractedEmail = userInfo.getEmail();

        // Store user email
        SharedPreferences emailPref = getSharedPreferences(SHAREDPREFERENCES_EMAIL, MODE_PRIVATE);
        SharedPreferences.Editor emailEditor = emailPref.edit();
        emailEditor.putString(SHAREDPREFERENCES_LOGIN_EMAIL, extractedEmail);
        emailEditor.commit();
    }

    private void storePassword_sharedPreferences() {
        // Grab user info
        String extractedPassword = userInfo.getPassword();

        // Store user info
        SharedPreferences passwordPref = getSharedPreferences(SHAREDPREFERENCES_PASSWORD, MODE_PRIVATE);
        SharedPreferences.Editor passwordEditor = passwordPref.edit();
        passwordEditor.putString(SHAREDPREFERENCES_LOGIN_PASSWORD, extractedPassword);
        passwordEditor.commit();
    }


}
