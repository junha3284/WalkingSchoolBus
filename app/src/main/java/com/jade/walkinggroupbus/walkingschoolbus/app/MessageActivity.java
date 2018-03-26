package com.jade.walkinggroupbus.walkingschoolbus.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.jade.walkinggroupbus.walkingschoolbus.R;
import com.jade.walkinggroupbus.walkingschoolbus.model.Message;
import com.jade.walkinggroupbus.walkingschoolbus.model.SharedData;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class MessageActivity extends AppCompatActivity {
    private static final String TAG = "MessageActivity's proxy";

    private List<Message> unreadMessages = new ArrayList<>();
    private List<Message> readMessages = new ArrayList<>();

    private SharedData sharedData;
    private UserInfo userInfo;

    private WGServerProxy proxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        userInfo = UserInfo.userInfo();
        sharedData = SharedData.getSharedData();

        String token = sharedData.getToken();

        // check if token is set properly
        if(token != null)
            proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), sharedData.getToken(), "1");
        else {
            ProxyBuilder.setOnTokenReceiveCallback(token1 -> onReceiveToken(token1));
        }

        getMessagesFromServer();
        setBtn();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getMessagesFromServer();
    }

    private void getMessagesFromServer() {
        Call<UserInfo> caller = proxy.getUserById(userInfo.getId());
        ProxyBuilder.callProxy(MessageActivity.this, caller, returned -> response(returned));
    }

    private void response(UserInfo returnedUserInfo){
        userInfo.setUserInfo(returnedUserInfo);
        
        // update unreadMessages and readMessages
        unreadMessages.clear();
        readMessages.clear();
        
        for(Message msg : userInfo.getUnreadMessages()){
            unreadMessages.add(msg);
        }
        for(Message msg : userInfo.getReadMessages()){
            readMessages.add(msg);
        }
        
        updateListViews();
    }

    private void updateListViews() {
        ArrayAdapter<String> adapterForUnreadMessages = new ArrayAdapter<String>(
                this,                                   // context
                R.layout.list_template_messages,                // layout to use (create)
                getMessagesDescription(unreadMessages));        // items to be displayed
        // configure list view
        ListView listForUnReadMessages = (ListView) findViewById(R.id.listView_unReadMessage);
        listForUnReadMessages.setAdapter(adapterForUnreadMessages);

        ArrayAdapter<String> adapterForReadMessages = new ArrayAdapter<String>(
                this,                                   // context
                R.layout.list_template_messages,                // layout to use
                getMessagesDescription(readMessages));          // items to be displayed
        // configure list view
        ListView listForReadMessages = (ListView) findViewById(R.id.listView_readMessage);
        listForReadMessages.setAdapter(adapterForReadMessages);


    }

    private String[] getMessagesDescription(List<Message> messageList){
        int size = messageList.size();
        String[] description = new String[size];
        for(int i=0; i < size; i++)
            description[i] = messageList.get(i).toString();
        return description;
    }

    private void setBtn(){
        Button newMessageButton = (Button) findViewById(R.id.button_sendMessage);
        newMessageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = ComposeMessageActivity.makeIntent(MessageActivity.this);
                startActivity(intent);
            }
        });
    }

    private void onReceiveToken(String token) {
        // Replace the current proxy with one that uses the token!
        Log.w(TAG, "   --> NOW HAVE TOKEN: " + token);
        proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), token, "1");
        sharedData.setToken(token);
    }

    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, MessageActivity.class);
        return intent;
    }
}
