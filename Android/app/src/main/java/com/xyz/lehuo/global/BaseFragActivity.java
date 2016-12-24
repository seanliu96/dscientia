package com.xyz.lehuo.global;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Created by xyz on 15/12/5.
 */
public class BaseFragActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApplication)getApplication()).getActivityCollector().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((MyApplication)getApplication()).getActivityCollector().removeActivity(this);
    }
}
