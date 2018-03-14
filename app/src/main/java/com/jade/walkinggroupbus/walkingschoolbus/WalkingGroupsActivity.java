package com.jade.walkinggroupbus.walkingschoolbus;

import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.jade.walkinggroupbus.walkingschoolbus.model.ChildInfo;
import com.jade.walkinggroupbus.walkingschoolbus.model.Group;
import com.jade.walkinggroupbus.walkingschoolbus.model.GroupsInfo;
import com.jade.walkinggroupbus.walkingschoolbus.model.SharedData;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class WalkingGroupsActivity extends AppCompatActivity {

    private SharedData sharedData;
    private WGServerProxy proxy;

    private List<String> groupNames = new ArrayList<String>();
    private GroupsInfo groupsInfo = GroupsInfo.getInstance();

    private UserInfo userInfo;
    private ChildInfo childInfo;

    private static final String TAG = "ServerTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking_groups);

        // setup shared data.
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

        if (childInfo.isActive()){
            getGroupNames(childInfo.getChild());
            refreshListView(childInfo.getChild());
        } else {
            getGroupNames(userInfo);
            refreshListView(userInfo);
        }

        // when someone clicks a group
        registerClickCallback();
        joinButton();
    }

    private void getGroupNames(UserInfo user) {
        for (Group group : user.getMemberOfGroups()){
            groupNames.add(group.getGroupDescription());
        }
    }

    private void joinButton() {
        Button btnJoinGroups = (Button) findViewById(R.id.button_join_walking_group);

        btnJoinGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // move to join walking group page
                Intent intent = new Intent(WalkingGroupsActivity.this , JoinGroupMapActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void refreshListView(UserInfo user) {
        // build adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,                  // context
                R.layout.item_groups,        // layout to use (create)
                groupNames);             // items to be displayed

        // configure list view
        ListView list = (ListView) findViewById(R.id.listView_my_groups);
        list.setAdapter(adapter);
    }

    private void registerClickCallback() {
        // set up onclick listener with list view
        ListView list = (ListView) findViewById(R.id.listView_my_groups);

        // configure listener to do what i want
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // id of what was clicked
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id ) {

                // MOVE TO MY GROUP DETAILS. PASS GROUP NAME
                Intent intent = MyGroupDetailsActivity.makeIntent(WalkingGroupsActivity.this);

                intent.putExtra("passedGroupName", groupNames.get(position));
                startActivity(intent);
            }
        });
    }

    private void onReceiveToken(String token) {
        // Replace the current proxy with one that uses the token!
        Log.w(TAG, "   --> NOW HAVE TOKEN: " + token);
        proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), token);
        sharedData.setToken(token);
    }

    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, WalkingGroupsActivity.class);
        return intent;
    }
}
