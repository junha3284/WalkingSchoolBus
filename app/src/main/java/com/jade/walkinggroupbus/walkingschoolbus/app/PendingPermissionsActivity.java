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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jade.walkinggroupbus.walkingschoolbus.R;
import com.jade.walkinggroupbus.walkingschoolbus.model.Permission;
import com.jade.walkinggroupbus.walkingschoolbus.model.SharedData;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;

import java.util.List;

import retrofit2.Call;

public class PendingPermissionsActivity extends AppCompatActivity {
    private static final String TAG = "PPA Proxy";

    private SharedData sharedData;
    private UserInfo userInfo;

    private WGServerProxy proxy;

    private List<Permission> pendingPermssions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_permissions);

        userInfo = UserInfo.userInfo();
        sharedData = SharedData.getSharedData();

        String token = sharedData.getToken();

        // check if token is set properly
        if(token != null)
            proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), sharedData.getToken(), "2");
        else {
            ProxyBuilder.setOnTokenReceiveCallback(token1 -> onReceiveToken(token1));
        }

        getDetailedPermissionRequests();
    }

    public void getDetailedPermissionRequests() {
        Call<UserInfo> caller = proxy.getUserById(userInfo.getId());
        ProxyBuilder.callProxy(PendingPermissionsActivity.this, caller, returned -> response(returned));
    }

    private void response(UserInfo returned) {
        userInfo.setUserInfo(returned);
        pendingPermssions = userInfo.getPendingPermissionRequests();
        populatePendingPermissionList();
    }

    private void populatePendingPermissionList() {
        ArrayAdapter<Permission> adapter = new PendingPermissionsListAdapter();
        ListView pendingPermissionsList = (ListView) findViewById (R.id.listView_pendingPermissions);
        pendingPermissionsList.setAdapter(adapter);
    }

    // set newly issued token to proxy and save it on sharedData singleton object
    private void onReceiveToken(String token) {
        // Replace the current proxy with one that uses the token!
        Log.w(TAG, "   --> NOW HAVE TOKEN: " + token);
        proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), token, "2");
        sharedData.setToken(token);
    }

    static public Intent makeIntent (Context context){
        Intent intent = new Intent(context, PendingPermissionsActivity.class);
        return intent;
    }


    /*
    --------------------------------
    PRIVATE
    --------------------------------
    */
    private class PendingPermissionsListAdapter extends ArrayAdapter<Permission> {
        public PendingPermissionsListAdapter(){
            super(PendingPermissionsActivity.this, R.layout.list_template_permission, pendingPermssions);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            // Make sure we have a view to work with
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater()
                        .inflate(R.layout.list_template_messages, parent, false);
            }

            // Find the message to work with.
            Permission currentPermission = pendingPermssions.get(position);

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
