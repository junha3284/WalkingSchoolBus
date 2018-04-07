package com.jade.walkinggroupbus.walkingschoolbus.app;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jade.walkinggroupbus.walkingschoolbus.R;
import com.jade.walkinggroupbus.walkingschoolbus.model.ChildInfo;
import com.jade.walkinggroupbus.walkingschoolbus.model.GPSLocation;
import com.jade.walkinggroupbus.walkingschoolbus.model.Group;
import com.jade.walkinggroupbus.walkingschoolbus.model.SharedData;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * allows user to view their monitored user's last uploaded location
 */

public class ViewMonitoredUserMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    UserInfo userInfo = UserInfo.userInfo();
    ChildInfo childInfo = ChildInfo.childInfo();

    private WGServerProxy proxy;
    private SharedData sharedData;
    private static final String TAG = "ServerTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_monitored_user_map);

        sharedData = SharedData.getSharedData();
        String token = sharedData.getToken();
        // check if token is set properly
        if(token != null)
            proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), sharedData.getToken(), "1");
        else {
            ProxyBuilder.setOnTokenReceiveCallback(token1 -> onReceiveToken(token1));
        }

        // TODO:: delete
        /*
        getIntentData();
        */

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void onReceiveToken(String token) {
        // Replace the current proxy with one that uses the token!
        Log.w(TAG, "   --> NOW HAVE TOKEN: " + token);
        proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), token);
        sharedData.setToken(token);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (!userInfo.managingChild()) {
            displayMonitoredUsers();
        }
        else {
            displayChildLeaders();
        }
    }

    private void displayMonitoredUsers() {
        // get monitored users
        Call<List<UserInfo>> caller = proxy.getMonitoredUsers(userInfo.getId());
        ProxyBuilder.callProxy(caller, monitoredUsers -> markAllUsers(monitoredUsers));
    }

    private void markAllUsers(List<UserInfo> monitoredUsers) {

        // save markers to adjust map camera
        List<Marker> markerList = new ArrayList<>();

        for (UserInfo user : monitoredUsers) {

            GPSLocation userLocation = user.getLastGpsLocation();

            if (userLocation != null && userLocation.getLat() != null && userLocation.getLat() != null) {
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(userLocation.getLat(), userLocation.getLng()))
                        .title(user.getName())
                        .snippet(userLocation.getTimestamp().toString())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                markerList.add(marker);
            }
        }

        // set camera to position of first monitored user
        if (markerList.size() > 0) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerList.get(0).getPosition(), 7));
        }
        else {
            Toast.makeText(this,
                    "No monitored user locations found",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void displayChildLeaders() {

        GPSLocation childLocation = childInfo.getLastGpsLocation();

        if (childLocation != null && childLocation.getLat() != null && childLocation.getLat() != null) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(childLocation.getLat(), childLocation.getLng()))
                    .title(childInfo.getName())
                    .snippet(childLocation.getTimestamp().toString())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),11));
        }
        else {
            Toast.makeText(ViewMonitoredUserMapActivity.this,
                    "No location data for child",
                    Toast.LENGTH_LONG).show();
        }

        for (Group group : childInfo.getMemberOfGroups()) {
            Call<UserInfo> caller = proxy.getUserById(group.getLeader().getId());
            ProxyBuilder.callProxy(caller, returnedLeader -> markLeader(returnedLeader));
        }
    }

    private void markLeader(UserInfo returnedLeader) {
        GPSLocation leaderLocation = childInfo.getLastGpsLocation();

        if (leaderLocation != null && leaderLocation.getLat() != null && leaderLocation.getLat() != null) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(leaderLocation.getLat(), leaderLocation.getLng()))
                    .title(returnedLeader.getName())
                    .snippet(leaderLocation.getTimestamp().toString())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }
    }

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, ViewMonitoredUserMapActivity.class);

        return intent;
    }
}
