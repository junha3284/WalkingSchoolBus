package com.jade.walkinggroupbus.walkingschoolbus.model;

import android.graphics.Color;

/**
 * hols the color values or image string names for all the themes
 */

public class Theme {
    private String themeName;

    // icon + image names
    private String backgroundImageName;
    private String[] iconNames;

    Theme(){
        // empty default constructor
    }

    Theme(String themeName) {
        this.themeName = themeName;
    }


    // getters
    public String getThemeName() {
        return themeName;
    }

    public String getBackgroundImageName() {
        return backgroundImageName;
    }

    // setters
    public void setBackgroundImageName(String backgroundImageName) {
        this.backgroundImageName = backgroundImageName;
    }
}
