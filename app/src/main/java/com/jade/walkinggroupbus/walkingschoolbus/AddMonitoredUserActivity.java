package com.jade.walkinggroupbus.walkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddMonitoredUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_monitored_user);
        setAddBtn();
    }

    void setAddBtn(){
        Button btn_add = (Button) findViewById(R.id.button_add);
        btn_add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                EditText edit_email = (EditText) findViewById(R.id.edit_email);
                EditText edit_name = (EditText) findViewById(R.id.edit_name);
                String string_email = edit_email.getText().toString();
                String string_name = edit_name.getText().toString();
                //TODO: use string_email and string_use to validate user and add this person to this user's MonitorsUsers.
            }
        });
    }

    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context,AddMonitoredUserActivity.class);
        return intent;
    }

}
