package com.jade.walkinggroupbus.walkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jade.walkinggroupbus.walkingschoolbus.model.SharedData;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;

import java.lang.reflect.Proxy;
import java.util.List;

import retrofit2.Call;

public class AddMonitoredUserActivity extends AppCompatActivity {

    private static final String TAG = "ServerTest";

    private UserInfo userInfo;
    private SharedData sharedData;
    private WGServerProxy proxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_monitored_user);

        userInfo = UserInfo.userInfo();
        sharedData = SharedData.getSharedData();

        String token = sharedData.getToken();
        if(token != null)
            proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), sharedData.getToken());
        else {
            ProxyBuilder.setOnTokenReceiveCallback(token1 -> onReceiveToken(token1));
        }

        setAddBtn();
    }

    void setAddBtn(){
        Button btn_add = (Button) findViewById(R.id.button_add);
        btn_add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                EditText edit_name = (EditText) findViewById(R.id.edit_name);
                EditText edit_email = (EditText) findViewById(R.id.edit_email);
                String string_name = edit_name.getText().toString();
                String string_email = edit_email.getText().toString();
                //TODO: use string_email and string_use to validate user and add this person to this user's MonitorsUsers.
                // request the server to add the user
                addMonitoredUser(string_email);
            }
        });
    }

    public void addMonitoredUser(String email){
        // request the server to get the id of the email owner
        Call<UserInfo> caller = proxy.getUserByEmail(email);
        ProxyBuilder.callProxy(caller, returnedUser -> response(returnedUser));
    }

    private void response(UserInfo returnedUser) {
        // request the server to add MonitoredUser
        Call<List<UserInfo>> caller = proxy.addMonitoredUser(userInfo.getId(),returnedUser);
        ProxyBuilder.callProxy(caller,returnedList -> response(returnedList));
    }

    private void response(List<UserInfo> returnedList){
        userInfo.setMonitorsUsers(returnedList);
        finish();
    }

    private void onReceiveToken(String token) {
        // Replace the current proxy with one that uses the token!
        Log.w(TAG, "   --> NOW HAVE TOKEN: " + token);
        proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), token);
        sharedData.setToken(token);
    }

    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context,AddMonitoredUserActivity.class);
        return intent;
    }

}
