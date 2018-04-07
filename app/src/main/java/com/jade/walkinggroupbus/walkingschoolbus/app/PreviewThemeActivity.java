package com.jade.walkinggroupbus.walkingschoolbus.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jade.walkinggroupbus.walkingschoolbus.R;
import com.jade.walkinggroupbus.walkingschoolbus.model.MyRewards;

public class PreviewThemeActivity extends AppCompatActivity {

    private MyRewards myRewards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myRewards = MyRewards.MyRewards();
        setTheme(myRewards.getPreviewThemeID());


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_theme);
    }

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, PreviewThemeActivity.class);
        return intent;
    }
}
