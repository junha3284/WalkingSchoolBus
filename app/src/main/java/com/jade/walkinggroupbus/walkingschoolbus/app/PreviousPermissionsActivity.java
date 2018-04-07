package com.jade.walkinggroupbus.walkingschoolbus.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jade.walkinggroupbus.walkingschoolbus.R;
import com.jade.walkinggroupbus.walkingschoolbus.model.Permission;
import com.jade.walkinggroupbus.walkingschoolbus.model.SharedData;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;

import java.util.List;

import retrofit2.Call;

public class PreviousPermissionsActivity extends AppCompatActivity {

    private UserInfo userInfo = UserInfo.userInfo();

    private List<Permission> approvedPermissions;
    private List<Permission> deniedPermissions;

    private WGServerProxy proxy;
    private SharedData sharedData = SharedData.getSharedData();

    private static final String TAG = "ServerTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //TODO: integrate theme change

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_permissions);

        String token = sharedData.getToken();
        if(token != null)
            proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), sharedData.getToken());
        else {
            ProxyBuilder.setOnTokenReceiveCallback(token1 -> onReceiveToken(token1));
        }

        // set up ListViews
        showPreviousPermissions();

        // set on item click listeners
        setOnItemClickListeners();
    }

    private void showPreviousPermissions() {

        // retrieve list of approved permissions
        Call<List<Permission>> approvedCaller = proxy.getApprovedPermissionsByUserID(userInfo.getId());
        ProxyBuilder.callProxy(this, approvedCaller, returnedPermissions -> createApprovedListView(returnedPermissions));

        // retrieve list of denied permissions
        Call<List<Permission>> deniedCaller = proxy.getDeniedPermissionsByUserID(userInfo.getId());
        ProxyBuilder.callProxy(this, deniedCaller, returnedPermissions -> createDeniedListView(returnedPermissions));

    }

    private void createApprovedListView(List<Permission> returnedPermissions) {
        approvedPermissions = returnedPermissions;

        // get all permission messages
        String[] permissionMessages = new String[approvedPermissions.size()];
        int i = 0;
        for (Permission permission : approvedPermissions) {
            permissionMessages[i] = permission.getMessage();
            i++;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.list_template_previous_permissions,
                permissionMessages);
        ListView list = findViewById(R.id.list_approved_permissions);
        list.setAdapter(adapter);
    }

    private void createDeniedListView(List<Permission> returnedPermissions) {
        deniedPermissions = returnedPermissions;

        // get all permission messages
        String[] permissionMessages = new String[deniedPermissions.size()];
        int i = 0;
        for (Permission permission : deniedPermissions) {
            permissionMessages[i] = permission.getMessage();
            i++;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.list_template_previous_permissions,
                permissionMessages);
        ListView list = findViewById(R.id.list_denied_permissions);
        list.setAdapter(adapter);
    }

    private void setOnItemClickListeners() {
        ListView approvedList = findViewById(R.id.list_approved_permissions);
        ListView deniedList = findViewById(R.id.list_denied_permissions);

        approvedList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // get permission id
                Long permissionID = approvedPermissions.get(i).getId();

                Intent intent = PreviousPermissionsDetailsActivity.makeIntent(PreviousPermissionsActivity.this, permissionID);
                startActivity(intent);
            }
        });

        deniedList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // get permission id
                Long permissionID = deniedPermissions.get(i).getId();

                Intent intent = PreviousPermissionsDetailsActivity.makeIntent(PreviousPermissionsActivity.this, permissionID);
                startActivity(intent);
            }
        });
    }

    private void onReceiveToken(String token) {
        // Replace the current proxy with one that uses the token!
        Log.w(TAG, "   --> NOW HAVE TOKEN: " + token);
        proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), token);
        sharedData.setToken(token);
    }

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, PreviousPermissionsActivity.class);
        return intent;
    }
}
