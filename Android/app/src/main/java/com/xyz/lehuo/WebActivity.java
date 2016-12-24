package com.xyz.lehuo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.xyz.lehuo.bean.Note;
import com.xyz.lehuo.bean.User;
import com.xyz.lehuo.global.BaseActivity;
import com.xyz.lehuo.global.Conf;
import com.xyz.lehuo.global.Constant;
import com.xyz.lehuo.global.MyApplication;
import com.xyz.lehuo.util.HttpUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xyz on 15/12/30.
 */
public class WebActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "WebActivity";

    public static final String COLLECT_CHANGE = "collect_change";
    public static final String LIKE_CHANGE = "like_change";

    private ImageView col;
    private ImageView like;
    private WebView webView;
    private Note note;
    private ProgressDialog pd;
    private User user;
    private boolean isCollect;
    private boolean isLike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        initView();
        initData();
        initWebview();
    }

    private void initView() {
        col = (ImageView) findViewById(R.id.collection);
        like = (ImageView) findViewById(R.id.like);
        webView = (WebView) findViewById(R.id.webview);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        like.setOnClickListener(this);
        col.setOnClickListener(this);
    }

    private void initData() {
        note = (Note) getIntent().getSerializableExtra("note");
        if (Conf.isLogin == true) {
            user = ((MyApplication)getApplication()).getUser();
            isCollect = user.isActivityCollected(note);
            isLike = user.isActivityFocused(note);
            if (isCollect) {
                col.setImageResource(R.mipmap.collection_pressed);
            } else {
                col.setImageResource(R.mipmap.collection);
            }
            if (isLike) {
                like.setImageResource(R.mipmap.like_pressed);
            } else {
                like.setImageResource(R.mipmap.like);
            }
        } else {
            col.setImageResource(R.mipmap.collection);
            like.setImageResource(R.mipmap.like);
        }
    }

    private void initWebview() {
        webView.setFocusable(true);
        webView.requestFocus(View.FOCUS_DOWN);
        webView.requestFocusFromTouch();
        webView.setVerticalScrollBarEnabled(true);
        WebSettings settings = webView.getSettings();
        settings.setSupportMultipleWindows(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(note.getDetailUrl());
        Log.i(TAG, "url===============>" + note.getDetailUrl());
    }

    @Override
    public void onClick(View v) {
        if (Conf.isLogin == false) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (v.getId()) {
            case R.id.collection:
                sendColRequest();
                break;
            case R.id.like:
                sendLikeRequest();
                break;
        }
    }

    private void sendColRequest() {
        String url;
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("uid", user.getUid()));
        params.add(new BasicNameValuePair("aid", note.getId()));
        Log.i(TAG, "uid===========>" + user.getUid() + " aid=============>" + note.getId());
        if (isCollect) {
            url = Constant.UN_COLLECT;
        } else {
            url = Constant.COLLECT;
        }
        new HttpUtil().create(HttpUtil.POST, url, params, colCallBack);
    }

    private HttpUtil.HttpCallBallListener colCallBack = new HttpUtil.HttpCallBallListener() {
        @Override
        public void onStart() {
            pd = ProgressDialog.show(WebActivity.this, "提示", "加载中，请稍后");
        }

        @Override
        public void onFinish() {
            pd.cancel();
        }

        @Override
        public void onError() {
            Toast.makeText(WebActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSuccess(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getInt("code") == 1) {
                    if (isCollect) {
                        Toast.makeText(WebActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                        isCollect = false;
                        user.cancleCol(note.getId());
                        col.setImageResource(R.mipmap.collection);
                    } else {
                        Toast.makeText(WebActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                        isCollect = true;
                        user.addCollection(note.getId());
                        col.setImageResource(R.mipmap.collection_pressed);
                    }
                    Intent intent = new Intent();
                    intent.setAction(COLLECT_CHANGE);
                    intent.putExtra("activityId", note.getId());
                    sendBroadcast(intent);
                } else {
                    if (isCollect) {
                        Toast.makeText(WebActivity.this, "取消失败", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(WebActivity.this, "收藏失败", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public void sendLikeRequest() {
        String url;
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("uid", user.getUid()));
        params.add(new BasicNameValuePair("aid", note.getId()));
        if (isLike) {
            url = Constant.UN_LIKE;
        } else {
            url = Constant.LIKE;
        }
        new HttpUtil().create(HttpUtil.POST, url, params, likeCallBack);
    }

    private HttpUtil.HttpCallBallListener likeCallBack = new HttpUtil.HttpCallBallListener() {
        @Override
        public void onStart() {
            pd = ProgressDialog.show(WebActivity.this, "提示", "加载中，请稍后");
        }

        @Override
        public void onFinish() {
            pd.cancel();
        }

        @Override
        public void onError() {
            Toast.makeText(WebActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSuccess(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getInt("code") == 1) {
                    if (isLike) {
                        Toast.makeText(WebActivity.this, "取消点赞", Toast.LENGTH_SHORT).show();
                        isLike = false;
                        user.cancleFocus(note.getId());
                        like.setImageResource(R.mipmap.like);
                    } else {
                        Toast.makeText(WebActivity.this, "点赞成功", Toast.LENGTH_SHORT).show();
                        isLike = true;
                        user.addFocus(note.getId());
                        like.setImageResource(R.mipmap.like_pressed);
                    }
                    Intent intent = new Intent();
                    intent.putExtra("activityId", note.getId());
                    intent.setAction(LIKE_CHANGE);
                    sendBroadcast(intent);
                } else {
                    if (isLike) {
                        Toast.makeText(WebActivity.this, "取消失败", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(WebActivity.this, "点赞失败", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
