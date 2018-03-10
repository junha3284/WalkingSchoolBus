package com.jade.walkinggroupbus.walkingschoolbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jade.walkinggroupbus.walkingschoolbus.model.GroupsInfo;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;

import java.util.List;

public class JoinGroupDetailsActivity extends AppCompatActivity {

    private GroupsInfo groupsInfo = GroupsInfo.getInstance();
    private UserInfo user = UserInfo.userInfo();
    private String groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group_details);

        getIntentData();

        setGroupText();

        setJoinGroupButton();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        groupName = intent.getStringExtra("group name");
    }

    private void setGroupText() {
        String destination = groupsInfo.getDestination(groupName);
        String meetingPlace = groupsInfo.getMeetingPlace(groupName);
        List<String> members = groupsInfo.getMembers(groupName);

        TextView textDestination = findViewById(R.id.text_destination);
        textDestination.setText(destination);

        TextView textMeetingPlace = findViewById(R.id.text_meeting_place);
        textMeetingPlace.setText(meetingPlace);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_template_members, members);

        ListView list = findViewById(R.id.list_members);
        list.setAdapter(adapter);
    }


    private void setJoinGroupButton() {
        Button button = findViewById(R.id.button_join);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.addWalkingGroup(groupName);

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
