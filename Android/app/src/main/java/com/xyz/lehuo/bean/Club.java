package com.xyz.lehuo.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xyz on 15/12/29.
 */
public class Club implements Serializable {

    private String id;
    private String imgUrl;
    private String name;
    private String intro;
    private ArrayList<Note> activities = new ArrayList<Note>();
    private ArrayList<Note> recentActivities = new ArrayList<Note>();


    public ArrayList<Note> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<Note> activities) {
        this.activities = activities;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Note> getRecentActivities() {
        return recentActivities;
    }

    public void setRecentActivities(ArrayList<Note> recentActivities) {
        this.recentActivities = recentActivities;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
