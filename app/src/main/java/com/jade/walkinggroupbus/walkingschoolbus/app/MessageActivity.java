package com.jade.walkinggroupbus.walkingschoolbus.app;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jade.walkinggroupbus.walkingschoolbus.R;
import com.jade.walkinggroupbus.walkingschoolbus.model.Message;
import com.jade.walkinggroupbus.walkingschoolbus.model.SharedData;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;

public class MessageActivity extends AppCompatActivity {
    private static final String TAG = "MessageActivity's proxy";
    private static final String TAG_TIMER = "TimerTask";

    private List<Message> unreadMessages = new ArrayList<>();
    private List<Message> readMessages = new ArrayList<>();

    private SharedData sharedData;
    private UserInfo userInfo;

    private WGServerProxy proxy;

    // For updating Messages for every 1 min
    Timer timer;
    TimerTask timerTask;

    final Handler handler = new Handler();

    // to express date properly
    DateFormat dateFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        userInfo = UserInfo.userInfo();
        sharedData = SharedData.getSharedData();

        String token = sharedData.getToken();

        dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());

        // check if token is set properly
        if(token != null)
            proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), sharedData.getToken(), "2");
        else {
            ProxyBuilder.setOnTokenReceiveCallback(token1 -> onReceiveToken(token1));
        }

        setBtn();

        // get messages from the server and update ListViews
        // and update messages in ListViews
        getMessagesFromServer();

        // set OnItemClickListener for ListViews
        //      request server to mark clicked message as read
        setOnClickListeners();
    }


    @Override
    protected void onResume() {
        super.onResume();

        getMessagesFromServer();

        // start timer which updates Messages every 1 min
        startTimer();
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

    // get messages from the server and update ListViews
    // and update messages in ListViews
    private void getMessagesFromServer() {
        Call<UserInfo> caller = proxy.getUserById(userInfo.getId());
        ProxyBuilder.callProxy(MessageActivity.this, caller, returned -> response(returned));
    }

    // set OnItemClickListener for ListViews
    //      request server to mark clicked message as read
    private void setOnClickListeners() {
        ListView listForUnreadMessages = (ListView) findViewById(R.id.listView_unReadMessage);
        listForUnreadMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Call<UserInfo> caller = proxy.readMessage(unreadMessages.get(position).getId(),
                        userInfo.getId(), true);
                ProxyBuilder.callProxy(MessageActivity.this,caller, returned -> response(returned));
            }
        });
    }

    private void response(UserInfo returnedUserInfo){
        userInfo.setUserInfo(returnedUserInfo);

        // reset Messages and populate unreadMessages and readMessages with new data from the server
        unreadMessages.clear();
        readMessages.clear();

        for(Message msg : userInfo.getUnreadMessages()){
            unreadMessages.add(msg);
        }
        for(Message msg : userInfo.getReadMessages()){
            readMessages.add(msg);
        }

        // reflect new data from the server to UI (ListViews)
        updateListViews();
    }

    private void updateListViews() {
        ArrayAdapter<Message> adapterForUnreadMessages = new UnreadMessageListAdapter();
        ListView listForUnReadMessages = (ListView) findViewById(R.id.listView_unReadMessage);
        listForUnReadMessages.setAdapter(adapterForUnreadMessages);


        ArrayAdapter<Message> adapterForReadMessages = new ReadMessageListAdapter();
        ListView listForReadMessages = (ListView) findViewById(R.id.listView_readMessage);
        listForReadMessages.setAdapter(adapterForReadMessages);
    }

    // start timer which updates Messages every 1 min
    private void startTimer() {
        timer = new Timer();

        timerTask = new TimerTask(){
            @Override
            public void run() {
                Log.i(TAG_TIMER,"message got updated with server call");
                getMessagesFromServer();
            }
        };

        timer.schedule(timerTask, 60000, 60000 );
    }



    // set newly issued token to proxy and save it on sharedData singleton object
    private void onReceiveToken(String token) {
        // Replace the current proxy with one that uses the token!
        Log.w(TAG, "   --> NOW HAVE TOKEN: " + token);
        proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), token, "2");
        sharedData.setToken(token);
    }

    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, MessageActivity.class);
        return intent;
    }



    /*
    --------------------------------
    PRIVATE
    --------------------------------
    */
    private class UnreadMessageListAdapter extends ArrayAdapter<Message> {
        public UnreadMessageListAdapter(){
            super(MessageActivity.this, R.layout.list_template_messages, unreadMessages);
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
            Message currentMsg = unreadMessages.get(position);

            // Fill the view
            // sender:
            TextView senderText = (TextView) itemView.findViewById(R.id.item_sender);
            senderText.setText(currentMsg.getFromUser().getName());

            // content:
            TextView contentText = (TextView) itemView.findViewById(R.id.item_content);
            contentText.setText(currentMsg.getText());

            // date:
            Date date = currentMsg.getTimestamp();
            TextView dateText = (TextView) itemView.findViewById(R.id.item_date);
            dateText.setText(DateFormat
                    .getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(date));


            return itemView;
        }
    }

    private class ReadMessageListAdapter extends ArrayAdapter<Message> {
        public ReadMessageListAdapter(){
            super(MessageActivity.this, R.layout.list_template_messages, readMessages);
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
            Message currentMsg = readMessages.get(position);

            // Fill the view
            // sender:
            TextView senderText = (TextView) itemView.findViewById(R.id.item_sender);
            senderText.setText(currentMsg.getFromUser().getName());

            // content:
            TextView contentText = (TextView) itemView.findViewById(R.id.item_content);
            contentText.setText(currentMsg.getText());

            // date:
            Date date = currentMsg.getTimestamp();
            TextView dateText = (TextView) itemView.findViewById(R.id.item_date);
            dateText.setText(DateFormat
                    .getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(date));


            return itemView;
        }
    }
}
