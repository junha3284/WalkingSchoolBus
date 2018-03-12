package com.jade.walkinggroupbus.walkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.jade.walkinggroupbus.walkingschoolbus.model.GroupsInfo;
import com.jade.walkinggroupbus.walkingschoolbus.model.SharedData;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class MyGroupDetailsActivity extends AppCompatActivity {

    private SharedData sharedData;
    private WGServerProxy proxy;
    private GroupsInfo groupsInfo = GroupsInfo.getInstance();
    private String groupName;

    private static final String TAG = "ServerTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_group_details);

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

        // TODO: display map
        // get map markers
        // fix map on map marker

        // leave walking group
        leaveGroupButton();


    }

    private void updateListView() {
        List<UserInfo> groupMemebers = groupsInfo.getMembers(groupName);
        List<String> groupMemberNames = new ArrayList<String>();

        for (UserInfo groupMember : groupMemebers) {
            // populate array list with member names
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
        // TODO: make user leave selected group
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
