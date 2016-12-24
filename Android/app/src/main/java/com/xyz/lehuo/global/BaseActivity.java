package com.xyz.lehuo.global;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by xyz on 15/12/5.
 */
public class BaseActivity extends Activity {


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
