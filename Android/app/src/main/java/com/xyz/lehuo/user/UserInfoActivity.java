package com.xyz.lehuo.user;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xyz.lehuo.R;
import com.xyz.lehuo.bean.User;
import com.xyz.lehuo.global.BaseActivity;
import com.xyz.lehuo.global.Conf;
import com.xyz.lehuo.global.MyApplication;
import com.xyz.lehuo.util.SPUtil;

/**
 * Created by xyz on 15/12/29.
 */
public class UserInfoActivity extends BaseActivity implements View.OnClickListener {

    public static final int LOGOUT = 11;

    private ImageView back;
    private TextView modify;
    private RelativeLayout logolLayout;
    private SimpleDraweeView userLogol;
    private Button logout;
    private TextView name;
    private TextView sex;
    private TextView major;
    private TextView grade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initView();
        initData();
        initBroadcast();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        modify = (TextView) findViewById(R.id.modify);
        logolLayout = (RelativeLayout) findViewById(R.id.logol_layout);
        userLogol = (SimpleDraweeView) findViewById(R.id.user_logol);
        name = (TextView) findViewById(R.id.name);
        //sex = (TextView) findViewById(R.id.sex);
        //major = (TextView) findViewById(R.id.major);
        //grade = (TextView) findViewById(R.id.grade);
        logout = (Button) findViewById(R.id.logout);
        back.setOnClickListener(this);
        modify.setOnClickListener(this);
        logolLayout.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    private void initData() {
        User user = ((MyApplication)getApplication()).getUser();
        name.setText(user.getName());
        //sex.setText(user.getSex());
        //major.setText(user.getMajor());
        //grade.setText(user.getGrade());
        Uri uri = Uri.parse(user.getAvatar());
        userLogol.setImageURI(uri);
    }

    private void initBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ModifyUserInfoActivity.MODIFY_SUCCESS);
        registerReceiver(mBroadcast, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcast);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                this.finish();
                break;
            case R.id.logol_layout:
                break;
            case R.id.modify:
                startActivity(new Intent(UserInfoActivity.this, ModifyUserInfoActivity.class));
                break;
            case R.id.logout:
                User.clear(this);
                Conf.isLogin = false;
                SPUtil.remove(UserInfoActivity.this, "isLogin");
                ((MyApplication)getApplication()).setUser(null);
                setResult(LOGOUT);
                this.finish();
                break;
        }
    }

    BroadcastReceiver mBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ModifyUserInfoActivity.MODIFY_SUCCESS)) {
                User user = ((MyApplication)getApplication()).getUser();
                //sex.setText(user.getSex());
                //major.setText(user.getMajor());
                //grade.setText(user.getGrade());
                name.setText(user.getName());
                if (!user.getAvatar().equals("")) {
                    userLogol.setImageURI(Uri.parse(user.getAvatar()));
                }
            }
        }
    };
}
