package com.jade.walkinggroupbus.walkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    private static final String TAG = "ServerTest";

    private UserInfo userInfo;
    private SharedData sharedData;
    private ChildInfo childInfo;

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
            proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), sharedData.getToken());
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
        text_name.setText(intent.getStringExtra(RESULT_KEY_USER_NAME));
        text_email.setText(intent.getStringExtra(RESULT_KEY_USER_EMAIL));
    }

    private void setBtns() {
        Button removeBtn = (Button) findViewById(R.id.button_remove);
        Button walkingGroupBtn = (Button) findViewById(R.id.button_create_WalkingGroupActivity);
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
                //todo: connect to WalkingGroupsActivity of the user who is the owner of the detailed info

                // update child singleton
                Intent intent = getIntent();
                Call<UserInfo> caller = proxy.getUserById(intent.getLongExtra(RESULT_KEY_MONITORED_USER_ID, 0));
                ProxyBuilder.callProxy(caller, returnedUser -> response(returnedUser));

                // activate for use
                childInfo.activateUser();

                Intent WGAIntent = WalkingGroupsActivity.makeIntent(MonitoredUserDetailActivity.this);
                startActivity(WGAIntent);

                // when returning to this page from WalkingGroupActivity, we must deactivate child
                childInfo.deactivateUser();
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
    }

    private void response(UserInfo returnedUser){
        childInfo.setChildInfo(returnedUser);
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

    public static Intent makeIntent(Context context, String name, String email, Long id) {
        Intent temp = new Intent(context, MonitoredUserDetailActivity.class);
        temp.putExtra(RESULT_KEY_USER_NAME, name);
        temp.putExtra(RESULT_KEY_USER_EMAIL, email);
        temp.putExtra(RESULT_KEY_MONITORED_USER_ID, id);
        return temp;
    }
}
