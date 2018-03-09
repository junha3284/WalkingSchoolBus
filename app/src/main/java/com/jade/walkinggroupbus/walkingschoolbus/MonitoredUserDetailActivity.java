package com.jade.walkinggroupbus.walkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MonitoredUserDetailActivity extends AppCompatActivity {

    public static final String RESULT_KEY_USER_NAME = "com.jade.walkinggroupbus.walkingschoolbus-name";
    public static final String RESULT_KEY_USER_EMAIL = "com.jade.walkinggroupbus.walkingschoolbus-email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitored_user_detail);
        setTexts();
        setBtns();
    }

    public void setTexts(){
        Intent intent = getIntent();
        TextView text_name = (TextView) findViewById(R.id.text_name);
        TextView text_email = (TextView) findViewById(R.id.text_email);
        text_name.setText(intent.getStringExtra(RESULT_KEY_USER_NAME));
        text_email.setText(intent.getStringExtra(RESULT_KEY_USER_EMAIL));
    }

    private void setBtns() {
        Button removeBtn = (Button) findViewById(R.id.button_remove);
        Button walkingGroupBtn = (Button) findViewById(R.id.button_create_WalkingGroupActivity);
        removeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //todo: remove the user who is the owner of the detailed info from the logged in user
                finish();
            }
        });
        walkingGroupBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //todo: connect to WalkingGroupsActivity of the user who is the owner of the detailed info
            }
        });

    }


    public static Intent makeIntent(Context context, String name, String email) {
        Intent temp = new Intent(context, MonitoredUserDetailActivity.class);
        temp.putExtra(RESULT_KEY_USER_NAME, name);
        temp.putExtra(RESULT_KEY_USER_EMAIL, email);
        return temp;
    }
}
