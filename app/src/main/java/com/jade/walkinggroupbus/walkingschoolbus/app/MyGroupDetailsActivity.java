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
import android.widget.ImageView;
import android.widget.ListView;

import com.jade.walkinggroupbus.walkingschoolbus.R;
import com.jade.walkinggroupbus.walkingschoolbus.model.ChildInfo;
import com.jade.walkinggroupbus.walkingschoolbus.model.Group;
import com.jade.walkinggroupbus.walkingschoolbus.model.GroupsInfo;
import com.jade.walkinggroupbus.walkingschoolbus.model.MyRewards;
import com.jade.walkinggroupbus.walkingschoolbus.model.SharedData;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;

import java.util.List;

import retrofit2.Call;

/**
 * allows current user to view details of a specific group and also :
 * navigate to an activity that starts a walk
 * navigate to an activity to see group meeting + destination location
 * leave walking group
 * navigate to an activity to view a specific member's parental info activity
 */

public class MyGroupDetailsActivity extends AppCompatActivity {

    private SharedData sharedData;
    private WGServerProxy proxy;

    private UserInfo userInfo;
    private ChildInfo childInfo;

    private GroupsInfo groupsInfo = GroupsInfo.getInstance();
    private String groupName;

    private List<UserInfo> groupMembers;

    private UserInfo groupLeader;
    private Group group;

    private static final String TAG = "ServerTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyRewards myRewards = MyRewards.MyRewards();
        setTheme(myRewards.getSelectedThemeID());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_group_details);

        // set background
        if (!myRewards.getSelectedTheme().equals("Default")
                && !myRewards.getSelectedTheme().equals("Dark")) {
            ImageView img = (ImageView) findViewById(R.id.imageView);
            img.setImageResource(myRewards.getSelectedImgID());
        } else {
            // if theme is default or dark, disable filter
            View filter = (View) findViewById(R.id.filter);
            filter.setVisibility(View.GONE);
        }


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

        getIntentData();

        // list views
        updateListViewMembers();
        updateListViewLeader();
        ListViewsOnClick();

        // setup buttons
        leaveGroupButton();
        mapButton();
        refreshButton();
        startWalkButton();
    }

    private void updateListViewMembers() {
        Long groupID = groupsInfo.getGroupID(groupName);

        // add group to child
        Call<List<UserInfo>> caller = proxy.getMembersOfGroup(groupID);
        ProxyBuilder.callProxy(MyGroupDetailsActivity.this,
                caller,
                returnedUsers -> responseMemberOfGroup(returnedUsers));
    }

    private void responseMemberOfGroup(List<UserInfo> members){
        groupMembers = members;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.list_template_members,
                getGroupMemberDescriptions(members));

        ListView list = findViewById(R.id.listView_groupMembers);
        list.setAdapter(adapter);
    }

    private String[] getGroupMemberDescriptions(List<UserInfo> members){
        int size = members.size();
        String[] description = new String[size];
        for(int i =0; i < size; i++)
            description[i] = members.get(i).toStringForList();
        return description;
    }

    private void updateListViewLeader() {
        getGroupLeaderID();
        Call<UserInfo> caller = proxy.getUserById(groupLeader.getId());
        ProxyBuilder.callProxy(MyGroupDetailsActivity.this, caller, returnedUser -> responseLeader(returnedUser));
    }

    private void getGroupLeaderID() {
        Long groupID = groupsInfo.getGroupID(groupName);
        group = groupsInfo.getGroupByID(groupID);
        groupLeader = group.getLeader();
    }

    private void responseLeader(UserInfo returnedUser){
        groupLeader.setUserInfo(returnedUser);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.list_template_members,
                getLeaderDescription(returnedUser));

        ListView list = findViewById(R.id.listView_groupLeader);
        list.setAdapter(adapter);
    }

    private String[] getLeaderDescription(UserInfo returnedUser) {
        String[] leaderDescription = new String[1];
        leaderDescription[0] = groupLeader.toStringForList();
        return leaderDescription;
    }

    private void ListViewsOnClick() {
        ListView listViewMembers = (ListView) findViewById(R.id.listView_groupMembers);

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

        ListView listViewLeader = (ListView) findViewById(R.id.listView_groupLeader);

        listViewLeader.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // move to parents info activity
                Intent intent = GroupMembersParentsActivity.makeIntent(MyGroupDetailsActivity.this);

                // pass group member name
                intent.putExtra("PassedGroupMemberID", groupLeader.getId());

                startActivity(intent);
            }
        });
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

    private void refreshButton() {
        Button btnRefresh = (Button) findViewById(R.id.button_refresh);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateListViewMembers();
                updateListViewLeader();
            }
        });
    }

    private void startWalkButton() {
        Button btnStartWalk = findViewById(R.id.button_startWalk);

        btnStartWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Long groupID = groupsInfo.getGroupID(groupName);
                Intent intent = OnWalkMapActivity.makeIntent(MyGroupDetailsActivity.this, groupID);

                // start OnWalkMapActivity
                startActivity(intent);
            }
        });
    }



    // general functions
    private void getIntentData() {
        Intent intent = getIntent();
        groupName = intent.getStringExtra("passedGroupName");
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
