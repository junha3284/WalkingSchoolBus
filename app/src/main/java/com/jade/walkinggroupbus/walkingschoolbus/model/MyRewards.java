package com.jade.walkinggroupbus.walkingschoolbus.model;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jade.walkinggroupbus.walkingschoolbus.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * class for storing + accessing all info related to user rewards.
 * stores theme info + theme preferences
 */

public class MyRewards {

    // 5 themes default, fire, water, spring, dark
    // ex: if default is obtained, obtainedRewards[0] == true
    private List<Boolean> obtainedRewards;

    private String selectedTheme;

    @JsonIgnore
    private String previewTheme;

    @JsonIgnore
    private int selectedIndex;

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
        obtainedRewards.set(0, true);


        // implementing themes here
        themes = new ArrayList<Theme>(numThemes);

        // create default theme - leave blank?
        Theme defaultTheme = new Theme("Default");

        themes.add(defaultTheme);

        // create fire theme
        Theme fireTheme = new Theme("Fire");

        themes.add(fireTheme);

        // create water theme
        Theme oceanTheme = new Theme("Ocean");

        themes.add(oceanTheme);

        // create spring theme
        Theme springTheme = new Theme("Spring");

        themes.add(springTheme);

        // create dark theme
        Theme darkTheme = new Theme("Dark");

        themes.add(darkTheme);


        // set default theme type
        selectedTheme = defaultTheme.getThemeName();
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
        return obtainedRewards;
    }

    public String getSelectedTheme() {
        return selectedTheme;
    }

    public String getPreviewTheme() {
        return previewTheme;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }


    @JsonIgnore
    public List<Theme> getThemes() {
        return themes;
    }

    @JsonIgnore
    public int getSelectedThemeID() {
        if (selectedTheme.equals("Dark")) {
            return R.style.Dark;
        } else if (selectedTheme.equals("Fire")) {
            return R.style.Fire;
        } else if (selectedTheme.equals("Ocean")) {
            return R.style.Ocean;
        } else if (selectedTheme.equals("Spring")) {
            return R.style.Spring;
        } else {
            return R.style.Default;
        }
    }

    @JsonIgnore
    public int getPreviewThemeID() {
        if (previewTheme.equals("Dark")) {
            return R.style.Dark;
        } else if (previewTheme.equals("Fire")) {
            return R.style.Fire;
        } else if (previewTheme.equals("Ocean")) {
            return R.style.Ocean;
        } else if (previewTheme.equals("Spring")) {
            return R.style.Spring;
        } else {
            return R.style.Default;
        }
    }

    @JsonIgnore
    public int getSelectedImgID() {
        if (selectedTheme.equals("Fire")) {
            return R.drawable.fire;
        } else if (selectedTheme.equals("Ocean")) {
            return R.drawable.ocean;
        } else if (selectedTheme.equals("Spring")) {
            return R.drawable.spring;
        }
        return -1;
    }

    @JsonIgnore
    public int getPreviewImgID() {
        if (previewTheme.equals("Fire")) {
            return R.drawable.fire;
        } else if (previewTheme.equals("Ocean")) {
            return R.drawable.ocean;
        } else if (previewTheme.equals("Spring")) {
            return R.drawable.spring;
        }
        return -1;
    }


    // setters
    public void setSelectedTheme(String selectedTheme) {
        this.selectedTheme = selectedTheme;
    }

    // setters
    public void setPreviewTheme(String previewTheme) {
        this.previewTheme = previewTheme;
    }

    public void setObtainedRewards(List<Boolean> obtainedRewards) {
        this.obtainedRewards = obtainedRewards;
    }


    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
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
        if (rewardIndex > numThemes - 1 || rewardIndex < 0) {
            Log.i("error", "rewardIndex out of bounds!");
            return;
        }
        obtainedRewards.set(rewardIndex, true);
    }





    // JSON FUNCTIONS
    public String convertToJsonString() {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String myRewardsJsonString = new Gson().toJson(instance);
        return myRewardsJsonString;
    }

    // note: this function can be easily abused. only input json string for obtainedRewards or
    // obtainedRewards variable will get messed up
    public void setRewardsWithJson(String jsonString) {
        Type typeToken = new TypeToken<MyRewards>(){}.getType();
        MyRewards jsonObj = new Gson().fromJson(jsonString, typeToken);

        instance.setSelectedTheme(jsonObj.getSelectedTheme());
        instance.setObtainedRewards(jsonObj.getObtainedRewards());
    }
}
