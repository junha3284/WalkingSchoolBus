package com.jade.walkinggroupbus.walkingschoolbus.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by choijun-ha on 2018-03-10.
 */

public class Group {
    private Long id;
    private String GroupDescription;

    private Double[] routeLatArray;
    private Double[] routeLngArray;

    private List<UserInfo> leaders = new ArrayList<>();
    private List<UserInfo> members = new ArrayList<>();

    private String href;
}
