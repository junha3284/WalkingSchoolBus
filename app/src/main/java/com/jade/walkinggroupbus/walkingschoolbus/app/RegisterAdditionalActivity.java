package com.jade.walkinggroupbus.walkingschoolbus.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jade.walkinggroupbus.walkingschoolbus.R;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;

import retrofit2.Call;

public class RegisterAdditionalActivity extends AppCompatActivity {
    public String birth_year;
    public String birth_month;
    public String address;
    public String cell_phone;
    public String home_phone;
    public String grade;
    public String teacher_name;
    public String emergency_contact;

    // Singleton
    private UserInfo userInfo;

    // Server Details
    private static final String TAG = "ServerTest";
    private WGServerProxy proxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_additional);

        // Singleton
        userInfo = UserInfo.userInfo();

        // Build the server proxy
        proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), null);

        Button confirm_button = (Button) findViewById(R.id.button_confirm);
        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Store entered information
                EditText edit_birth_year = (EditText) findViewById(R.id.edit_birth_year);
                EditText edit_birth_month = (EditText) findViewById(R.id.edit_birth_month);
                EditText edit_address = (EditText) findViewById(R.id.edit_address);
                EditText edit_cell_phone = (EditText) findViewById(R.id.edit_cell_phone);
                EditText edit_home_phone = (EditText) findViewById(R.id.edit_home_phone);
                EditText edit_grade = (EditText) findViewById(R.id.edit_grade);
                EditText edit_teacher_name = (EditText) findViewById(R.id.edit_teacher_name);
                EditText edit_emergency_contact = (EditText) findViewById(R.id.edit_emergency_contact);

                // Get data from EditText box
                birth_year = edit_birth_year.getText().toString();
                birth_month = edit_birth_month.getText().toString();
                address = edit_address.getText().toString();
                cell_phone = edit_cell_phone.getText().toString();
                home_phone = edit_home_phone.getText().toString();
                grade = edit_grade.getText().toString();
                teacher_name = edit_teacher_name.getText().toString();
                emergency_contact = edit_emergency_contact.getText().toString();

                action_confirm_button();
            }
        });

        Button cancel_button = (Button) findViewById(R.id.button_cancel);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Return previous fields to be null
                userInfo.setPassword(null);
                userInfo.setEmail(null);
                userInfo.setName(null);
                finish();
            }
        });
    }

    // Following section checks for incorrect/empty fields
    private void action_confirm_button() {
        action_check_birth_month(birth_month);
        action_check_birth_year(birth_year);
        action_check_address(address);
        action_check_cell_phone(cell_phone);
        action_check_home_phone(home_phone);
        action_check_grade(grade);
        action_check_teacher_name(teacher_name);
        action_check_emergency_contact(emergency_contact);
    }

    private void action_check_birth_month(String birth_month) {
        if (birth_month.length() != 0){
            userInfo.setBirthMonth(birth_month);
        }
        else{
            String message = "Error, Birth Month field was left blank.";
            action_errorMessage(message);
        }
    }

    private void action_check_birth_year(String birth_year) {
        if (birth_year.length() != 0){
            userInfo.setBirthYear(birth_year);
        }
        else{
            String message = "Error, Birth Year field was left blank.";
            action_errorMessage(message);
        }
    }

    private void action_check_address(String address) {
        if (address.length() != 0){
            userInfo.setAddress(address);
        }
        else{
            String message = "Error, Address field was left blank.";
            action_errorMessage(message);
        }
    }

    private void action_check_cell_phone(String cell_phone) {
        if (cell_phone.length() != 0){
            userInfo.setCellPhone(cell_phone);
        }
        else{
            String message = "Error, Cell Phone field was left blank.";
            action_errorMessage(message);
        }
    }

    private void action_check_home_phone(String home_phone) {
        if (home_phone.length() != 0){
            userInfo.setHomePhone(home_phone);
        }
        else{
            String message = "Error, Home Phone field was left blank.";
            action_errorMessage(message);
        }
    }

    private void action_check_grade(String grade) {
        if (grade.length() != 0){
            userInfo.setGrade(grade);
        }
        else{
            String message = "Error, Grade field was left blank.";
            action_errorMessage(message);
        }
    }

    private void action_check_teacher_name(String teacher_name) {
        if (teacher_name.length() != 0){
            userInfo.setTeacherName(teacher_name);
        }
        else{
            String message = "Error, Grade field was left blank.";
            action_errorMessage(message);
        }
    }

    private void action_check_emergency_contact(String emergency_contact) {
        if (emergency_contact.length() != 0){

            userInfo.setEmergencyContactInfo(emergency_contact);
            // Display message to show successful registration
            Toast.makeText(RegisterAdditionalActivity.this, "Registration Successful.", Toast.LENGTH_LONG).show();

            // Make call to server to store data
            Call<UserInfo> caller = proxy.createNewUser(userInfo);
            ProxyBuilder.callProxy(RegisterAdditionalActivity.this, caller, returnedUser -> response(returnedUser));

            finish();
        }
        else{
            String message = "Error, Grade field was left blank.";
            action_errorMessage(message);
        }
    }

    private void action_errorMessage(String message) {
        TextView text_errorMessage = (TextView) findViewById(R.id.text_error);
        text_errorMessage.setText(message);
    }

    // Receive user identifier ID.
    private void response(UserInfo user) {
        Log.w(TAG, "Server replied with user: " + user.toString());
        userInfo.setId(user.getId());
    }

}
