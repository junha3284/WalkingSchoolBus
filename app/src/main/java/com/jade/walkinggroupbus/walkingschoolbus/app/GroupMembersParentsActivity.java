package com.jade.walkinggroupbus.walkingschoolbus.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.jade.walkinggroupbus.walkingschoolbus.R;


public class GroupMembersParentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_members_parents);

        // use the passed group member ID to get his user INFO THeN DISPLAY THAT SHIT BOI
    }

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, MyGroupDetailsActivity.class);
        return intent;
    }
}
