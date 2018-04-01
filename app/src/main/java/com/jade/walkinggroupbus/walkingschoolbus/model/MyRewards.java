package com.jade.walkinggroupbus.walkingschoolbus.model;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * class for storing + accessing all info related to user rewards.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyRewards {

    // 5 themes default, fire, water, spring, dark
    // ex: if default is obtained, obtainedRewards[0] == true
    private List<Boolean> obtainedRewards;
    private int numThemes;

    // Singleton Implementation
    private static MyRewards instance;

    private MyRewards() {
        numThemes = 5;

        obtainedRewards = new ArrayList<Boolean>(5);
        Collections.fill(obtainedRewards, false);
        obtainedRewards.set(0, true);
    }

    // Checks to see if an instance of MyRewards already exists
    public static MyRewards MyRewards(){
        if (instance == null){
            instance = new MyRewards();
            return instance;

        } else {
            return instance;
        }
    }

    public List<Boolean> getObtainedRewards() {
        return Collections.unmodifiableList(obtainedRewards);
    }

    public boolean checkObtainedRewardsByIndex(int rewardIndex) {
        if (rewardIndex > numThemes || rewardIndex < 0) {
            Log.i("error", "rewardIndex out of bounds!");
            return false;
        }
        return obtainedRewards.get(rewardIndex);
    }

    public boolean checkObtainedRewardsByName(String themeName) {
        if (themeName.length() > 0) {
            // standardize the input themeName
            themeName = themeName.toLowerCase();
            themeName = themeName.trim();
            String standardizedThemeName = themeName.substring(0, 1).toUpperCase() + themeName.substring(1);

            // check the name
            ThemeInfo themeInfo = ThemeInfo.ThemeInfo();

            for (int i = 0; i < numThemes; i++) {
                String comparedTheme = themeInfo.getThemes().get(i).getThemeName();
                boolean themeObtained = obtainedRewards.get(i);

                // if names matched + theme is purchased
                if (standardizedThemeName.equals(comparedTheme) && themeObtained) {
                    return true;
                }
            }
        }
        return false;
    }

    public void giveReward(int rewardIndex) {
        if (rewardIndex > numThemes || rewardIndex < 0) {
            Log.i("error", "rewardIndex out of bounds!");
            return;
        }
        obtainedRewards.set(rewardIndex, true);
    }
}
