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
import com.jade.walkinggroupbus.walkingschoolbus.model.SharedData;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;

import retrofit2.Call;

public class ModifyMonitoredUsersActivity extends AppCompatActivity {

    public static final String RESULT_KEY_MONITORED_USER_ID = "com.jade.walkinggroupbus.walkingschoolbus-monitored_user_id";

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
    private SharedData sharedData;
    private UserInfo userInfo;

    private WGServerProxy proxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_monitored_users);

        childInfo = ChildInfo.childInfo();
        sharedData = SharedData.getSharedData();
        userInfo = UserInfo.userInfo();

        Intent intent = getIntent();
        Long id = intent.getLongExtra(RESULT_KEY_MONITORED_USER_ID, 0);

        String token = sharedData.getToken();
        if(token != null)
            proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), sharedData.getToken());
        else {
            ProxyBuilder.setOnTokenReceiveCallback(token1 -> onReceiveToken(token1));
        }

        Button saveBtn = (Button) findViewById(R.id.button_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getEditTextFields();

                confirmFields();

                finish();
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
    }

    private void confirmFields() {

        if (userInfo.managingChild()) {
            // Get the child you want to monitor
            Intent intent = getIntent();
            Call<UserInfo> caller = proxy.getUserById(intent.getLongExtra(RESULT_KEY_MONITORED_USER_ID, 0));
            ProxyBuilder.callProxy(caller, returnedUser -> getChild(returnedUser));

            // Edit their information
            if (email.length() != 0) {
                childInfo.setEmail(email);
            }
            if (name.length() != 0) {
                childInfo.setName(name);
            }
            if (password1.length() != 0 && password1.equals(password2)) {
                childInfo.setPassword(password1);
            }
            if (birth_month.length() != 0) {
                childInfo.setBirthMonth(birth_month);
            }
            if (birth_year.length() != 0) {
                childInfo.setBirthYear(birth_year);
            }
            if (address.length() != 0) {
                childInfo.setAddress(address);
            }
            if (cell_phone.length() != 0) {
                childInfo.setCellPhone(cell_phone);
            }
            if (home_phone.length() != 0) {
                childInfo.setHomePhone(home_phone);
            }
            if (grade.length() != 0) {
                childInfo.setGrade(grade);
            }
            if (teacher_name.length() != 0) {
                childInfo.setTeacherName(teacher_name);
            }
            if (emergency_contact.length() != 0) {
                childInfo.setEmergencyContactInfo(emergency_contact);
            }
            Call<UserInfo> editUser = proxy.editUser(intent.getLongExtra(RESULT_KEY_MONITORED_USER_ID, 0), childInfo);
            ProxyBuilder.callProxy(editUser, returnedUser -> response(returnedUser));
        }
        else{
            // Edit their information
            if (email.length() != 0) {
                userInfo.setEmail(email);
            }
            if (name.length() != 0) {
                userInfo.setName(name);
            }
            if (password1.length() != 0 && password1.equals(password2)) {
                userInfo.setPassword(password1);
            }
            if (birth_month.length() != 0) {
                userInfo.setBirthMonth(birth_month);
            }
            if (birth_year.length() != 0) {
                userInfo.setBirthYear(birth_year);
            }
            if (address.length() != 0) {
                userInfo.setAddress(address);
            }
            if (cell_phone.length() != 0) {
                userInfo.setCellPhone(cell_phone);
            }
            if (home_phone.length() != 0) {
                userInfo.setHomePhone(home_phone);
            }
            if (grade.length() != 0) {
                userInfo.setGrade(grade);
            }
            if (teacher_name.length() != 0) {
                userInfo.setTeacherName(teacher_name);
            }
            if (emergency_contact.length() != 0) {
                userInfo.setEmergencyContactInfo(emergency_contact);
            }
            Call<UserInfo> editUser = proxy.editUser(userInfo.getId(), childInfo);
            ProxyBuilder.callProxy(editUser, returnedUser -> response(returnedUser));
        }
    }

    private void getChild(UserInfo returnedUser){
        childInfo.setChildInfo(returnedUser);
    }

    public static Intent makeIntent(Context context, Long id) {

        Intent temp = new Intent(context, ModifyMonitoredUsersActivity.class);
        temp.putExtra(RESULT_KEY_MONITORED_USER_ID, id);

        return temp;
    }

    private void response(UserInfo returnedUser) {
        Log.w(TAG, "    User: " + returnedUser);
    }

    private void onReceiveToken(String token) {
        // Replace the current proxy with one that uses the token!
        Log.w(TAG, "   --> NOW HAVE TOKEN: " + token);
        proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), token);
        sharedData.setToken(token);
    }
}
