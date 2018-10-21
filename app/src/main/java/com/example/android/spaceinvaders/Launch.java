package com.example.android.spaceinvaders;

import java.io.Serializable;

public class Launch implements Serializable {
    private String name, date, videoURL, location, mapURL, rocketName, missionName, imgURL, description, lsp, countryCode;

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getLocation() {
        return location;
    }

    public String getMapURL() {
        return mapURL;
    }

    public String getImgURL() {
        return imgURL;
    }

    public String getMissionName() {
        return missionName;
    }

    public String getDescription() {
        return description;
    }

    public String getLsp() {
        return lsp;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getRocketName() {
        return rocketName;
    }

    Launch(String n, String d, String v, String l, String m, String rn, String i, String mn, String desc, String agency, String cc) {
        name = n;
        date = d;
        videoURL = v;
        location = l;
        mapURL = m;
        rocketName = rn;
        missionName = mn;
        imgURL = i;
        description = desc;
        lsp = agency;
        countryCode = cc;
    }
}
