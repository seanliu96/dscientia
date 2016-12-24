package com.xyz.lehuo.user;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.xyz.lehuo.R;
import com.xyz.lehuo.WebActivity;
import com.xyz.lehuo.bean.Note;
import com.xyz.lehuo.bean.User;
import com.xyz.lehuo.global.BaseActivity;
import com.xyz.lehuo.global.Constant;
import com.xyz.lehuo.global.MyApplication;
import com.xyz.lehuo.util.HttpUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xyz on 15/12/30.
 */
public class CollectionActivity extends BaseActivity {

    public static final String TAG = "CollectionActivity";

    ListView listView;
    SwipeRefreshLayout refreshLayout;
    List<Note> activities = new ArrayList<Note>();
    CollectionAdapter adapter;
    ImageView back;
    ProgressDialog pd;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        initView();
        initData();
        initBroadcast();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        listView = (ListView) findViewById(R.id.listview);
        adapter = new CollectionAdapter(this, activities);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CollectionActivity.this, WebActivity.class);
                intent.putExtra("activity", activities.get(position));
                startActivity(intent);
            }
        });
        listView.setAdapter(adapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionActivity.this.finish();
            }
        });
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("uid", user.getUid()));
                Log.i(TAG, "uid==============>" + user.getUid());
                new HttpUtil().create(HttpUtil.POST, Constant.GET_USER_COLLECT_ACTS, params, new HttpUtil.HttpCallBallListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onFinish() {
                        refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(CollectionActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(String result) {
                        dealResult(result);
                    }
                });
            }
        });
    }

    private void initData() {
        user = ((MyApplication)getApplication()).getUser();
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("uid", user.getUid()));
        Log.i(TAG, "uid==============>" + user.getUid());
        new HttpUtil().create(HttpUtil.POST, Constant.GET_USER_COLLECT_ACTS, params, new HttpUtil.HttpCallBallListener() {
            @Override
            public void onStart() {
                pd = ProgressDialog.show(CollectionActivity.this, "提示", "加载中，请稍后");
            }

            @Override
            public void onFinish() {
                pd.cancel();
            }

            @Override
            public void onError() {
                Toast.makeText(CollectionActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String result) {
                dealResult(result);
            }
        });
    }

    private void dealResult(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getInt("code") == 1) {
                JSONArray data = jsonObject.getJSONArray("data");
                activities.clear();
                for (int i = 0; i < data.length(); i++) {
                    JSONObject jo = data.optJSONObject(i);
                    Note note = new Note();
                    note.setId(jo.getString("id"));
                    note.setDetailUrl(jo.getString("detail_url"));
                    /*note.setReadNum(Integer.parseInt(jo.getString("read_nums")));
                    note.setEndDate(jo.getString("end_date"));
                    note.setEndTime(jo.getString("end_time"));
                    note.setTitle(jo.getString("title"));
                    note.setImgUrl(jo.getString("img_url"));
                    note.setStartTime(jo.getString("start_time"));
                    note.setStartDate(jo.getString("start_date"));
                    note.setOrganizer(jo.getString("organizer"));
                    note.setType(jo.getString("type"));*/
                    activities.add(note);
                }
                adapter.setData(activities);
            } else {
                Toast.makeText(CollectionActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WebActivity.COLLECT_CHANGE);
        registerReceiver(mBroadcast, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcast);
    }

    BroadcastReceiver mBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(WebActivity.COLLECT_CHANGE)) {
                for (Note a : activities) {
                    if (a.getId().equals(intent.getStringExtra("activityId"))) {
                        activities.remove(a);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
    };
}
