package com.xyz.lehuo.global;

import android.app.Application;

import com.xyz.lehuo.bean.User;

/**
 * Created by xyz on 15/12/5.
 */
public class MyApplication extends Application {

    private ActivityCollector activityCollector;
    private User user;

    @Override
    public void onCreate() {
        super.onCreate();
        activityCollector = new ActivityCollector();
    }

    public ActivityCollector getActivityCollector() {
        return activityCollector;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
