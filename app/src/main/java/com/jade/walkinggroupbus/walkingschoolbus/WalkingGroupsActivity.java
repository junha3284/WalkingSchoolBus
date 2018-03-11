package com.jade.walkinggroupbus.walkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    private GroupsInfo groupsInfo = GroupsInfo.getInstance();

    private static final String TAG = "ServerTest";

    private List<Group> myGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking_groups);

        // setup shared data
        sharedData = SharedData.getSharedData();
        String token = sharedData.getToken();

        // check if token is set properly
        if(token != null)
            proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), sharedData.getToken());
        else {
            ProxyBuilder.setOnTokenReceiveCallback(token1 -> onReceiveToken(token1));
        }


        // when someone clicks a group
        registerClickCallback();

        getGroupNames();

        // display the data to the list view
        refreshListView();
    }

    private void getGroupNames() {
        Call<List<Group>> caller = proxy.getGroups();
        ProxyBuilder.callProxy(WalkingGroupsActivity.this, caller, returnedGroups -> response(returnedGroups));
    }

    private void response(List<Group> returnedGroups) {
        groupsInfo.setGroups(returnedGroups);
    }

    private void refreshListView() {
        List<String> groupNames = groupsInfo.getNames();


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

                // do stuff when list on item is clicked
                // DEBUG
                TextView textView = (TextView) viewClicked;
                String message = "You clicked on item # " + position + 1
                        + ", which is: " + textView.getText().toString();
                Toast.makeText(WalkingGroupsActivity.this, message, Toast.LENGTH_LONG).show();;


                // MOVE TO CALCULATE SERVING SIZE SCREEN;
                //ArrayList passedGroup = fakeGroupArray.get(position);

                Intent intent = MyGroupDetailsActivity.makeIntent(WalkingGroupsActivity.this);
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
