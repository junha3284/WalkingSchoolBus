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
import com.jade.walkinggroupbus.walkingschoolbus.model.Group;
import com.jade.walkinggroupbus.walkingschoolbus.model.GroupsInfo;
import com.jade.walkinggroupbus.walkingschoolbus.model.SharedData;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * displays all walking groups current user is a member of + ones they lead
 * can see further details by clicking on the group
 * allows user to navigate to the create + join walking group activities
 */
public class WalkingGroupsActivity extends AppCompatActivity {

    private SharedData sharedData;
    private WGServerProxy proxy;

    private List<String> groupNames = new ArrayList<String>();
    private GroupsInfo groupsInfo = GroupsInfo.getInstance();

    // iteration 2:
    private List<String> groupLeadNames = new ArrayList<String>();

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

        if (userInfo.managingChild()) {
            disableCreateGroupButton();
        } else {
            createGroupButton();
        }

        Call<List<Group>> caller = proxy.getGroups();
        ProxyBuilder.callProxy(WalkingGroupsActivity.this,
                caller,
                returnedGroups -> setUpActivity(returnedGroups));
    }


    /*
        iteration 1:
        display groups the user is in to the screen
        on click of group in listview, show details (in the next activity)
    */

    private void disableCreateGroupButton() {
        Button btnCreateGroup = (Button) findViewById(R.id.button_create_group);

        btnCreateGroup.setVisibility(View.GONE);
    }

    private void createGroupButton() {
        Button btnCreateGroup = (Button) findViewById(R.id.button_create_group);

        btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WalkingGroupsActivity.this, CreateGroupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setUpActivity(List<Group> returnedGroups) {
        groupsInfo.setGroups(returnedGroups);

        if (userInfo.managingChild()) {
            getGroupNames(childInfo.getMemberOfGroups());
            getGroupLeadNames(childInfo.getLeadsGroups());

            refreshListView();

        } else {
            getGroupNames(userInfo.getMemberOfGroups());
            getGroupLeadNames(userInfo.getLeadsGroups());

            refreshListView();
        }

        // when someone clicks a group
        registerClickCallback();
        setButtons();
    }

    private void getGroupNames(List<Group> memberOfGroups) {
        groupNames.clear();
        for (Group group : memberOfGroups){
            groupNames.add(groupsInfo.getNameByID(group.getId()));
        }
    }

    private void refreshListView() {
        // build adapter for joined Groups
        ArrayAdapter<String> adapterJoinedGroups = new ArrayAdapter<String>(
                this,
                R.layout.list_template_members,
                groupNames);

        // configure list view
        ListView listJoinedGroups = (ListView) findViewById(R.id.listView_my_groups);
        listJoinedGroups.setAdapter(adapterJoinedGroups);

        // build adapter for lead Groups
        ArrayAdapter<String> adapterLeadGroups = new ArrayAdapter<String>(
                this,
                R.layout.list_template_members,
                groupLeadNames);

        // configure list view
        ListView listLeadGroups = (ListView) findViewById(R.id.listView_leaderGroups);
        listLeadGroups.setAdapter(adapterLeadGroups);
    }

    private void registerClickCallback() {
        // set up onclick listener with Joined Groups List View
        ListView listJoinedGroups = (ListView) findViewById(R.id.listView_my_groups);

        listJoinedGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // id of what was clicked
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id ) {

                // MOVE TO MY GROUP DETAILS. PASS GROUP NAME
                Intent intent = MyGroupDetailsActivity.makeIntent(WalkingGroupsActivity.this);

                intent.putExtra("passedGroupName", groupNames.get(position));
                startActivity(intent);
            }
        });


        // set up onclick listener with Lead Groups List View
        ListView listLeadGroups = (ListView) findViewById(R.id.listView_leaderGroups);

        listLeadGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // id of what was clicked
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id ) {

                // MOVE TO MY GROUP DETAILS. PASS GROUP NAME
                Intent intent = MyGroupDetailsActivity.makeIntent(WalkingGroupsActivity.this);

                intent.putExtra("passedGroupName", groupLeadNames.get(position));
                startActivity(intent);
            }
        });
    }

    private void setButtons() {
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

        Button btnRefresh = (Button) findViewById(R.id.button_refresh);
        btnRefresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (userInfo.managingChild()) {
                    getGroupNames(childInfo.getMemberOfGroups());
                    getGroupLeadNames(childInfo.getLeadsGroups());
                    refreshListView();
                } else {
                    getGroupNames(userInfo.getMemberOfGroups());
                    getGroupLeadNames(userInfo.getLeadsGroups());
                    refreshListView();
                }
            }
        });
    }

    /*
        iteration 2:
        groups i lead is also visible - same functionality as iteration 1
    */

    // look into user and see what groups he leads
    // if he leads a group, then pull the info from the server

    private void getGroupLeadNames(List<Group> leadsGroups) {
        groupLeadNames.clear();
        for (Group group : leadsGroups){
            groupLeadNames.add(groupsInfo.getNameByID(group.getId()));
        }
    }


    // general functions

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
