package com.jade.walkinggroupbus.walkingschoolbus.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jade.walkinggroupbus.walkingschoolbus.R;
import com.jade.walkinggroupbus.walkingschoolbus.model.ChildInfo;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;

import retrofit2.Call;

public class ModifyMonitoredUsersActivity extends AppCompatActivity {

    public static final String RESULT_KEY_USER_NAME = "com.jade.walkinggroupbus.walkingschoolbus-name";
    public static final String RESULT_KEY_USER_EMAIL = "com.jade.walkinggroupbus.walkingschoolbus-email";
    public static final String RESULT_KEY_MONITORED_USER_ID = "com.jade.walkinggroupbus.walkingschoolbus-monitored_user_id";
    public static final String RESULT_KEY_USER_BIRTH_YEAR = "com.jade.walkinggroupbus.walkingschoolbus-birth_year";
    public static final String RESULT_KEY_USER_BIRTH_MONTH = "com.jade.walkinggroupbus.walkingschoolbus-birth_month";
    public static final String RESULT_KEY_USER_ADDRESS = "com.jade.walkinggroupbus.walkingschoolbus-address";
    public static final String RESULT_KEY_USER_CELL_PHONE = "com.jade.walkinggroupbus.walkingschoolbus-cell_phone";
    public static final String RESULT_KEY_USER_HOME_PHONE = "com.jade.walkinggroupbus.walkingschoolbus-home_phone";
    public static final String RESULT_KEY_USER_GRADE = "com.jade.walkinggroupbus.walkingschoolbus-grade";
    public static final String RESULT_KEY_USER_TEACHER_NAME = "com.jade.walkinggroupbus.walkingschoolbus-teacher_name";
    public static final String RESULT_KEY_USER_EMERGENCY_CONTACT_INFORMATION = "com.jade.walkinggroupbus.walkingschoolbus-emeregency_contact_information";

    private static final String TAG = "ServerTest";

    public String email;
    public String name;
    public String password1;
    public String password2;
    public String birth_year;
    public String birth_month;
    public String address;
    public String cell_phone;
    public String home_phone;
    public String grade;
    public String teacher_name;
    public String emergency_contact;

    private ChildInfo childInfo;
    private UserInfo userInfo;

    private Long id;

