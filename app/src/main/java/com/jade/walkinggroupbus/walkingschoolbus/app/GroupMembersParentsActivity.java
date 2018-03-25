package com.jade.walkinggroupbus.walkingschoolbus.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jade.walkinggroupbus.walkingschoolbus.R;
import com.jade.walkinggroupbus.walkingschoolbus.model.SharedData;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.ProxyBuilder;
import com.jade.walkinggroupbus.walkingschoolbus.proxy.WGServerProxy;

import java.util.List;

import retrofit2.Call;

// note: custom list view from https://www.youtube.com/watch?v=FKUlw7mFXRM

public class GroupMembersParentsActivity extends AppCompatActivity {

    private SharedData sharedData;
    private WGServerProxy proxy;
    private static final String TAG = "ServerTest";

    private Long groupMemberID;
    private List<UserInfo> groupMemberParents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_members_parents);

        sharedData = SharedData.getSharedData();
        String token = sharedData.getToken();

        // check if token is set properly
        if(token != null)
            proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), sharedData.getToken());
        else {
            ProxyBuilder.setOnTokenReceiveCallback(token1 -> onReceiveToken(token1));
        }
        Log.i("gggg", "god fucking damn it 1");
        getIntentData();
        updateListViewWithParentInfo();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        groupMemberID = intent.getLongExtra("PassedGroupMemberID", -1);
    }

    private void updateListViewWithParentInfo() {
        // call server for user info using ID
        Call<UserInfo> caller = proxy.getUserById(groupMemberID);
        ProxyBuilder.callProxy(GroupMembersParentsActivity.this, caller, returnedUser -> response(returnedUser));

    }

    private void response(UserInfo returnedUser){
        groupMemberParents = returnedUser.getMonitoredByUsers();

        // change title text
        TextView tvTitle = (TextView) findViewById(R.id.text_title);
        tvTitle.append(" " + returnedUser.getName());

        /*
        // instantiate custom adapter + link to list view
        ListView lvParentInfo = (ListView) findViewById(R.id.listView_parents);
        CustomAdapter customAdapter = new CustomAdapter();
        lvParentInfo.setAdapter(customAdapter);
        */

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.list_template_members,
                getParentsDescriptions(groupMemberParents));

        ListView list = findViewById(R.id.listView_parents);
        list.setAdapter(adapter);
    }

    private String[] getParentsDescriptions(List<UserInfo> parents){
        int size = parents.size();
        String[] description = new String[size];
        for(int i =0; i < size; i++)
            description[i] = parents.get(i).toStringContactInfo();
        return description;
    }

    // general functions
    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, GroupMembersParentsActivity.class);
        return intent;
    }

    private void onReceiveToken(String token) {
        // Replace the current proxy with one that uses the token!
        Log.w(TAG, "   --> NOW HAVE TOKEN: " + token);
        proxy = ProxyBuilder.getProxy(getString(R.string.API_KEY), token);
        sharedData.setToken(token);
    }


    /*
    // creating custom adapter to show parent info
    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return groupMemberParents.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.list_group_members_parents, null);

            // link variables to text views in custom layout
            TextView tvParentName = (TextView) convertView.findViewById(R.id.text_parentName);
            TextView tvParentEmail = (TextView) convertView.findViewById(R.id.text_parentEmail);
            TextView tvParentCell = (TextView) convertView.findViewById(R.id.text_ParentCell);
            TextView tvParentHome = (TextView) convertView.findViewById(R.id.text_ParentHome);

            // add data to text views
            String parentName = groupMemberParents.get(position).getName();
            String parentEmail = groupMemberParents.get(position).getEmail();
            // TODO: update once additional info from Richard is done
            //String parentCell = groupMemberParents.get(position)
            //String parentHome = groupMemberParents.get(position)

            if (parentName.length() > 0) {
                tvParentName.append(parentName);
            }
            if (parentEmail.length() > 0) {
                tvParentEmail.append(parentEmail);
            }
            return convertView;
        }
    }
    */
}
