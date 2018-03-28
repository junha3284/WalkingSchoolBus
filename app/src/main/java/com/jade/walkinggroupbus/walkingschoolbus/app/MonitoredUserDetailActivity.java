package com.jade.walkinggroupbus.walkingschoolbus.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jade.walkinggroupbus.walkingschoolbus.R;
import com.jade.walkinggroupbus.walkingschoolbus.model.ChildInfo;
import com.jade.walkinggroupbus.walkingschoolbus.model.SharedData;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;

import retrofit2.Call;

public class MonitoredUserDetailActivity extends AppCompatActivity {

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

    private static final String TAG = "ChildInfo";

    private UserInfo userInfo;
    private SharedData sharedData;
    private ChildInfo childInfo;
    private UserInfo modifiedUserInfo;

    private WGServerProxy proxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitored_user_detail);

        userInfo = UserInfo.userInfo();
        sharedData = SharedData.getSharedData();
        childInfo = ChildInfo.childInfo();

        String token = sharedData.getToken();
        if(token != null)
            proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), sharedData.getToken(), "1");
        else {
            ProxyBuilder.setOnTokenReceiveCallback(token1 -> onReceiveToken(token1));
        }


        // set Texts which present the data about the MonitoredUser
        setTexts();

        setBtns();

    }


    public void setTexts(){
        Intent intent = getIntent();
        TextView text_name = (TextView) findViewById(R.id.text_name);
        TextView text_email = (TextView) findViewById(R.id.text_email);
        TextView text_birth_year = (TextView) findViewById(R.id.text_birth_year);
        TextView text_birth_month = (TextView) findViewById(R.id.text_birth_month);
        TextView text_cell_phone = (TextView) findViewById(R.id.text_cell_phone);
        TextView text_home_phone = (TextView) findViewById(R.id.text_home_phone);
        TextView text_address = (TextView) findViewById(R.id.text_address);
        TextView text_grade = (TextView) findViewById(R.id.text_grade);
        TextView text_teacher_name = (TextView) findViewById(R.id.text_teacher_name);
        TextView text_emergency_contact = (TextView) findViewById(R.id.text_emergency_contact);

        if (userInfo.managingChild()) {
            text_name.setText(intent.getStringExtra(RESULT_KEY_USER_NAME));
            text_email.setText(intent.getStringExtra(RESULT_KEY_USER_EMAIL));
            text_birth_year.setText(intent.getStringExtra(RESULT_KEY_USER_BIRTH_YEAR));
            text_birth_month.setText(intent.getStringExtra(RESULT_KEY_USER_BIRTH_MONTH));
            text_cell_phone.setText(intent.getStringExtra(RESULT_KEY_USER_CELL_PHONE));
            text_home_phone.setText(intent.getStringExtra(RESULT_KEY_USER_HOME_PHONE));
            text_address.setText(intent.getStringExtra(RESULT_KEY_USER_ADDRESS));
            text_grade.setText(intent.getStringExtra(RESULT_KEY_USER_GRADE));
            text_teacher_name.setText(intent.getStringExtra(RESULT_KEY_USER_TEACHER_NAME));
            text_emergency_contact.setText(intent.getStringExtra(RESULT_KEY_USER_EMERGENCY_CONTACT_INFORMATION));
        }
        else {
            text_name.setText(userInfo.getName());
            text_email.setText(userInfo.getEmail());
            text_birth_year.setText(userInfo.getBirthYear());
            text_birth_month.setText(userInfo.getBirthMonth());
            text_cell_phone.setText(userInfo.getCellPhone());
            text_home_phone.setText(userInfo.getHomePhone());
            text_address.setText(userInfo.getAddress());
            text_grade.setText(userInfo.getGrade());
            text_teacher_name.setText(userInfo.getTeacherName());
            text_emergency_contact.setText(userInfo.getEmergencyContactInfo());

            // Hides buttons if user is looking at their own data
            Button btnRemove = (Button) findViewById(R.id.button_remove);
            Button btnWalkingGroups = (Button) findViewById(R.id.button_create_WalkingGroupActivity);
            btnRemove.setVisibility(View.GONE);
            btnWalkingGroups.setVisibility(View.GONE);
        }

    }

    private void setBtns() {
        Button removeBtn = (Button) findViewById(R.id.button_remove);
        Button walkingGroupBtn = (Button) findViewById(R.id.button_create_WalkingGroupActivity);
        Button modifyBtn = (Button) findViewById(R.id.button_modify);
        Button viewUserBtn = (Button) findViewById(R.id.button_view_user);

        removeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // removed MonitoredUser( the owner of detailed Info) from the User's MonitorsUsers
                removeMonitoredUser();
            }
        });
        walkingGroupBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                // update child singleton
                Intent intent = getIntent();
                Call<UserInfo> caller = proxy.getUserById(intent.getLongExtra(RESULT_KEY_MONITORED_USER_ID, 0));
                ProxyBuilder.callProxy(caller, returnedUser -> response(returnedUser));

                // activate for use
                userInfo.startManagingChild();

                Intent WGAIntent = WalkingGroupsActivity.makeIntent(MonitoredUserDetailActivity.this);
                startActivity(WGAIntent);
            }
        });

        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = getIntent();

                Long ID = intent.getLongExtra(RESULT_KEY_MONITORED_USER_ID, 0);
                Log.w("ID", "" + ID);

                Intent modifyActivity = ModifyMonitoredUsersActivity.makeIntent(MonitoredUserDetailActivity.this, ID);
               startActivity(modifyActivity);
            }
        });

        viewUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // update child singleton
                Intent intent = getIntent();
                Call<UserInfo> caller = proxy.getUserById(intent.getLongExtra(RESULT_KEY_MONITORED_USER_ID, 0));
                ProxyBuilder.callProxy(caller, returnedUser -> response(returnedUser));

                // activate for use
                userInfo.startManagingChild();

                Intent viewUserIntent = ViewMonitoredUserMapActivity.makeIntent(MonitoredUserDetailActivity.this);
                startActivity(viewUserIntent);
            }
        });
    }

    private void removeMonitoredUser(){
        //get the id of one of the MonitoredUsers, who is the owner of the Detailed Info
        Intent intent = getIntent();
        Long monitoredUserId = intent.getLongExtra(RESULT_KEY_MONITORED_USER_ID,-1);

        // request to the server for deleting the monitoredUser from the MonitorsUsers
        Call<Void> caller = proxy.deleteMonitoredUser(userInfo.getId(), monitoredUserId);
        ProxyBuilder.callProxy(caller,returnNothing -> response(returnNothing));
        finish();
    }

    private void response(UserInfo returnedUser){
        childInfo.setChildInfo(returnedUser);
        Log.w("ChildInfo", "    User: " + childInfo.toString());
    }

    private void response(Void returnNothing){
        finish();
    }

    private void onReceiveToken(String token) {
        // Replace the current proxy with one that uses the token!
        Log.w(TAG, "   --> NOW HAVE TOKEN: " + token);
        proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), token);
        sharedData.setToken(token);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(userInfo.managingChild())
            userInfo.stopManagingChild();
    }

    public static Intent makeIntent(Context context, String name, String email, Long id,
                                    String birthYear, String birthMonth, String address, String cellPhone,
                                    String homePhone, String grade, String teacherName, String emergencyContact) {

        Intent temp = new Intent(context, MonitoredUserDetailActivity.class);
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
}
