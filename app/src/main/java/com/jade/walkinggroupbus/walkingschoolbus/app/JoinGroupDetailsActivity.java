package com.jade.walkinggroupbus.walkingschoolbus.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.jade.walkinggroupbus.walkingschoolbus.R;
import com.jade.walkinggroupbus.walkingschoolbus.model.ChildInfo;
import com.jade.walkinggroupbus.walkingschoolbus.model.GroupsInfo;
import com.jade.walkinggroupbus.walkingschoolbus.model.MyRewards;
import com.jade.walkinggroupbus.walkingschoolbus.model.SharedData;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * allows current user to see the members of a specific group
 * and also join the group
 */
public class JoinGroupDetailsActivity extends AppCompatActivity {

    private GroupsInfo groupsInfo = GroupsInfo.getInstance();
    private UserInfo user = UserInfo.userInfo();
    private ChildInfo child = ChildInfo.childInfo();

    private String groupName;

    private WGServerProxy proxy;
    private SharedData sharedData;
    private static final String TAG = "ServerTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyRewards myRewards = MyRewards.MyRewards();
        setTheme(myRewards.getSelectedThemeID());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group_details);

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


        sharedData = SharedData.getSharedData();
        String token = sharedData.getToken();
        // check if token is set properly
        if(token != null)
            proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), sharedData.getToken());
        else {
            ProxyBuilder.setOnTokenReceiveCallback(token1 -> onReceiveToken(token1));
        }

        getIntentData();

        setGroupText();

        setJoinGroupButton();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        groupName = intent.getStringExtra("group name");
    }

    private void setGroupText() {
        Long groupID = groupsInfo.getGroupID(groupName);
            // add group to child
        Call<List<UserInfo>> caller = proxy.getMembersOfGroup(groupID);
        ProxyBuilder.callProxy(JoinGroupDetailsActivity.this, caller, returnedUsers -> responseMemberOfGroup(returnedUsers));

    }

    private void responseMemberOfGroup(List<UserInfo> members){

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_template_members, getMonitoredUserDescriptions(members));

        ListView list = findViewById(R.id.list_members);
        list.setAdapter(adapter);
    }

    private String[] getMonitoredUserDescriptions(List<UserInfo> members){
        int size = members.size();
        String[] description = new String[size];
        for(int i =0; i < size; i++)
            description[i] = members.get(i).toStringForList();
        return description;
    }


    private void setJoinGroupButton() {
        Button button = findViewById(R.id.button_join);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Long groupID = groupsInfo.getGroupID(groupName);
                if (user.managingChild()) {
                    // add group to child
                    Call<List<UserInfo>> caller = proxy.addNewMemberOfGroup(groupID, child);
                    ProxyBuilder.callProxy(JoinGroupDetailsActivity.this, caller, returnedUsers -> response(returnedUsers));
                }
                else {
                    // add group to user
                    Call<List<UserInfo>> caller = proxy.addNewMemberOfGroup(groupID, user);
                    ProxyBuilder.callProxy(JoinGroupDetailsActivity.this, caller, returnedUsers -> response(returnedUsers));
                }

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void response(List<UserInfo> returnedUsers) {
        groupsInfo.setMembers(groupName, returnedUsers);
        // update user/child
        Long id;
        if (user.managingChild()) {
            id = child.getId();
        }
        else {
            id = user.getId();
        }
        Call<UserInfo> caller = proxy.getUserById(id);
        ProxyBuilder.callProxy(JoinGroupDetailsActivity.this, caller, returnedUser -> update(returnedUser));
    }

    private void update(UserInfo returnedUser) {
        if (user.managingChild()) {
            child.setChildInfo(returnedUser);
        }
        else {
            user.setUserInfo(returnedUser);
        }
    }

    private void onReceiveToken(String token) {
        // Replace the current proxy with one that uses the token!
        Log.w(TAG, "   --> NOW HAVE TOKEN: " + token);
        proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), token);
        sharedData.setToken(token);
    }
}
