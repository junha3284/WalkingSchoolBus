package com.jade.walkinggroupbus.walkingschoolbus.model;

import java.util.ArrayList;
import java.util.List;

/**
 * stores all created themes
 */

public class ThemeInfo {
    private List<Theme> Themes = new ArrayList<Theme>(5);

    // create default theme - leave blank?
    private Theme defaultTheme = new Theme("Default");

    
    // create fire theme
    private Theme fireTheme = new Theme("Fire");


    // create water theme
    private Theme waterTheme = new Theme("Water");


    // create spring theme
    private Theme springTheme = new Theme("Spring");


    // create dark theme
    private Theme darkTheme = new Theme("Dark");

}