    private WGServerProxy proxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_monitored_users);

        childInfo = ChildInfo.childInfo();
        userInfo = UserInfo.userInfo();

        Button saveBtn = (Button) findViewById(R.id.button_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEditTextFields();

                Intent intent = getIntent();

                Log.w("modified", "    User: " + childInfo.toString());

                // Make call to server to store data
                Call<UserInfo> caller = proxy.editUser(intent.getLongExtra(RESULT_KEY_MONITORED_USER_ID, 0), childInfo);
                ProxyBuilder.callProxy(ModifyMonitoredUsersActivity.this, caller, returnedUser -> response(returnedUser));
            }
        });
    }

    private void getEditTextFields() {
        // Store entered information
        EditText edit_emailEntry = (EditText) findViewById(R.id.edit_enterEmail);
        EditText edit_nameEntry = (EditText) findViewById(R.id.edit_enterName);
        EditText edit_passwordEntry = (EditText) findViewById(R.id.edit_enterPass);
        EditText edit_passwordConfirm = (EditText) findViewById(R.id.edit_confirmPass);
        EditText edit_birth_year = (EditText) findViewById(R.id.edit_birth_year);
        EditText edit_birth_month = (EditText) findViewById(R.id.edit_birth_month);
        EditText edit_address = (EditText) findViewById(R.id.edit_address);
        EditText edit_cell_phone = (EditText) findViewById(R.id.edit_cell_phone);
        EditText edit_home_phone = (EditText) findViewById(R.id.edit_home_phone);
        EditText edit_grade = (EditText) findViewById(R.id.edit_grade);
        EditText edit_teacher_name = (EditText) findViewById(R.id.edit_teacher_name);
        EditText edit_emergency_contact = (EditText) findViewById(R.id.edit_emergency_contact);

        // Get data from EditText box
        email = edit_emailEntry.getText().toString();
        name = edit_nameEntry.getText().toString();
        password1 = edit_passwordEntry.toString();
        password2 = edit_passwordConfirm.toString();

        birth_year = edit_birth_year.getText().toString();
        birth_month = edit_birth_month.getText().toString();
        address = edit_address.getText().toString();
        cell_phone = edit_cell_phone.getText().toString();
        home_phone = edit_home_phone.getText().toString();
        grade = edit_grade.getText().toString();
        teacher_name = edit_teacher_name.getText().toString();
        emergency_contact = edit_emergency_contact.getText().toString();

        confirmFields();
    }

    private void confirmFields() {
        Intent intent = getIntent();
        if (email.length() == 0){
            email = intent.getStringExtra(RESULT_KEY_USER_EMAIL);
        }
        else{
            childInfo.setEmail(email);
        }
        if (name.length() == 0){
            name = intent.getStringExtra(RESULT_KEY_USER_NAME);
        }
        else{
            childInfo.setName(name);
        }
        if (password1.length() != 0 && password1.equals(password2)){
            childInfo.setPassword(password1);
        }

        if (birth_month.length() == 0){
            birth_month = intent.getStringExtra(RESULT_KEY_USER_BIRTH_MONTH);
        }
        else{
            childInfo.setBirthMonth(birth_month);
        }
        if (birth_year.length() == 0){
            birth_year = intent.getStringExtra(RESULT_KEY_USER_BIRTH_YEAR);
        }
        else{
            childInfo.setBirthYear(birth_year);
        }
        if (address.length() == 0){
            address = intent.getStringExtra(RESULT_KEY_USER_ADDRESS);
        }
        else{
            childInfo.setAddress(address);
        }
        if (cell_phone.length() == 0){
            cell_phone = intent.getStringExtra(RESULT_KEY_USER_CELL_PHONE);
        }
        else{
            childInfo.setCellPhone(cell_phone);
        }
        if (home_phone.length() == 0){
            home_phone = intent.getStringExtra(RESULT_KEY_USER_HOME_PHONE);
        }
        else{
            childInfo.setHomePhone(home_phone);
        }

        if (grade.length() == 0){
            grade = intent.getStringExtra(RESULT_KEY_USER_GRADE);
        }
        else{
            childInfo.setGrade(grade);
        }
        if (teacher_name.length() == 0){
            teacher_name = intent.getStringExtra(RESULT_KEY_USER_TEACHER_NAME);
        }
        else{
            childInfo.setTeacherName(teacher_name);
        }
        if (emergency_contact.length() == 0){
            emergency_contact = intent.getStringExtra(RESULT_KEY_USER_EMERGENCY_CONTACT_INFORMATION);
        }
        else{
            childInfo.setEmergencyContactInfo(emergency_contact);
        }
    }


    public static Intent makeIntent(Context context, String name, String email, Long id,
                                    String birthYear, String birthMonth, String address, String cellPhone,
                                    String homePhone, String grade, String teacherName, String emergencyContact) {

        Intent temp = new Intent(context, ModifyMonitoredUsersActivity.class);
        temp.putExtra(RESULT_KEY_USER_NAME, name);
        temp.putExtra(RESULT_KEY_USER_EMAIL, email);
        temp.putExtra(RESULT_KEY_MONITORED_USER_ID, id);
        temp.putExtra(RESULT_KEY_USER_BIRTH_YEAR, birthYear);
        temp.putExtra(RESULT_KEY_USER_BIRTH_MONTH, birthMonth);
        temp.putExtra(RESULT_KEY_USER_ADDRESS, address);
        temp.putExtra(RESULT_KEY_USER_CELL_PHONE, cellPhone);
        temp.putExtra(RESULT_KEY_USER_HOME_PHONE, homePhone);
        temp.putExtra(RESULT_KEY_USER_GRADE, grade);
        temp.putExtra(RESULT_KEY_USER_TEACHER_NAME, teacherName);
        temp.putExtra(RESULT_KEY_USER_EMERGENCY_CONTACT_INFORMATION, emergencyContact);

        return temp;
    }

    private void response(UserInfo returnedUser) {
        Log.w(TAG, "Edit Successful");
    }
}
