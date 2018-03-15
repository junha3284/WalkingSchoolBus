package com.jade.walkinggroupbus.walkingschoolbus.app;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jade.walkinggroupbus.walkingschoolbus.R;
import com.jade.walkinggroupbus.walkingschoolbus.model.Group;
import com.jade.walkinggroupbus.walkingschoolbus.model.GroupsInfo;
import com.jade.walkinggroupbus.walkingschoolbus.model.SharedData;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;

import java.util.List;

import retrofit2.Call;

public class CreateGroupActivity extends AppCompatActivity {

    private Float meetingPlaceLat = null;
    private Float meetingPlaceLng = null;
    private Float destinationLat = null;
    private Float destinationLng = null;
    private String groupDescription = "";

    UserInfo user = UserInfo.userInfo();
    GroupsInfo groupsInfo = GroupsInfo.getInstance();

    private WGServerProxy proxy;
    private SharedData sharedData;
    private static final String TAG = "ServerTest";
    private static final int REQUEST_CODE_CREATEGROUPMAP = 02;

    private static final String MEETING_PLACE_LAT = "meeting place latitude";
    private static final String MEETING_PLACE_LNG = "meeting place longitude";
    private static final String DESTINATION_LAT = "destination latitude";
    private static final String DESTINATION_LNG = "destination longitude";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        sharedData = SharedData.getSharedData();
        String token = sharedData.getToken();
        // check if token is set properly
        if(token != null)
            proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), sharedData.getToken());
        else {
            ProxyBuilder.setOnTokenReceiveCallback(token1 -> onReceiveToken(token1));
        }

        setLocations();

        createGroup();
    }

    private void setLocations() {
        Button btn = findViewById(R.id.button_set_locations);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateGroupActivity.this, CreateGroupMapActivity.class);
                if (meetingPlaceLat != null && meetingPlaceLng != null &&
                    destinationLat != null && destinationLng != null) {
                    intent.putExtra(MEETING_PLACE_LAT, meetingPlaceLat.floatValue());
                    intent.putExtra(MEETING_PLACE_LNG, meetingPlaceLng.floatValue());
                    intent.putExtra(DESTINATION_LAT, destinationLat.floatValue());
                    intent.putExtra(DESTINATION_LNG, destinationLng.floatValue());
                }
                startActivityForResult(intent, REQUEST_CODE_CREATEGROUPMAP);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CREATEGROUPMAP:
                if (resultCode == Activity.RESULT_OK) {
                    meetingPlaceLat = data.getFloatExtra(MEETING_PLACE_LAT,0);
                    meetingPlaceLng = data.getFloatExtra(MEETING_PLACE_LNG,0);
                    destinationLat = data.getFloatExtra(DESTINATION_LAT,0);
                    destinationLng = data.getFloatExtra(DESTINATION_LNG,0);
                }
        }
    }

    private void createGroup() {
        Button btn = findViewById(R.id.button_create_group);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = findViewById(R.id.edit_group_description);
                groupDescription = editText.getText().toString();

                if (meetingPlaceLat != null && meetingPlaceLng != null &&
                    destinationLat != null && destinationLng != null &&
                    !(groupDescription.equals(""))) {
                    // create group
                    Double[] latArray = {meetingPlaceLat.doubleValue(), destinationLat.doubleValue()};
                    Double[] lngArray = {meetingPlaceLng.doubleValue(), destinationLng.doubleValue()};

                    Group newGroup = new Group();
                    newGroup.setGroupDescription(groupDescription);
                    newGroup.setRouteLatArray(latArray);
                    newGroup.setRouteLngArray(lngArray);
                    newGroup.setLeader(user);

                    // due to 500 error -- fix later
                    newGroup.setId(new Long(-1));

                    Call<Group> caller = proxy.createGroup(newGroup);
                    ProxyBuilder.callProxy(CreateGroupActivity.this, caller, returnedGroup -> update(returnedGroup));

                    finish();
                }
                else {
                    if (meetingPlaceLat == null && meetingPlaceLng == null &&
                        destinationLat == null && destinationLng == null) {
                        Toast.makeText(CreateGroupActivity.this, "Set meeting and destination locations", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(CreateGroupActivity.this, "Add a group description", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void update(Group returnedGroup) {
        // update groups
        Call<List<Group>> group_caller = proxy.getGroups();
        ProxyBuilder.callProxy(CreateGroupActivity.this, group_caller, returnedGroups -> updateGroups(returnedGroups));

        // update user
        Call<UserInfo> user_caller = proxy.getUserById(user.getId());
        ProxyBuilder.callProxy(CreateGroupActivity.this, user_caller, returnedUser -> updateUser(returnedUser));
    }

    private void updateGroups(List<Group> returnedGroups) {
        groupsInfo.setGroups(returnedGroups);
    }

    private void updateUser(UserInfo returnedUser) {
        user.setEmail(returnedUser.getEmail());
        user.setHref(returnedUser.getHref());
        user.setId(returnedUser.getId());
        user.setLeadsGroups(returnedUser.getLeadsGroups());
        user.setMemberOfGroups(returnedUser.getMemberOfGroups());
        user.setMonitoredByUsers(returnedUser.getMonitoredByUsers());
        user.setMonitorsUsers(returnedUser.getMonitorsUsers());
        user.setName(returnedUser.getName());
        user.setPassword(returnedUser.getPassword());
    }

    private void onReceiveToken(String token) {
        Log.w(TAG, "   --> NOW HAVE TOKEN: " + token);
        proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), token);
        sharedData.setToken(token);
    }
}
