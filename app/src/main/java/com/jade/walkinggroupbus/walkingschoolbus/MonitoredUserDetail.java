package com.jade.walkinggroupbus.walkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MonitoredUserDetail extends AppCompatActivity {

    public static final String RESULT_KEY_USER_NAME = "com.jade.walkinggroupbus.walkingschoolbus-name";
    public static final String RESULT_KEY_USER_EMAIL = "com.jade.walkinggroupbus.walkingschoolbus-email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitored_user_detail);
        setTexts();
    }

    public void setTexts(){
        TextView text_name = (TextView) findViewById(R.id.)
    }

    public static Intent makeIntent(Context context, String name, String email) {
        Intent temp = new Intent(context, MonitoredUserDetail.class);
        temp.putExtra(RESULT_KEY_USER_NAME, name);
        temp.putExtra(RESULT_KEY_USER_EMAIL, email);
        return temp;
    }
}
