package com.jade.walkinggroupbus.walkingschoolbus;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jade.walkinggroupbus.walkingschoolbus.model.GroupsInfo;

public class JoinGroupActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap map;
    private GroupsInfo groupsInfo = GroupsInfo.getInstance();

    private static final int REQUEST_CODE_JOINGROUPDETAILS = 074;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_join_group);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        String[] groupNames = groupsInfo.getNames();

        for (int i = 0; i < groupNames.length; i++) {
            float groupCoordinates[] = groupsInfo.getCoordinates(groupNames[i]);
            Marker marker = map.addMarker(new MarkerOptions()
                            .position(new LatLng(groupCoordinates[0],groupCoordinates[1]))
                            .title(groupNames[i]));
        }
        map.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(JoinGroupActivity.this, JoinGroupDetailsActivity.class);
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
}
