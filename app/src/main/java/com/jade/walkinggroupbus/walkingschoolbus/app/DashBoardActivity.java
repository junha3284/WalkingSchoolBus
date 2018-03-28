package com.jade.walkinggroupbus.walkingschoolbus.app;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
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
import com.jade.walkinggroupbus.walkingschoolbus.R;

import java.util.List;

import retrofit2.Call;

public class DashBoardActivity extends AppCompatActivity {

    private UserInfo userInfo;
    private SharedData sharedData;
    private WGServerProxy proxy;

    private static final String TAG = "ServerTest";

    private List<UserInfo> monitoredUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        // produce the references for singleton objects
        userInfo = UserInfo.userInfo();
        sharedData = SharedData.getSharedData();

        String token = sharedData.getToken();

        // check if token is set properly
        if(token != null)
            proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), sharedData.getToken());
        else {
            ProxyBuilder.setOnTokenReceiveCallback(token1 -> onReceiveToken(token1));
        }

        // get data about monitored Users from the server and populate the ListView with that
        getMonitoredUsers();

        // set buttons
        setButtons();
        setOnClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setButtons();
        getMonitoredUsers();
    }

    private void setButtons() {
        Button btnMsg = (Button) findViewById(R.id.button_Message);
        btnMsg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = MessageActivity.makeIntent(DashBoardActivity.this);
                startActivity(intent);
            }
        });

        btnMsg.setText(getString(R.string.message) + " ( " + userInfo.getUnreadMessages().size() + " )");

        Button addBtn = (Button) findViewById(R.id.button_add);
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = AddMonitoredUserActivity.makeIntent(DashBoardActivity.this);
                startActivity(intent);
            }
        });
        Button refreshBtn = (Button) findViewById(R.id.button_refresh);
        refreshBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // get data about monitored Users from the server and populate the ListView with that
                getMonitoredUsers();
            }
        });

        Button myInfoBtn = (Button) findViewById(R.id.button_my_info);
        myInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( DashBoardActivity.this, MonitoredUserDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getMonitoredUsers(){
        Call<List<UserInfo>> caller = proxy.getMonitoredUsers(userInfo.getId());
        ProxyBuilder.callProxy(caller,returnedList -> response(returnedList));
    }

    private void response(List<UserInfo> returnedList) {
        userInfo.setMonitorsUsers(returnedList);

        ListView listMonitoredUsers = (ListView) findViewById(R.id.list_monitored_users);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.list_monitored_user,
                getMonitoredUserDescriptions());
        listMonitoredUsers.setAdapter(adapter);
    }

    // Convert List<UserInfo> into string which is usable for ListView Adapter
    private String[] getMonitoredUserDescriptions(){
        monitoredUsers = userInfo.getMonitorsUsers();
        int size = monitoredUsers.size();
        String[] description = new String[size];
        for(int i =0; i < size; i++)
            description[i] = monitoredUsers.get(i).toStringForList();
        return description;
    }

    private void setOnClickListeners(){
        ListView listMonitoredUsers = (ListView) findViewById(R.id.list_monitored_users);
        listMonitoredUsers.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View viewClicked, int position, long id) {
                UserInfo clickedUser = monitoredUsers.get(position);
                String name = clickedUser.getName();
                String email = clickedUser.getEmail();
                Long ID = clickedUser.getId();
                String birthYear = clickedUser.getBirthYear();
                String birthMonth = clickedUser.getBirthMonth();
                String address = clickedUser.getAddress();
                String cellPhone = clickedUser.getCellPhone();
                String homePhone = clickedUser.getHomePhone();
                String grade = clickedUser.getGrade();
                String teacherName = clickedUser.getTeacherName();
                String emergencyContact = clickedUser.getEmergencyContactInfo();

                userInfo.startManagingChild();

                Intent intent = MonitoredUserDetailActivity.makeIntent(DashBoardActivity.this,
                        name, email, ID, birthYear, birthMonth, address, cellPhone, homePhone, grade, teacherName, emergencyContact);
                startActivity(intent);
            }
        });
    }


    // general functions

    private void onReceiveToken(String token) {
        // Replace the current proxy with one that uses the token!
        Log.w(TAG, "   --> NOW HAVE TOKEN: " + token);
        proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), token);
        sharedData.setToken(token);
    }

    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, DashBoardActivity.class);
        return intent;
    }
}
