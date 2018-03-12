package com.jade.walkinggroupbus.walkingschoolbus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jade.walkinggroupbus.walkingschoolbus.model.GroupsInfo;

import java.util.List;

/**
 * Created by brand on 2018-03-11.
 */

public class MyGroupDetailsMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;

    private String groupName;
    private GroupsInfo groupsInfo = GroupsInfo.getInstance();
    private List<Double> groupCoordinates;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // setup marker for selected group on map creation
        Log.i("App", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> onMapReady");
        map = googleMap;

        getIntentData();
        getGroupLocationCoordinates();

        // test
        LatLng groupLocation = new LatLng(groupCoordinates.get(0), groupCoordinates.get(1));
        map.addMarker(new MarkerOptions()
                .position(groupLocation)
                .title(groupName));
        map.moveCamera(CameraUpdateFactory.newLatLng(groupLocation));

        /*
        // initial map testing
        LatLng sydney = new LatLng(-34, 151);
        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */
    }

    private void getIntentData() {
        Intent intent = getIntent();
        groupName = intent.getStringExtra("passedGroupName");
    }

    private void getGroupLocationCoordinates() {
        groupCoordinates = groupsInfo.getMeetingPlaceCoordinates(groupName);
    }
}
