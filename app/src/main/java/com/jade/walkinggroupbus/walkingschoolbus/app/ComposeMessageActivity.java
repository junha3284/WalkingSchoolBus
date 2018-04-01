package com.jade.walkinggroupbus.walkingschoolbus.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.jade.walkinggroupbus.walkingschoolbus.R;
import com.jade.walkinggroupbus.walkingschoolbus.model.Group;
import com.jade.walkinggroupbus.walkingschoolbus.model.Message;
import com.jade.walkinggroupbus.walkingschoolbus.model.SharedData;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;

import java.util.List;

import retrofit2.Call;

/**
 * activity where current user can create messages to send to
 * their groups and their parents
 * supports panic message functionality
 */
public class ComposeMessageActivity extends AppCompatActivity {
    private static final String TAG = "Proxy";

    private UserInfo userInfo;
    private SharedData sharedData;
    private WGServerProxy proxy;

    private Long idForSending;

    private List<Group> leadingGroups;
    private boolean sendingToParent = true;

    private boolean emergency = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_message);

        userInfo = UserInfo.userInfo();
        sharedData = SharedData.getSharedData();

        String token = sharedData.getToken();

        // check if token is set properly
        if(token != null)
            proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), sharedData.getToken(), "1");
        else {
            ProxyBuilder.setOnTokenReceiveCallback(token1 -> onReceiveToken(token1));
        }

        // After updateUserInfo, update UIs (Spinner and Btn) recording to it
        updateUserInfo();
    }

    private void updateUserInfo() {
        Call<UserInfo> caller = proxy.getUserById(userInfo.getId());
        ProxyBuilder.callProxy(ComposeMessageActivity.this, caller, returned->respond(returned));
    }

    private void respond(UserInfo returnedUser){
        userInfo.setUserInfo(returnedUser);

        leadingGroups = userInfo.getLeadsGroups();
        // set Spinner in UI such that the user can choose who a message is sent to
        setSpinner();
        setBtn();
    }

    // set Spinner in UI such that the user can choose who a message is sent to
    //      first item is parent
    //      from second, the groups which the user is leading
    private void setSpinner() {
        //create adapter for spinner
        int size = leadingGroups.size();
        String[] items = new String[size + 1];

        items[0] = "parent";
        for(int i=0; i < size; i++){
            items[i+1] = leadingGroups.get(i).getGroupDescription();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.list_template_spinner_item,
                items);

        // set Adapter for spinner
        Spinner recipientSpinner = (Spinner) findViewById(R.id.spinner_recipient);
        recipientSpinner.setAdapter(adapter);

        // set OnItemListener
        recipientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if( position == 0) {
                    idForSending = userInfo.getId();
                    sendingToParent = true;
                    return;
                }
                idForSending = leadingGroups.get(position-1).getId();
                sendingToParent = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void setBtn() {
        Button sendButton = (Button) findViewById(R.id.button_send);
        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                CheckBox emergencyCheckBox = (CheckBox) findViewById(R.id.checkBox_emergency);
                emergency = emergencyCheckBox.isChecked();
                if(sendingToParent)
                    sendMessageToParent();
                else
                    sendMessageToClickedGroup();
                finish();
            }
        });
    }

    private void sendMessageToParent() {
        EditText msgEdit = (EditText) findViewById(R.id.edit_message);
        String message = msgEdit.getText().toString();
        if(emergency)
            message = "Emergency! " + message;

        if(message.length() != 0) {
            Message newMessage = new Message(message, emergency);
            Call<Message> caller = proxy.newMessageToParents(idForSending, newMessage);
            ProxyBuilder.callProxy(this, caller, returnedMsg -> response(returnedMsg));
            return;
        }
        Toast.makeText(this,
                "Content is empty!\n please input content for Message",
                Toast.LENGTH_SHORT)
            .show();
    }

    private void sendMessageToClickedGroup() {
        EditText msgEdit = (EditText) findViewById(R.id.edit_message);
        String message = msgEdit.getText().toString();
        if(emergency)
            message = "Emergency! " + message;

        if (message.length() != 0) {
            Message newMessage = new Message(message, emergency);
            Call<Message> caller = proxy.newMessageToGroup(idForSending, newMessage);
            ProxyBuilder.callProxy(this, caller, returnedMsg -> response(returnedMsg));
            return;
        }
        Toast.makeText(this,
                "Content is empty!\n please input content for Message",
                Toast.LENGTH_SHORT)
                .show();
    }

    private void response(Message returnedMsg) {
        Toast.makeText(this,
                "Message Sent successfully!",
                Toast.LENGTH_SHORT)
                .show();
    }

    // set newly issued token to proxy and save it on sharedData singleton object
    private void onReceiveToken(String token) {
        // Replace the current proxy with one that uses the token!
        Log.w(TAG, "   --> NOW HAVE TOKEN: " + token);
        proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), token, "1");
        sharedData.setToken(token);
    }

    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, ComposeMessageActivity.class);
        return intent;
    }
}
