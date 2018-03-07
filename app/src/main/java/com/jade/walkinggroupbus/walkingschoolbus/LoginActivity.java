package com.jade.walkinggroupbus.walkingschoolbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;

public class LoginActivity extends AppCompatActivity {

        // Singleton Pattern implementation
        private UserInfo userInfo;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            userInfo = UserInfo.userInfo();

            // Launches Register activity
            Button btn = (Button) findViewById(R.id.register);
            btn.setOnClickListener(new View.OnClickListener() {
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
                    EditText password = (EditText) findViewById(R.id.passField);
                    EditText email = (EditText) findViewById(R.id.userfield);

                    String inputPassword = password.getText().toString();
                    String inputEmail = email.getText().toString();

                    // Check to see if any fields were left blank
                    if (password.length() < 1 || email.length() < 1){
                        errorMessage();
                    }
                    else if (inputPassword.equals(userInfo.getPassword()) && inputEmail.equals(userInfo.getEmail())){

                        // Pass login to Singleton for the rest of the application to use
                        userInfo.setEmail(inputEmail);
                        userInfo.setName(inputPassword);

                        // Launch Main Menu activity
                        Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    // Handles incorrect user information
                    else{
                        errorMessage();
                    }
                }
            });

        }
        private void errorMessage(){
            TextView error = (TextView) findViewById(R.id.errorLogin);
            String message = "Error, there is an incorrect field.";
            error.setText(message);
        }
    }

