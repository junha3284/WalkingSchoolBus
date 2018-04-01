package com.jade.walkinggroupbus.walkingschoolbus.model;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * class for storing + accessing all info related to user rewards.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyRewards {

    // 5 themes default, fire, water, spring, dark
    // ex: if default is obtained, obtainedRewards[0] == true
    private boolean[] obtainedRewards = {true, false, false, false, false};
    private int numThemes = 5;

    public boolean[] getObtainedRewards() {
        return obtainedRewards;
    }

    public void giveReward(int rewardIndex) {
        if (rewardIndex > numThemes || rewardIndex < 0) {
            Log.i("error", "rewardIndex out of bounds!");
            return;
        }
        obtainedRewards[rewardIndex] = true;
    }
}
