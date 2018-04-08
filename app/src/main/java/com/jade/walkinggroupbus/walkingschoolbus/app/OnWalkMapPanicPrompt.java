package com.jade.walkinggroupbus.walkingschoolbus.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jade.walkinggroupbus.walkingschoolbus.R;
import com.jade.walkinggroupbus.walkingschoolbus.model.Message;
import com.jade.walkinggroupbus.walkingschoolbus.model.SharedData;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;

import java.util.Collections;

import retrofit2.Call;

/**
 * allows user create a panic message while on walk that sends to parents of the user
 */
public class OnWalkMapPanicPrompt extends AppCompatDialogFragment {
    public String panicMessage;
    private WGServerProxy proxy;
    private SharedData sharedData;
    private static final String TAG = "ServerTest";

    private UserInfo userInfo;

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
                .inflate(R.layout.activity_on_walk_map_panic_prompt, null);

        Button sendBtn = (Button) v.findViewById(R.id.button_send);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edit_panicMessage = (EditText) v.findViewById(R.id.OWA_edit_optional_message);
                panicMessage = "Emergency! " + edit_panicMessage.getText().toString();

                Message message = new Message(panicMessage, true);

                Long id = userInfo.getId();

                // Call to Server. Emergency Message
                Call<Message> caller = proxy.newMessageToParents(id, message);
                ProxyBuilder.callProxy(caller, returnedNothing -> response(returnedNothing));

                getActivity().finish();
            }
        });

        Button cancelBtn = (Button) v.findViewById(R.id.OWA_btn_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        // Create a button listener
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        };

        TextView tv = (TextView)v.findViewById(R.id.text_panic_disclaimer);
        tv.setTextColor(Color.WHITE);
        TextView tvMsg = (TextView) v.findViewById(R.id.OWA_text_optional_message);
        tvMsg.setTextColor(Color.WHITE);

        EditText et = (EditText) v.findViewById(R.id.OWA_edit_optional_message);
        et.setTextColor(Color.WHITE);

        v.setBackgroundColor(Color.RED);



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

    private void response(Message message){

    }
}
