package com.jade.walkinggroupbus.walkingschoolbus.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jade.walkinggroupbus.walkingschoolbus.R;
import com.jade.walkinggroupbus.walkingschoolbus.model.MyRewards;
import com.jade.walkinggroupbus.walkingschoolbus.model.SharedData;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;

/**
 * activity where the current user can add other users to their monitor list
 * by providing their email + name
 */
public class AddMonitoredUserActivity extends AppCompatActivity {

    private static final String TAG = "ServerTest";

    private UserInfo userInfo;
    private SharedData sharedData;
    private WGServerProxy proxy;

    private String string_name;
    private String string_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set theme
        MyRewards myRewards = MyRewards.MyRewards();
        setTheme(myRewards.getSelectedThemeID());

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
                string_name = edit_name.getText().toString();
                string_email = edit_email.getText().toString();

                // request the server to add the user
                addMonitoredUser(string_email);
            }
        });
    }

    public void addMonitoredUser(String email){
        // request the server to get the id of the email owner
        Call<UserInfo> caller = proxy.getUserByEmail(email);
        ProxyBuilder.callProxy(caller, returnedUser -> response(returnedUser));
        finish();
    }

    private void response(UserInfo returnedUser) {
        // request the server to add MonitoredUser
        if(returnedUser.getName().equals(string_name)) {
            Call<List<UserInfo>> caller = proxy.addMonitoredUser(userInfo.getId(), returnedUser);
            ProxyBuilder.callProxy(caller,returnedList -> response(returnedList));
        }
        else {
            Toast.makeText(AddMonitoredUserActivity.this,"Name is not matched with the email",Toast.LENGTH_SHORT).show();
        }
    }

    private void response(List<UserInfo> returnedList){
        userInfo.setMonitorsUsers(returnedList);
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
