package com.jade.walkinggroupbus.walkingschoolbus.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jade.walkinggroupbus.walkingschoolbus.R;
import com.jade.walkinggroupbus.walkingschoolbus.model.MyRewards;
import com.jade.walkinggroupbus.walkingschoolbus.model.SharedData;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * allows current user to view the parental contact information
 * of a specific group memeber
 */

public class GroupMembersParentsActivity extends AppCompatActivity {

    private SharedData sharedData;
    private WGServerProxy proxy;
    private static final String TAG = "ServerTest";

    private Long groupMemberID;
    private List<UserInfo> groupMemberParentIDs;
    private List<UserInfo> groupMemberParents = new ArrayList<UserInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyRewards myRewards = MyRewards.MyRewards();
        setTheme(myRewards.getSelectedThemeID());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_members_parents);

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
        updateListViewWithParentInfo();
        refreshListView();

        setRefreshButton();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        groupMemberID = intent.getLongExtra("PassedGroupMemberID", -1);
    }

    private void updateListViewWithParentInfo() {
        // call server for user info using ID
        Call<UserInfo> caller = proxy.getUserById(groupMemberID);
        ProxyBuilder.callProxy(GroupMembersParentsActivity.this, caller, returnedUser -> response(returnedUser));
    }

    private void response(UserInfo returnedUser){
        groupMemberParentIDs = returnedUser.getMonitoredByUsers();

        // change title text
        TextView tvTitle = (TextView) findViewById(R.id.text_title);
        tvTitle.append(" " + returnedUser.getName());

        // get group members parents from ID
        getGroupMembersParentsData(groupMemberParentIDs);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.list_template_members,
                getParentsDescriptions());

        ListView list = findViewById(R.id.listView_parents);
        list.setAdapter(adapter);
    }

    private void getGroupMembersParentsData(List<UserInfo> groupMemberParentIDs) {
        // SERVER CALL FOR MONITOREDBYUSERS ONLY RETURNS THEIR ID! SO CALL SERVER FOR THEIR INFO EACH TIME
        int size = groupMemberParentIDs.size();
        for(int i = 0; i < size; i++) {
            // server call for parent info
            Call<UserInfo> caller = proxy.getUserById(groupMemberParentIDs.get(i).getId());
            ProxyBuilder.callProxy(GroupMembersParentsActivity.this,
                    caller,
                    returnedUser -> responseParent(returnedUser));
        }
    }

    private void responseParent(UserInfo returnedUser) {
        groupMemberParents.add(returnedUser);
        refreshListView();
    }

    private String[] getParentsDescriptions(){
        int size = groupMemberParents.size();
        String[] description = new String[size];
        for(int i = 0; i < size; i++) {
            description[i] = groupMemberParents.get(i).toStringContactInfo();
        }
            
        return description;
    }

    private void refreshListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.list_template_members,
                getParentsDescriptions());

        ListView list = findViewById(R.id.listView_parents);
        list.setAdapter(adapter);
    }

    private void setRefreshButton() {
        Button btnRefresh = (Button) findViewById(R.id.button_refresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshListView();
            }
        });
    }

    // general functions
    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, GroupMembersParentsActivity.class);
        return intent;
    }

    private void onReceiveToken(String token) {
        // Replace the current proxy with one that uses the token!
        Log.w(TAG, "   --> NOW HAVE TOKEN: " + token);
        proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), token);
        sharedData.setToken(token);
    }
}
