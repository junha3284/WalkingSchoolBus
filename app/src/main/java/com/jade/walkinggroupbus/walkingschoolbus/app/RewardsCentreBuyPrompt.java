package com.jade.walkinggroupbus.walkingschoolbus.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.jade.walkinggroupbus.walkingschoolbus.R;
import com.jade.walkinggroupbus.walkingschoolbus.model.Message;
import com.jade.walkinggroupbus.walkingschoolbus.model.SharedData;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;

/**
 * Created by Richard Wong on 2018-04-01.
 */

public class RewardsCentreBuyPrompt extends AppCompatDialogFragment{
    private UserInfo userInfo;
    private SharedData sharedData;
    private WGServerProxy proxy;
    private static final String TAG = "ServerTest";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        userInfo = UserInfo.userInfo();
        sharedData = SharedData.getSharedData();
        String token = sharedData.getToken();

        // check if token is set properly
        if(token != null)
            proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), sharedData.getToken());
        else {
            ProxyBuilder.setOnTokenReceiveCallback(token1 -> onReceiveToken(token1));
        }

        // Create the view to show
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.activity_rewards_centre_buy_prompt, null);

        // Purchase Button
        Button purchaseBtn = (Button) v.findViewById(R.id.RCBP_button_purchase);
        purchaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO subtract points from total, and add theme to collection. Perform a check to see if user has already bought this theme.
                dismiss();
            }
        });

        // Cancel button
        Button cancelBtn = (Button) v.findViewById(R.id.RCBP_button_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        // Create a button listener
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        };

        // Build the alert dialog
        return new AlertDialog.Builder(getActivity())
                .setTitle("")
                .setView(v)
                .create();
    }

    private void onReceiveToken(String token) {
        // Replace the current proxy with one that uses the token!
        Log.w(TAG, "   --> NOW HAVE TOKEN: " + token);
        proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), token);
        sharedData.setToken(token);
    }
}
