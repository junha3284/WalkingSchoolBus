package com.jade.walkinggroupbus.walkingschoolbus.model;

import android.graphics.Color;

/**
 * hols the color values or image string names for all the themes
 */

public class Theme {
    private String themeName;

    // colors
    private Color textViewColor;
    private Color titleColor;
    private Color backgroundColor;
    private Color buttonColor;
    private Color buttonTextColor;

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

    public Color getTextViewColor() {
        return textViewColor;
    }

    public Color getTitleColor() {
        return titleColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getButtonColor() {
        return buttonColor;
    }

    public Color getButtonTextColor() {
        return buttonTextColor;
    }

    public String getBackgroundImageName() {
        return backgroundImageName;
    }

    public String[] getIconNames() {
        return iconNames;
    }




    // setters
    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public void setTextViewColor(Color textViewColor) {
        this.textViewColor = textViewColor;
    }

    public void setTitleColor(Color titleColor) {
        this.titleColor = titleColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setButtonColor(Color buttonColor) {
        this.buttonColor = buttonColor;
    }

    public void setButtonTextColor(Color buttonTextColor) {
        this.buttonTextColor = buttonTextColor;
    }

    public void setBackgroundImageName(String backgroundImageName) {
        this.backgroundImageName = backgroundImageName;
    }

    public void setIconNames(String[] iconNames) {
        this.iconNames = iconNames;
    }
}
