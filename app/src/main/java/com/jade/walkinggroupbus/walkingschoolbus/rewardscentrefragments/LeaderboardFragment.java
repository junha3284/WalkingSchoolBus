package com.jade.walkinggroupbus.walkingschoolbus.rewardscentrefragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jade.walkinggroupbus.walkingschoolbus.R;

/**
 * Created by Richard Wong on 2018-04-01.
 */

// Code tutorial for android tabs obtained from: https://www.youtube.com/watch?v=bNpWGI_hGGg

public class LeaderboardFragment extends Fragment{
    private static final String TAG = "LeaderboardFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard_tab, container, false);

        // TODO setup leaderboard

        return view;
    }
}
