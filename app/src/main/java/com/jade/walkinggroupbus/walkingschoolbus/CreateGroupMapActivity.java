package com.jade.walkinggroupbus.walkingschoolbus;

import android.*;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CreateGroupMapActivity extends FragmentActivity
        implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMyLocationButtonClickListener {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;

    private Marker[] markers = {null, null};
    private int numMarked = 0;

    private static final int REQUEST_CODE_LOCATIONPERMISSION = 13116;
    private static final int MAX_MARKERS = 2;

    private static final String MEETING_PLACE_LAT = "meeting place latitude";
    private static final String MEETING_PLACE_LNG = "meeting place longitude";
    private static final String DESTINATION_LAT = "destination latitude";
    private static final String DESTINATION_LNG = "destination latitude";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_map);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getIntentData();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(MEETING_PLACE_LAT) && intent.hasExtra(MEETING_PLACE_LNG) &&
            intent.hasExtra(DESTINATION_LAT) && intent.hasExtra(DESTINATION_LNG)) {
            LatLng meetingPlaceLatLng = new LatLng(intent.getFloatExtra(MEETING_PLACE_LAT, 0),
                                                   intent.getFloatExtra(MEETING_PLACE_LNG, 0));
            LatLng destinationLatLng = new LatLng(intent.getFloatExtra(DESTINATION_LAT, 0),
                                                  intent.getFloatExtra(DESTINATION_LNG, 0));

            // set markers
            Marker meetingPlaceMarker = mMap.addMarker(new MarkerOptions()
                    .position(meetingPlaceLatLng)
                    .title("Delete Meeting Place")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            markers[0] = meetingPlaceMarker;
            numMarked++;

            Marker destinationMarker = mMap.addMarker(new MarkerOptions()
                    .position(destinationLatLng)
                    .title("Delete Destination")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            markers[1] = destinationMarker;
            numMarked++;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // set meeting place / destination markers
        mMap.setOnMapClickListener(latLng -> addMarker(latLng));

        // set marker info window click listener
        // delete marker
        mMap.setOnInfoWindowClickListener(this);

        // get location
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(60000);

        // check permissions
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
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

    private void addMarker(LatLng latLng) {
        if (numMarked == 0 || (numMarked == 1 && markers[0] == null)) {
            // put down meeting place marker
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Delete Meeting Place")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            markers[0] = marker;
        }
        else {
            // put down destination marker
            if (markers[1] != null) {
                markers[1].remove();
            }
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Delete Destination")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            markers[1] = marker;
        }
        if (numMarked < MAX_MARKERS) {
            numMarked++;
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (marker.getTitle().equals("Delete Meeting Place")) {
            markers[0] = null;
            numMarked--;
        }
        else {
            markers[1] = null;
            numMarked--;
        }
        marker.remove();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_LOCATIONPERMISSION:
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
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
            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));
        }
    };

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // false to move camera
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // verify, and get marker data
        // set activity result and intent data
        if (markers[0] != null && markers[1] != null) {
            Intent intent = new Intent();
            LatLng meetingPlace = markers[0].getPosition();
            LatLng destination = markers[1].getPosition();

            intent.putExtra(MEETING_PLACE_LAT, meetingPlace.latitude);
            intent.putExtra(MEETING_PLACE_LNG, meetingPlace.longitude);
            intent.putExtra(DESTINATION_LAT, destination.latitude);
            intent.putExtra(DESTINATION_LNG, destination.longitude);

            setResult(Activity.RESULT_OK, intent);
        }
        else {
            setResult(Activity.RESULT_CANCELED);
        }
        finish();
    }
}
