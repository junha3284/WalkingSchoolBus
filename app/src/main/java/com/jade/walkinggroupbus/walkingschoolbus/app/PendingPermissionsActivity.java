package com.jade.walkinggroupbus.walkingschoolbus.app;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jade.walkinggroupbus.walkingschoolbus.R;
import com.jade.walkinggroupbus.walkingschoolbus.model.MyRewards;
import com.jade.walkinggroupbus.walkingschoolbus.model.Permission;
import com.jade.walkinggroupbus.walkingschoolbus.model.SharedData;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class PendingPermissionsActivity extends AppCompatActivity {
    private static final String TAG = "PPA Proxy";

    private SharedData sharedData;
    private UserInfo userInfo;

    private WGServerProxy proxy;

    private List<Permission> pendingPermissions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set theme
        MyRewards myRewards = MyRewards.MyRewards();
        setTheme(myRewards.getSelectedThemeID());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_permissions);

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

        userInfo = UserInfo.userInfo();
        sharedData = SharedData.getSharedData();

        String token = sharedData.getToken();

        // check if token is set properly
        if(token != null)
            proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), sharedData.getToken(), "2");
        else {
            ProxyBuilder.setOnTokenReceiveCallback(token1 -> onReceiveToken(token1));
        }

        // get new UserInfo with depth 1 and populate the listView
        getDetailedPermissionRequests();
        // set OnItemClickListener for the listView
        setClickListener();
        // set refresh button
        setBtn();
    }

    // get new UserInfo with depth 1 and populate the listView
    public void getDetailedPermissionRequests() {
        Call<UserInfo> caller = proxy.getUserById( userInfo.getId());
        ProxyBuilder.callProxy(PendingPermissionsActivity.this, caller, returned -> response(returned));
    }

    private void response( UserInfo returned) {
        userInfo.setUserInfo( returned);
        pendingPermissions = userInfo.getPendingPermissionRequests();
        populatePendingPermissionList();
    }

    private void populatePendingPermissionList() {
        ArrayAdapter<Permission> adapter = new PendingPermissionsListAdapter();
        ListView pendingPermissionsList = (ListView) findViewById (R.id.listView_pendingPermissions);
        pendingPermissionsList.setAdapter(adapter);
    }

    // set OnItemClickListener for the listView
    private void setClickListener() {
        ListView pendingPermissionList = (ListView) findViewById( R.id.listView_pendingPermissions);
        pendingPermissionList.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = PendingPermissionDetailActivity.makeIntent(
                        PendingPermissionsActivity.this,
                        pendingPermissions.get( position ));
                startActivity(intent);
            }
        });
    }

    // set refresh button
    private void setBtn() {
        Button refreshBtn = (Button) findViewById( R.id.button_refresh);
        refreshBtn.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick( View view) {
                getDetailedPermissionRequests();
            }
        });
    }

    // set newly issued token to proxy and save it on sharedData singleton object
    private void onReceiveToken(String token) {
        // Replace the current proxy with one that uses the token!
        Log.w(TAG, "   --> NOW HAVE TOKEN: " + token);
        proxy = ProxyBuilder.getProxy( getString(R.string.API_KEY), token, "2");
        sharedData.setToken(token);
    }

    static public Intent makeIntent ( Context context){
        Intent intent = new Intent( context, PendingPermissionsActivity.class);
        return intent;
    }


    /*
    --------------------------------
    PRIVATE
    --------------------------------
    */
    private class PendingPermissionsListAdapter extends ArrayAdapter<Permission> {
        public PendingPermissionsListAdapter(){
            super(PendingPermissionsActivity.this, R.layout.list_template_permission, pendingPermissions);
        }

        @NonNull
        @Override
        public View getView( int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            // Make sure we have a view to work with
            View itemView = convertView;
            if ( itemView == null) {
                itemView = getLayoutInflater()
                        .inflate(R.layout.list_template_permission, parent, false);
            }

            // Find the message to work with.
            Permission currentPermission = pendingPermissions.get(position);

            // Fill the view
            // requesting User:
            TextView requestingUserText = (TextView) itemView.findViewById(R.id.item_requestingUser);
            requestingUserText.setText(currentPermission.getRequestingUser().getName());

            // action content:
            TextView actionContentText = (TextView) itemView.findViewById(R.id.item_actionContent);
            actionContentText.setText(currentPermission.getAction());

            return itemView;
        }
    }
}
