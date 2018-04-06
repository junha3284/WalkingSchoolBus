package com.jade.walkinggroupbus.walkingschoolbus.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jade.walkinggroupbus.walkingschoolbus.R;

public class PermissionCentreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_centre);

        setBtns();
    }

    void setBtns(){
        //TODO: connect permission-related activity with PermissionCentreActivity
    }

    static Intent makeIntent(Context context){
        Intent intent = new Intent(context, PermissionCentreActivity.class);
        return intent;
    }
}
