package com.jade.walkinggroupbus.walkingschoolbus.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jade.walkinggroupbus.walkingschoolbus.R;
import com.jade.walkinggroupbus.walkingschoolbus.model.Authorizor;
import com.jade.walkinggroupbus.walkingschoolbus.model.Permission;
import com.jade.walkinggroupbus.walkingschoolbus.model.SharedData;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;

import java.util.List;

import retrofit2.Call;

public class PreviousPermissionsDetailsActivity extends AppCompatActivity {

    private static String PERMISSION_ID = "permission ID";
    private Long permissionID;

    private WGServerProxy proxy;
    private SharedData sharedData = SharedData.getSharedData();

    private static final String TAG = "ServerTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        //TODO: integrate theme change
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_permissions_details);

        String token = sharedData.getToken();
        if(token != null)
            proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), sharedData.getToken(), "1");
        else {
            ProxyBuilder.setOnTokenReceiveCallback(token1 -> onReceiveToken(token1));
        }
        
        getIntentData();
        
        showPermissionsAuthorizors();
    }

    private void getIntentData() {
        // retrieve permission ID
        Intent intent = getIntent();
        permissionID = intent.getLongExtra(PERMISSION_ID, -1);
        if (permissionID == -1) {
            permissionID = null;
        }
    }

    private void showPermissionsAuthorizors() {
        // get permission info from server
        Call<Permission> caller = proxy.getPermissionByID(permissionID);
        ProxyBuilder.callProxy(this, caller, returnedPermission -> displayAuthorizorsList(returnedPermission));
    }

    private void displayAuthorizorsList(Permission returnedPermission) {
        // get all authorizors
        List<Authorizor> authorizors = returnedPermission.getAuthorizors();
        // get status detail for each authorizor
        String[] statusDetail = new String[authorizors.size()];
        int i = 0;
        for (Authorizor authorizor : authorizors) {
            statusDetail[i] = authorizor.toStringForList();
            i++;
        }

        // display status details
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.list_template_previous_permissions,
                statusDetail);
        ListView list = findViewById(R.id.list_authorizors);
        list.setAdapter(adapter);

    }

    public static Intent makeIntent(Context context, Long permissionID) {
        // create intent, save id
        Intent intent = new Intent();
        intent.putExtra(PERMISSION_ID, permissionID);
        
        return intent;
    }

    private void onReceiveToken(String token) {
        // Replace the current proxy with one that uses the token!
        Log.w(TAG, "   --> NOW HAVE TOKEN: " + token);
        proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), token);
        sharedData.setToken(token);
    }
}
