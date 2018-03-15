package com.jade.walkinggroupbus.walkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jade.walkinggroupbus.walkingschoolbus.model.ChildInfo;
import com.jade.walkinggroupbus.walkingschoolbus.model.GroupsInfo;
import com.jade.walkinggroupbus.walkingschoolbus.model.SharedData;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class MyGroupDetailsActivity extends AppCompatActivity {

    private SharedData sharedData;
    private WGServerProxy proxy;

    private UserInfo userInfo;
    private ChildInfo childInfo;

    private GroupsInfo groupsInfo = GroupsInfo.getInstance();
    private String groupName;

    private static final String TAG = "ServerTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_group_details);

        // instantiate vars
        userInfo = UserInfo.userInfo();
        childInfo = ChildInfo.childInfo();
        sharedData = SharedData.getSharedData();
        String token = sharedData.getToken();

        // check if token is set properly
        if(token != null)
            proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), sharedData.getToken());
        else {
            ProxyBuilder.setOnTokenReceiveCallback(token1 -> onReceiveToken(token1));
        }

        // display users in this group
        getIntentData();
        updateListView();

        mapButton();

        // leave walking group
        leaveGroupButton();
    }

    private void mapButton() {
        Button btnMap = (Button) findViewById(R.id.button_displayMap);

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyGroupDetailsActivity.this, MyGroupDetailsMapActivity.class);
                intent.putExtra("passedGroupName", groupName);
                startActivity(intent);
            }
        });
    }

    private void updateListView() {
        // get the names of the group members
        List<UserInfo> groupMembers = groupsInfo.getMembers(groupName);
        List<String> groupMemberNames = new ArrayList<String>();
        for (UserInfo groupMember : groupMembers) {
            groupMemberNames.add(groupMember.getName());
        }

        // build adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,                  // context
                R.layout.item_groups,        // layout to use (create)
                groupMemberNames);             // items to be displayed

        // configure list view
        ListView list = (ListView) findViewById(R.id.listView_groupMemebers);
        list.setAdapter(adapter);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        groupName = intent.getStringExtra("passedGroupName");
    }

    private void leaveGroupButton() {
        Button btnLeaveGroup = (Button) findViewById(R.id.button_leave_group);

        btnLeaveGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leaveGroup();
                finish();
            }
        });
    }

    private void leaveGroup() {
        Button btnLeave = (Button) findViewById(R.id.button_leave_group);

        btnLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call proxy to leave group
                Long groupID = groupsInfo.getGroupID(groupName);

                Long userID;
                if (userInfo.managingChild()) {
                    userID = childInfo.getId();
                } else {
                    userID = userInfo.getId();
                }

                Call<Void> caller = proxy.leaveGroup(groupID ,userID);
                ProxyBuilder.callProxy(caller,returnNothing -> response(returnNothing));

                // update our singleton
                Call<UserInfo> userInfoCall = proxy.getUserById(userID);
                ProxyBuilder.callProxy(userInfoCall, returnedUser -> response(returnedUser));

            }
        });
    }

    private void response(UserInfo returnedUser){
        // update our singleton
        if (userInfo.managingChild()) {
            childInfo.setChildInfo(returnedUser);
        } else {
            userInfo.setUserInfo(returnedUser);
        }
    }

    // call successful if nothing returned
    private void response(Void returnNothing){
        finish();
    }

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, MyGroupDetailsActivity.class);

        return intent;
    }

    private void onReceiveToken(String token) {
        // Replace the current proxy with one that uses the token!
        Log.w(TAG, "   --> NOW HAVE TOKEN: " + token);
        proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), token);
        sharedData.setToken(token);
    }
}
