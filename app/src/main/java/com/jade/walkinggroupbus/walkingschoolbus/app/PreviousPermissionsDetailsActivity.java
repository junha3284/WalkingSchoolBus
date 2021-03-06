package com.jade.walkinggroupbus.walkingschoolbus.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.jade.walkinggroupbus.walkingschoolbus.R;
import com.jade.walkinggroupbus.walkingschoolbus.model.Authorizor;
import com.jade.walkinggroupbus.walkingschoolbus.model.MyRewards;
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
        // set theme
        MyRewards myRewards = MyRewards.MyRewards();
        setTheme(myRewards.getSelectedThemeID());
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_permissions_details);

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
        if (permissionID != null) {
            Call<Permission> caller = proxy.getPermissionByID(permissionID);
            ProxyBuilder.callProxy(this, caller, returnedPermission -> displayAuthorizorsList(returnedPermission));
        }
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
        Intent intent = new Intent(context, PreviousPermissionsDetailsActivity.class);
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
