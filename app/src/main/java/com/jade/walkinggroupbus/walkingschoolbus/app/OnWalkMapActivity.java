package com.jade.walkinggroupbus.walkingschoolbus.app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jade.walkinggroupbus.walkingschoolbus.R;
import com.jade.walkinggroupbus.walkingschoolbus.model.GPSLocation;
import com.jade.walkinggroupbus.walkingschoolbus.model.GroupsInfo;
import com.jade.walkinggroupbus.walkingschoolbus.model.SharedData;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;

import java.security.acl.Group;
import java.util.Date;
import java.util.List;

import retrofit2.Call;

public class OnWalkMapActivity extends FragmentActivity
        implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;

    private UserInfo userInfo = UserInfo.userInfo();
    private GroupsInfo groupsInfo = GroupsInfo.getInstance();

    private WGServerProxy proxy;
    private SharedData sharedData;
    private static final String TAG = "ServerTest";
    private static final String GROUP_ID = "group ID";
    private static final int REQUEST_CODE_LOCATIONPERMISSION = 13116;

    private Long onWalkGroupID;
    GPSLocation gpsLocation = new GPSLocation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_walk_map);

        sharedData = SharedData.getSharedData();
        String token = sharedData.getToken();
        // check if token is set properly
        if(token != null)
            proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), sharedData.getToken());
        else {
            ProxyBuilder.setOnTokenReceiveCallback(token1 -> onReceiveToken(token1));
        }

        getIntentData();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

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

    private void getIntentData() {
        Intent intent = getIntent();
        onWalkGroupID = intent.getLongExtra(GROUP_ID,0);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // location every 30 seconds
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(30000);
        mLocationRequest.setFastestInterval(30000);

        // check permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mMap.setMyLocationEnabled(true);
        }
        else {
            // Toast for permission
            Toast.makeText(this,"Allow location permissions to center location",Toast.LENGTH_LONG).show();
            String[] permissions = {"android.permission.ACCESS_COARSE_LOCATION"};
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_LOCATIONPERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_LOCATIONPERMISSION:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    mMap.setMyLocationEnabled(true);
                }
        }
    }

    LocationCallback mLocationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            Location location = locationResult.getLastLocation();

            // get location and time info
            Double[] newGPSCoordinates = {location.getLatitude(),location.getLongitude()};
            gpsLocation.setLocation(newGPSCoordinates);
            gpsLocation.setTimestamp(new Date());

            // send location info
            Call<GPSLocation> gpsLocationCaller = proxy.setNewGPSLocation(userInfo.getId(), gpsLocation);
            ProxyBuilder.callProxy(OnWalkMapActivity.this, gpsLocationCaller, returnedGPSLocation -> updateUser(returnedGPSLocation));

            // retrieve group info
            // update markers with locations
            Call<List<UserInfo>> groupUsersCaller = proxy.getMembersOfGroup(onWalkGroupID);
            ProxyBuilder.callProxy(OnWalkMapActivity.this, groupUsersCaller, returnedGroupMembers -> updateMarkers(returnedGroupMembers));

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));
        }
    };

    private void updateUser(GPSLocation returnedGPSLocation) {
        userInfo.setLastGpsLocation(returnedGPSLocation);
    }

    private void updateMarkers(List<UserInfo> returnedGroupMembers) {
        // clear previous markers
        mMap.clear();

        // set/reset route markers
        // and check if destination is reached
        Call<com.jade.walkinggroupbus.walkingschoolbus.model.Group> groupCaller = proxy.getGroupByID(onWalkGroupID);
        ProxyBuilder.callProxy(OnWalkMapActivity.this, groupCaller, returnedGroup -> setRouteMarkers(returnedGroup));

        Long groupLeader = groupsInfo.getLeaderByID(onWalkGroupID);

        for (UserInfo user : returnedGroupMembers) {
            // if user is not leader, get id
            // and mark yellow
            Long id = user.getId();
            String name = user.getName();
            if (!id.equals(groupLeader) && !id.equals(userInfo.getId())) {
                Call<GPSLocation> caller = proxy.getLastGPSLocation(id);
                ProxyBuilder.callProxy(OnWalkMapActivity.this, caller, returnedLocation -> markUser(name, returnedLocation));
            }
            else if (id.equals(groupLeader)) {
                // if member is leader, colour marker green
                Call<GPSLocation> leaderCaller = proxy.getLastGPSLocation(id);
                ProxyBuilder.callProxy(OnWalkMapActivity.this, leaderCaller, returnedLocation -> markLeader(name, returnedLocation));
            }
        }
    }

    private void setRouteMarkers(com.jade.walkinggroupbus.walkingschoolbus.model.Group returnedGroup) {
        Double[] lat = returnedGroup.getRouteLatArray();
        Double[] lng = returnedGroup.getRouteLngArray();

        // meeting place
        Marker meetingPlace = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat[0], lng[0]))
                .title("Meeting Place")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        // destination
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat[1], lng[1]))
                .title("Destination")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        // TODO:: if destination reached
        if (gpsLocation.getLat().equals(lat[1]) && gpsLocation.getLng().equals(lng[1])) {

        }
    }

    private void markUser(String name, GPSLocation returnedLocation) {
        Double[] location = returnedLocation.getLocation();
        if (location[0] != null && location[1] != null) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(location[0], location[1]))
                    .title(name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        }
    }

    private void markLeader(String name, GPSLocation returnedLocation) {
        Double[] location = returnedLocation.getLocation();
        if (location[0] != null && location[1] != null) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(location[0], location[1]))
                    .title(name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // false to move camera
        return false;
    }

    public static Intent makeIntent(Context context, Long groupID) {
        Intent intent = new Intent(context, OnWalkMapActivity.class);
        intent.putExtra(GROUP_ID, groupID.longValue());
        return intent;
    }
}
