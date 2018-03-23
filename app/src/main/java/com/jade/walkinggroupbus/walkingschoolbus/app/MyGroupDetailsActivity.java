package com.jade.walkinggroupbus.walkingschoolbus.app;

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

import com.jade.walkinggroupbus.walkingschoolbus.R;
import com.jade.walkinggroupbus.walkingschoolbus.model.ChildInfo;
import com.jade.walkinggroupbus.walkingschoolbus.model.GroupsInfo;
import com.jade.walkinggroupbus.walkingschoolbus.model.SharedData;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;

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

    private List<UserInfo> groupMembers;

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

        // iteration 2: view parents of child selected in list view
        listViewOnClick();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        groupName = intent.getStringExtra("passedGroupName");
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
        Long groupID = groupsInfo.getGroupID(groupName);
        // add group to child
        Call<List<UserInfo>> caller = proxy.getMembersOfGroup(groupID);
        ProxyBuilder.callProxy(MyGroupDetailsActivity.this, caller, returnedUsers -> responseMemberOfGroup(returnedUsers));
    }

    private void responseMemberOfGroup(List<UserInfo> members){
        groupMembers = members;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_template_members, getMonitoredUserDescriptions(members));

        ListView list = findViewById(R.id.listView_groupMemebers);
        list.setAdapter(adapter);
    }

    private String[] getMonitoredUserDescriptions(List<UserInfo> members){
        int size = members.size();
        String[] description = new String[size];
        for(int i =0; i < size; i++)
            description[i] = members.get(i).toStringForList();
        return description;
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
        Long groupID = groupsInfo.getGroupID(groupName);

        Long userID;
        if (userInfo.managingChild()) {
            userID = childInfo.getId();
        } else {
            userID = userInfo.getId();
        }
        Call<Void> caller = proxy.leaveGroup(groupID ,userID);
        ProxyBuilder.callProxy(caller,returnNothing -> response(returnNothing, userID));
     }

    // call successful if nothing returned
    private void response(Void returnNothing, Long userId){
        // update our singleton
        Call<UserInfo> userInfoCall = proxy.getUserById(userId);
        ProxyBuilder.callProxy(userInfoCall, returnedUser -> response(returnedUser));
    }

    private void response(UserInfo returnedUser){
        // update our singleton
        if (userInfo.managingChild()) {
            childInfo.setChildInfo(returnedUser);
        } else {
            userInfo.setUserInfo(returnedUser);
        }
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

    private void listViewOnClick() {
        ListView listViewMembers = (ListView) findViewById(R.id.listView_groupMemebers);

        listViewMembers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // move to parents info activity
                Intent intent = GroupMembersParentsActivity.makeIntent(MyGroupDetailsActivity.this);

                // pass group member name
                intent.putExtra("PassedGroupMemberID", groupMembers.get(position).getId());

                startActivity(intent);
            }
        });
    }
}
