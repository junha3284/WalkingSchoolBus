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
import com.jade.walkinggroupbus.walkingschoolbus.model.GroupsInfo;
import com.jade.walkinggroupbus.walkingschoolbus.model.SharedData;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class MyGroupDetailsActivity extends AppCompatActivity implements OnMapReadyCallback{

    private SharedData sharedData;
    private WGServerProxy proxy;
    private GoogleMap map;

    private UserInfo userInfo;

    private GroupsInfo groupsInfo = GroupsInfo.getInstance();
    private String groupName;
    private List<Double> groupCoordinates;

    private static final String TAG = "ServerTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_group_details);

        // instantiate vars
        userInfo = UserInfo.userInfo();
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

        // setup map
        getGroupLocationCoordinates();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_myGroupDetails);
        mapFragment.getMapAsync(this);

        // leave walking group
        leaveGroupButton();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // setup marker for selected group on map creation
        map = googleMap;
        Marker mapMarker = map.addMarker(new MarkerOptions()
                .position(new LatLng(groupCoordinates.get(0),groupCoordinates.get(1)))
                .title(groupName));
    }

    private void getGroupLocationCoordinates() {
        groupCoordinates = groupsInfo.getMeetingPlaceCoordinates(groupName);
    }

    private void updateListView() {
        List<UserInfo> groupMembers = groupsInfo.getMembers(groupName);
        List<String> groupMemberNames = new ArrayList<String>();

        for (UserInfo groupMember : groupMembers) {
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
        Button btnLeave = (Button) findViewById(R.id.button_leave_group);

        btnLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call proxy to leave group
                Long groupID = groupsInfo.getGroupID(groupName);
                Call<Void> caller = proxy.leaveGroup(groupID ,userInfo.getId());
                ProxyBuilder.callProxy(caller,returnNothing -> response(returnNothing));
            }
        });
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
