package com.jade.walkinggroupbus.walkingschoolbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jade.walkinggroupbus.walkingschoolbus.model.TestUserTESTCLASS;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class WalkingGroupsActivity extends AppCompatActivity {

    private ArrayList<ArrayList<TestUserTESTCLASS>> fakeGroupArray = new ArrayList<ArrayList<TestUserTESTCLASS>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking_groups);

        // create array of fake groups
        //ArrayList<ArrayList<TestUserTESTCLASS>> fakeGroupArray = new ArrayList<ArrayList<TestUserTESTCLASS>>();

        for (int j = 0; j < 10; j++) {
            // create fake group to test the list view - array of userInfo
            ArrayList<TestUserTESTCLASS> fakeGroup = new ArrayList<TestUserTESTCLASS>();
            for (int i = 0; i < 10; i++) {
                TestUserTESTCLASS fakePerson = new TestUserTESTCLASS();
                fakeGroup.add(fakePerson);
            }

            fakeGroupArray.add(fakeGroup);
        }

        for (int i = 0; i < 10; i++){
            Log.i("sfdsf", fakeGroupArray.get(0).get(i).getEmail());
        }

        ArrayList<TestUserTESTCLASS> test = fakeGroupArray.get(1);

        // when someone clicks a group
        registerClickCallback();

        // display the data to the list view
        refreshListView(fakeGroupArray);
    }

    private void refreshListView(ArrayList userGroups) {
        // create list of items
        ArrayList<String> groupNameArray = new ArrayList<String>();
        String group = "Group ";
        for (int i = 1; i < 30; i++){
            String iString = "" + i;
            String groupName = group + iString;
            groupNameArray.add(groupName);
        }

        // build adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,                  // context
                R.layout.item_groups,        // layout to use (create)
                groupNameArray);             // items to be displayed

        // configure list view
        ListView list = (ListView) findViewById(R.id.listView_my_groups);
        list.setAdapter(adapter);
    }

    private void registerClickCallback() {
        // set up onclick listener with list view
        ListView list = (ListView) findViewById(R.id.listView_my_groups);

        // configure listener to do what i want
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // id of what was clicked
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id ) {

                // do stuff when list on item is clicked
                // DEBUG
                TextView textView = (TextView) viewClicked;
                String message = "You clicked on item # " + position + 1
                        + ", which is: " + textView.getText().toString();
                Toast.makeText(WalkingGroupsActivity.this, message, Toast.LENGTH_LONG).show();;


                // MOVE TO CALCULATE SERVING SIZE SCREEN;
                ArrayList passedGroup = fakeGroupArray.get(position);

                Intent intent = MyGroupDetailsActivity.makeIntent(WalkingGroupsActivity.this);
                startActivity(intent);
            }
        });
    }
}
