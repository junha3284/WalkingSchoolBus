package com.jade.walkinggroupbus.walkingschoolbus.model;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * class for storing + accessing all info related to user rewards.
 */

public class MyRewards {

    // 5 themes default, fire, water, spring, dark
    // ex: if default is obtained, obtainedRewards[0] == true
    private List<Boolean> obtainedRewards;

    @JsonIgnore
    private List<Theme> themes;

    @JsonIgnore
    private int numThemes;

    // Singleton Implementation
    @JsonIgnore
    private static MyRewards instance;

    private MyRewards() {
        numThemes = 5;

        // setting up the server object
        obtainedRewards = new ArrayList<Boolean>(numThemes);
        for (int i = 0; i < numThemes; i++) {
            obtainedRewards.add(false);
        }
        //Collections.fill(obtainedRewards, false);
        obtainedRewards.set(0, true);


        // implementing themes here
        // TODO: implement themes
        themes = new ArrayList<Theme>(numThemes);

        // create default theme - leave blank?
        Theme defaultTheme = new Theme("Default");

        themes.add(defaultTheme);

        // create fire theme
        Theme fireTheme = new Theme("Fire");

        themes.add(fireTheme);

        // create water theme
        Theme waterTheme = new Theme("Water");

        themes.add(waterTheme);

        // create spring theme
        Theme springTheme = new Theme("Spring");

        themes.add(springTheme);

        // create dark theme
        Theme darkTheme = new Theme("Dark");

        themes.add(darkTheme);
    }

    // singleton function
    public static MyRewards MyRewards(){
        if (instance == null){
            instance = new MyRewards();
            return instance;

        } else {
            return instance;
        }
    }

    // getters
    public List<Boolean> getObtainedRewards() {
        return Collections.unmodifiableList(obtainedRewards);
    }

    @JsonIgnore
    public List<Theme> getThemes() {
        return themes;
    }


    // general functions
    public boolean checkObtainedRewardsByIndex(int themeIndex) {
        if (themeIndex > numThemes || themeIndex < 0) {
            Log.i("error", "rewardIndex out of bounds!");
            return false;
        }
        return obtainedRewards.get(themeIndex);
    }

    public boolean checkObtainedRewardsByName(String themeName) {
        if (themeName.length() > 0) {
            // standardize the input themeName
            themeName = themeName.toLowerCase();
            themeName = themeName.trim();
            String standardizedThemeName = themeName.substring(0, 1).toUpperCase() + themeName.substring(1);

            // check the name
            for (int i = 0; i < numThemes; i++) {
                String comparedTheme = themes.get(i).getThemeName();
                boolean themeObtained = obtainedRewards.get(i);

                // if names matched + theme is purchased
                if (standardizedThemeName.equals(comparedTheme) && themeObtained) {
                    return true;
                }
            }
        }
        return false;
    }

    public void unlockReward(int rewardIndex) {
        if (rewardIndex > numThemes || rewardIndex < 0) {
            Log.i("error", "rewardIndex out of bounds!");
            return;
        }
        obtainedRewards.set(rewardIndex, true);
    }


    // JSON FUNCTIONS
    public String convertToJsonString() {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String obtainedRewardsJsonString = new Gson().toJson(obtainedRewards);
        return obtainedRewardsJsonString;
    }

    // note: this function can be easily abused. only input json string for obtainedRewards or
    // obtainedRewards variable will get messed up
    public void setRewardsWithJson(String jsonString) {
        Type typeToken = new TypeToken<List<Boolean>>(){}.getType();
        obtainedRewards = new Gson().fromJson(jsonString, typeToken);
    }
}
