package com.jade.walkinggroupbus.walkingschoolbus.app.rewardscentrefragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jade.walkinggroupbus.walkingschoolbus.R;
import com.jade.walkinggroupbus.walkingschoolbus.app.ExitWalkDialogFragment;
import com.jade.walkinggroupbus.walkingschoolbus.app.MainMenuActivity;
import com.jade.walkinggroupbus.walkingschoolbus.app.OnWalkMapPanicPrompt;
import com.jade.walkinggroupbus.walkingschoolbus.app.RewardsCentreBuyPrompt;
import com.jade.walkinggroupbus.walkingschoolbus.model.MyRewards;
import com.jade.walkinggroupbus.walkingschoolbus.model.UserInfo;

public class ShopFragment extends Fragment{
    private static final String TAG = "ShopFragment";
    private MyRewards myRewards;
    private UserInfo userInfo;
    private String message;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myRewards = MyRewards.MyRewards();

        // set theme
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), myRewards.getSelectedThemeID());
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        View view = localInflater.inflate(R.layout.fragment_shop_tab, container, false);

        // set background
        if (!myRewards.getSelectedTheme().equals("Default")
                && !myRewards.getSelectedTheme().equals("Dark")) {
            ImageView img = (ImageView) view.findViewById(R.id.imageView);
            img.setImageResource(myRewards.getSelectedImgID());
        } else {
            // if theme is default or dark, disable filter
            View filter = (View) view.findViewById(R.id.filter);
            filter.setVisibility(View.GONE);
        }

        myRewards = MyRewards.MyRewards();
        userInfo = UserInfo.userInfo();
        message = "" + userInfo.getCurrentPoints();

        setUpBuyButtons(view);

        // TODO track points in the activity
        TextView pointsView = (TextView) view.findViewById(R.id.RCA_text_point_display);
        pointsView.setText(message);

        return view;
    }

    // TODO prompt user to confirm their purchase, check if they already have it, and to subtract their current points
    private void setUpBuyButtons(View view) {
        Button buyFireBtn = (Button) view.findViewById(R.id.RCA_button_fire_buy);
        Button buyWaterBtn = (Button) view.findViewById(R.id.RCA_button_water_buy);
        Button buySpringBtn = (Button) view.findViewById(R.id.RCA_button_spring_buy);
        Button buyDarkBtn = (Button) view.findViewById(R.id.RCA_button_dark_buy);

        buyFireBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRewards.setPreviewTheme("Fire");
                myRewards.setSelectedIndex(1);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                RewardsCentreBuyPrompt rewardsCentreBuyPrompt = new RewardsCentreBuyPrompt();
                rewardsCentreBuyPrompt.show(fragmentManager, "buyFire");

            }
        });

        buyWaterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRewards.setPreviewTheme("Water");
                myRewards.setSelectedIndex(2);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                RewardsCentreBuyPrompt rewardsCentreBuyPrompt = new RewardsCentreBuyPrompt();
                rewardsCentreBuyPrompt.show(fragmentManager, "buyWater");

            }
        });

        buySpringBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRewards.setPreviewTheme("Spring");
                myRewards.setSelectedIndex(3);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                RewardsCentreBuyPrompt rewardsCentreBuyPrompt = new RewardsCentreBuyPrompt();
                rewardsCentreBuyPrompt.show(fragmentManager, "buySpring");
            }
        });

        buyDarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRewards.setPreviewTheme("Dark");
                myRewards.setSelectedIndex(4);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                RewardsCentreBuyPrompt rewardsCentreBuyPrompt = new RewardsCentreBuyPrompt();
                rewardsCentreBuyPrompt.show(fragmentManager, "buyDark");
            }
        });
    }
}

