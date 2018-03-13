package com.jade.walkinggroupbus.walkingschoolbus;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jade.walkinggroupbus.walkingschoolbus.model.Group;
import com.jade.walkinggroupbus.walkingschoolbus.model.GroupsInfo;
import com.jade.walkinggroupbus.walkingschoolbus.model.SharedData;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;

import java.util.List;

import retrofit2.Call;

public class JoinGroupMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private GroupsInfo groupsInfo = GroupsInfo.getInstance();

    private String groupName;
    private WGServerProxy proxy;
    private SharedData sharedData;
    private static final String TAG = "ServerTest";
    private static final int REQUEST_CODE_JOINGROUPDETAILS = 074;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group_map);

        sharedData = SharedData.getSharedData();
        String token = sharedData.getToken();
        // check if token is set properly
        if(token != null)
            proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), sharedData.getToken());
        else {
            ProxyBuilder.setOnTokenReceiveCallback(token1 -> onReceiveToken(token1));
        }

        setUpGroups();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void setUpGroups() {
        Call<List<Group>> caller = proxy.getGroups();
        ProxyBuilder.callProxy(JoinGroupMapActivity.this, caller, returnedGroups -> response(returnedGroups));
    }

    private void response(List<Group> returnedGroups) {
        groupsInfo.setGroups(returnedGroups);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        List<String> groupNames = groupsInfo.getNames();

        for (int i = 0; i < groupNames.size(); i++) {
            List<Double> groupCoordinates = groupsInfo.getMeetingPlaceCoordinates(groupNames.get(i));
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(groupCoordinates.get(0),groupCoordinates.get(1)))
                    .title(groupNames.get(i)));
        }
        mMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(JoinGroupMapActivity.this, JoinGroupDetailsActivity.class);
        intent.putExtra("group name", marker.getTitle());
        startActivityForResult(intent, REQUEST_CODE_JOINGROUPDETAILS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_JOINGROUPDETAILS:
                if (resultCode == Activity.RESULT_OK) {
                    finish();
                    break;
                }
        }
    }

    private void onReceiveToken(String token) {
        // Replace the current proxy with one that uses the token!
        Log.w(TAG, "   --> NOW HAVE TOKEN: " + token);
        proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), token);
        sharedData.setToken(token);
    }
}
