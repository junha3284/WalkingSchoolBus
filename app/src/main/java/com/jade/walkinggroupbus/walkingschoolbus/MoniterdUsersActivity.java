package com.jade.walkinggroupbus.walkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.jade.walkinggroupbus.walkingschoolbus.model.SharedData;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class MoniterdUsersActivity extends AppCompatActivity {

    private UserInfo userInfo;
    private SharedData sharedData;
    private WGServerProxy proxy;

    private static final String TAG = "ServerTest";

    private List<UserInfo> monitoredUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moniterd_users);

        userInfo = UserInfo.userInfo();
        sharedData = SharedData.getSharedData();

        String token = sharedData.getToken();
        if(token != null)
            proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), sharedData.getToken());
        else {
            ProxyBuilder.setOnTokenReceiveCallback(token1 -> onReceiveToken(token1));
        }

        getMonitoredUsers();
        setOnClickListeners();
        setBtns();
    }

    private void getMonitoredUsers(){
        Call<List<UserInfo>> caller = proxy.getMonitoredUsers(userInfo.getId());
        ProxyBuilder.callProxy(caller,returnedList -> response(returnedList));
    }

    private void response(List<UserInfo> returnedList) {
        userInfo.setMonitorsUsers(returnedList);
        updateListView();
    }

    private void updateListView(){
        ListView listMonitoredUsers = (ListView) findViewById(R.id.list_monitored_users);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_moniotred_user, getMonitoredUserDescriptions());
        listMonitoredUsers.setAdapter(adapter);
    }

    private void setOnClickListeners(){
        ListView listMoniotredUsers = (ListView) findViewById(R.id.list_monitored_users);
        listMoniotredUsers.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View viewClicked, int position, long id) {
                UserInfo clickedUser = monitoredUsers.get(position);
                String name = clickedUser.getName();
                String email = clickedUser.getEmail();
                Long ID = clickedUser.getId();
                Intent intent = MonitoredUserDetailActivity.makeIntent(MoniterdUsersActivity.this, name, email, ID);
                startActivity(intent);
            }
        });
    }

    private void setBtns(){
        Button addBtn = (Button) findViewById(R.id.button_add);
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = AddMonitoredUserActivity.makeIntent(MoniterdUsersActivity.this);
                startActivity(intent);
            }
        });
        Button refreshBtn = (Button) findViewById(R.id.button_refresh);
        refreshBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                getMonitoredUsers();
            }
        });
    }

    private String[] getMonitoredUserDescriptions(){
        monitoredUsers = userInfo.getMonitorsUsers();
        int size = monitoredUsers.size();
        String[] description = new String[size];
        for(int i =0; i < size; i++)
            description[i] = monitoredUsers.get(i).toStringForList();
        return description;
    }

    private void onReceiveToken(String token) {
        // Replace the current proxy with one that uses the token!
        Log.w(TAG, "   --> NOW HAVE TOKEN: " + token);
        proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), token);
        sharedData.setToken(token);
    }

    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context,  MoniterdUsersActivity.class);
        return intent;
    }
}
