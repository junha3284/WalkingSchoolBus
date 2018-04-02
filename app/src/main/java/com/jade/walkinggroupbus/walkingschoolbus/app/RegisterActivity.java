package com.jade.walkinggroupbus.walkingschoolbus.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jade.walkinggroupbus.walkingschoolbus.R;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;

import retrofit2.Call;

public class RegisterActivity extends AppCompatActivity {

    // Singleton
    private UserInfo userInfo;

    private static final String TAG = "ServerTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Singleton
        userInfo = UserInfo.userInfo();

        // Takes user input, verifies, then passes to server
        Button button_confirm = (Button) findViewById(R.id.button_next);
        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Store entered information
                EditText edit_passwordEntry = (EditText) findViewById(R.id.edit_enterPass);
                EditText edit_passwordConfirm = (EditText) findViewById(R.id.edit_confirmPass);
                EditText edit_emailEntry = (EditText) findViewById(R.id.edit_enterEmail);
                EditText edit_nameEntry = (EditText) findViewById(R.id.edit_enterName);

                // Get data from EditText box
                String password1 = edit_passwordEntry.getText().toString();
                String password2 = edit_passwordConfirm.getText().toString();
                String email = edit_emailEntry.getText().toString();
                String name = edit_nameEntry.getText().toString();

                // Check for empty/incorrect fields
                action_confirmEmail(email);
                action_confirmName(name);
                action_confirmPassword(password1, password2);
            }
        });

        // Brings user back to login screen
        Button button_cancel = (Button) findViewById(R.id.button_cancel);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void action_confirmEmail(String email) {
        if (email.length() != 0){
            userInfo.setEmail(email);
        }
        else{
            String message = "Error, email field was left blank.";
            action_errorMessage(message);
        }
    }

    private void action_confirmName(String name) {
        if (name.length() != 0){
            userInfo.setName(name);
        }
        else{
            String message = "Error, name field was left blank.";
            action_errorMessage(message);
        }
    }

    private void action_confirmPassword(String password1, String password2) {

        // Checks to see if password field was left blank
        if (password1.length() < 1 || password2.length() < 1){
            String message = "Error, passwords do not match.";
            action_errorMessage(message);
        }
        else if (password1.equals(password2)){

            // Store user info in Singleton Pattern
            userInfo.setPassword(password1);

            Intent intent = new Intent(RegisterActivity.this, RegisterAdditionalActivity.class);
            startActivity(intent);

            finish();
        }
        else{
            String message = "Error, a field was left blank";
            action_errorMessage(message);
        }
    }

    // Handles error messages
    private void action_errorMessage(String message){
        TextView text_errorMessage = (TextView) findViewById(R.id.text_error);
        text_errorMessage.setText(message);
    }

    // Receive user identifier ID.
    private void response(UserInfo user) {
        Log.w(TAG, "Server replied with user: " + user.toString());
        userInfo.setId(user.getId());
    }
}
