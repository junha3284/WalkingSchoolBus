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
import android.widget.Toast;

import com.jade.walkinggroupbus.walkingschoolbus.R;
import com.jade.walkinggroupbus.walkingschoolbus.model.Message;
import com.jade.walkinggroupbus.walkingschoolbus.model.MyRewards;
import com.jade.walkinggroupbus.walkingschoolbus.model.SharedData;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;

import retrofit2.Call;

public class RewardsCentreBuyPrompt extends AppCompatDialogFragment{
    private UserInfo userInfo;
    private SharedData sharedData;
    private WGServerProxy proxy;
    private MyRewards myRewards;
    private static final String TAG = "ServerTest";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        userInfo = UserInfo.userInfo();
        sharedData = SharedData.getSharedData();
        myRewards = MyRewards.MyRewards();
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
                // Checks to see if user has already obtained the theme
                if (myRewards.checkObtainedRewardsByName(myRewards.getPreviewTheme())){
                    Toast.makeText(getActivity(), "Theme already purchased. Returning to Rewards Centre.", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
                // Checks to see if the user has enough points
                else if(userInfo.getCurrentPoints() < 100){
                    Toast.makeText(getActivity(), "You do not have enough points. Returning to Rewards Centre.", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
                // Awards the theme and subtracts the points from current points.
                else{
                    myRewards.unlockReward(myRewards.getSelectedIndex());
                    int currentPoints = userInfo.getCurrentPoints() - 100;
                    userInfo.setCurrentPoints(currentPoints);

                    // update server once reward is bought
                    String myRewardsJsonString = myRewards.convertToJsonString();
                    userInfo.setCustomJson(myRewardsJsonString);

                    Call<UserInfo> editUser = proxy.editUser(userInfo.getId(), userInfo);
                    ProxyBuilder.callProxy(editUser, returnedUser -> response(returnedUser));

                    Toast.makeText(getActivity(), "Purchase Successful. Returning to Rewards Centre.", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
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

    private void response(UserInfo returnedUser) {
        Log.w(TAG, "    User: " + returnedUser);
    }

    private void onReceiveToken(String token) {
        // Replace the current proxy with one that uses the token!
        Log.w(TAG, "   --> NOW HAVE TOKEN: " + token);
        proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), token);
        sharedData.setToken(token);
    }


}
