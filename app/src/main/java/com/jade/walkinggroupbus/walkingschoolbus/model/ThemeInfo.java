package com.jade.walkinggroupbus.walkingschoolbus.model;

import java.util.ArrayList;
import java.util.List;

/**
 * stores all created themes in a singleton
 */

public class ThemeInfo {
    private List<Theme> themes;

    // Singleton Implementation
    private static ThemeInfo instance;

    // prevents public instantiation
    private ThemeInfo(){
        themes = new ArrayList<Theme>(5);

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

    // Checks to see if an instance of ThemeInfo already exists
    public static ThemeInfo ThemeInfo(){
        if (instance == null){
            instance = new ThemeInfo();
            return instance;

        } else {
            return instance;
        }
    }

    // getter
    public List<Theme> getThemes() {
        return themes;
    }
}
