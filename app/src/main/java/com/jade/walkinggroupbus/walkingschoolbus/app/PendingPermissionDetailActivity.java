package com.jade.walkinggroupbus.walkingschoolbus.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jade.walkinggroupbus.walkingschoolbus.R;
import com.jade.walkinggroupbus.walkingschoolbus.model.MyRewards;
import com.jade.walkinggroupbus.walkingschoolbus.model.Permission;
import com.jade.walkinggroupbus.walkingschoolbus.model.SharedData;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;

import retrofit2.Call;

public class PendingPermissionDetailActivity extends AppCompatActivity {

    private static final String TAG = "PPDA";
    // Codes for Extra in intent
    public static final String EXTRA_CODE_ID = "com.jade.walkinggroupbus.walkingschoolbus.app-id";
    public static final String EXTRA_CODE_MESSAGE = "com.jade.walkinggroupbus.walkingschoolbus.app-message";
    public static final String EXTRA_CODE_REQUESTINGUSERNAME = "com.jade.walkinggroupbus.walkingschoolbus.app-requestingUserName";
    public static final String EXTRA_CODE_ACTION = "com.jade.walkinggroupbus.walkingschoolbus.app-action";

    private SharedData sharedData;
    private UserInfo userInfo;

    private WGServerProxy proxy;

    private Long permissionId;
    private String message;
    private String requestingUserName;
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set theme
        MyRewards myRewards = MyRewards.MyRewards();
        setTheme(myRewards.getSelectedThemeID());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_permission_detail);

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
            proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), sharedData.getToken());
        else {
            ProxyBuilder.setOnTokenReceiveCallback(token1 -> onReceiveToken(token1));
        }

        // extract and store data into private variables of Activity
        extractExtraDataFromIntent();

        //populate TextViews
        updateUI();

        // set response buttons (Approve button and deny button)
        setBtns();
    }

    // extract and store data into private variables of Activity
    private void extractExtraDataFromIntent(){
        Intent intent = getIntent();

        permissionId = intent.getLongExtra(EXTRA_CODE_ID, -1);
        message = intent.getStringExtra(EXTRA_CODE_MESSAGE);
        requestingUserName = intent.getStringExtra(EXTRA_CODE_REQUESTINGUSERNAME);
        action = intent.getStringExtra(EXTRA_CODE_ACTION);
    }

    //populate TextViews
    private void updateUI(){
        TextView messageTextView = (TextView) findViewById(R.id.text_message);
        TextView requestingUserTextView = (TextView) findViewById(R.id.text_requestingUserName);
        TextView actionTextView = (TextView) findViewById(R.id.text_action);

        messageTextView.setText(message);
        requestingUserTextView.setText(requestingUserName);
        actionTextView.setText(action);
    }

    // set response buttons (Approve button and deny button)
    private void setBtns(){
        Button approveBtn = (Button) findViewById(R.id.button_approve);
        Button denyBtn = (Button) findViewById(R.id.button_deny);

        approveBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Call<Permission> caller = proxy.respondToPermissionRequest(permissionId, "APPROVED");
                ProxyBuilder.callProxy(caller, returned->response(returned));
                finish();
            }
        });
        denyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Permission> caller = proxy.respondToPermissionRequest(permissionId, "DENIED");
                ProxyBuilder.callProxy(caller, returned->response(returned));
                finish();
            }
        });
    }

    private void response(Permission permission){
        Log.i(TAG, "permission request id: "
                + permission.getId()
                + ", status: "
                + permission.getStatus());

        Toast.makeText(PendingPermissionDetailActivity.this,
                "permission response accepted",
                Toast.LENGTH_SHORT);
    }

    // set newly issued token to proxy and save it on sharedData singleton object
    private void onReceiveToken(String token) {
        // Replace the current proxy with one that uses the token!
        Log.w(TAG, "   --> NOW HAVE TOKEN: " + token);
        proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), token);
        sharedData.setToken(token);
    }

    static public Intent makeIntent(Context context, Permission permission){
        Intent intent = new Intent ( context, PendingPermissionDetailActivity.class);

        // put permission's data into intent
        intent.putExtra(EXTRA_CODE_ID,
                permission.getId());
        intent.putExtra(EXTRA_CODE_MESSAGE,
                permission.getMessage());
        intent.putExtra(EXTRA_CODE_REQUESTINGUSERNAME,
                permission.getRequestingUser().getName());
        intent.putExtra(EXTRA_CODE_ACTION,
                permission.getAction());

        return intent;
    }
}
