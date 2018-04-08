package com.jade.walkinggroupbus.walkingschoolbus.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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

        if (!myRewards.getPreviewTheme().equals("Default")
                && !myRewards.getPreviewTheme().equals("Dark")) {
            ImageView img = (ImageView) findViewById(R.id.imageView);
            img.setImageResource(myRewards.getPreviewImgID());
        } else {
            // if theme is default or dark, disable filter
            View filter = (View) findViewById(R.id.filter);
            filter.setVisibility(View.GONE);
        }
    }

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, PreviewThemeActivity.class);
        return intent;
    }
}
