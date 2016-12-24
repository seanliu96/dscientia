package com.xyz.lehuo.First;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xyz.lehuo.R;
import com.xyz.lehuo.WebActivity;
import com.xyz.lehuo.bean.Note;
import com.xyz.lehuo.datebase.DatabaseManager;
import com.xyz.lehuo.global.Constant;
import com.xyz.lehuo.util.HttpUtil;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xyz on 15/12/24.
 */
public class FirstFragment extends Fragment {

    public static final String TAG = "FirstFragment";

    private SwipeRefreshLayout refreshLayout;
    private ListView listView;
    private List<com.xyz.lehuo.bean.Note> activities = new ArrayList<com.xyz.lehuo.bean.Note>();
    private FirstAdapter adapter;
    private ViewPager vp;
    private View headView;
    private TextView title;
    private BannerAdapter bannerAdapter;
    private LinearLayout dotGroups;
    private List<ImageView> dots = new ArrayList<>();
    private List<String> bannerUrls = new ArrayList<String>();
    private ProgressDialog pd;
    private DatabaseManager databaseManager;
    private List<Note> allActivities = new ArrayList<>();

    ImageHandler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        databaseManager = new DatabaseManager(getActivity());
        initView(view);
        initListener();
        initData();
        return view;
    }

    private void initView(View v) {
        headView = LayoutInflater.from(getActivity()).inflate(R.layout.banner_headview, null);
        title = (TextView) headView.findViewById(R.id.title);
        vp = (ViewPager) headView.findViewById(R.id.vp);
        dotGroups = (LinearLayout) headView.findViewById(R.id.dot_groups);
        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh_layout);
        listView = (ListView) v.findViewById(R.id.list);
        listView.addHeaderView(headView);
        adapter = new FirstAdapter(activities, getActivity());
        listView.setAdapter(adapter);
        bannerAdapter = new BannerAdapter(getActivity(), bannerUrls);
        vp.setAdapter(bannerAdapter);
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dots.size(); i++) {
                    if (i == position % bannerUrls.size()) {
                        dots.get(i).setImageResource(R.drawable.dot_focused);
                    } else {
                        dots.get(i).setImageResource(R.drawable.dot_normal);
                    }
                }
                title.setText(allActivities.get(position % bannerUrls.size()).getNoteName());
                handler.sendMessage(Message.obtain(handler,
                        ImageHandler.MSG_PAGE_CHANGED, position, 0));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (bannerUrls.size() > 1) {
                    switch (state) {
                        case ViewPager.SCROLL_STATE_DRAGGING:
                            handler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);
                            break;
                        case ViewPager.SCROLL_STATE_IDLE:
                            handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE,
                                    ImageHandler.MSG_DELAY);
                            break;
                        case ViewPager.SCROLL_STATE_SETTLING:
                            break;
                        default:
                            break;
                    }
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("activity", activities.get(position - 1));
                startActivity(intent);
            }
        });
    }

    private void initData() {
        handler = new ImageHandler(this);
        List<NameValuePair> params = new ArrayList<>();
        new HttpUtil().create(HttpUtil.POST, Constant.GET_RECOMMEND__ACTS, params, getData);
    }

    private void initListener() {
        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                List<NameValuePair> params = new ArrayList<>();
                new HttpUtil().create(HttpUtil.POST, Constant.GET_RECOMMEND__ACTS, params, refreshData);
            }
        });
    }

    private int getPixels(int dipValue) {
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dipValue, r.getDisplayMetrics());
        return px;
    }

    private void initDotGroup() {
        for (int i = 0; i < bannerUrls.size(); i++) {
            ImageView dot = new ImageView(getActivity());
            if (i == 0) {
                dot.setImageResource(R.drawable.dot_focused);
            } else {
                dot.setImageResource(R.drawable.dot_normal);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getPixels(8), getPixels(8));
            params.setMargins(getPixels(3), 0, getPixels(3), 0);
            dot.setLayoutParams(params);
            dot.setPadding(getPixels(1), 0, getPixels(1), 0);
            dots.add(dot);
            dotGroups.addView(dot);
        }
        handler.sendEmptyMessageDelayed(ImageHandler.MSG_BREAK_SILENT,
                ImageHandler.MSG_DELAY);
    }

    private void dealResult(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getInt("code") == 1) {
                allActivities.clear();
                activities.clear();
                bannerUrls.clear();
                JSONArray array = jsonObject.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    //Note activity = new Note();
                    Note activity = new Note();
                    JSONObject jo = array.optJSONObject(i);
                    activity.setDate(jo.getString("note_date"));
                    activity.setContent(jo.getString("note_content"));
                    activity.setWeek(jo.getString("note_week"));
                    activity.setNoteName(jo.getString("note_name"));
                    activity.setImgUrl(jo.getString("img_url"));
                    activity.setId(jo.getString("note_id"));
                    activity.setDetailUrl(jo.getString("detail_url"));
                    allActivities.add(activity);
                    if (i < 5) {
                        bannerUrls.add(activity.getImgUrl());
                    } else {
                        activities.add(activity);
                    }
                }
                databaseManager.clearActivityTable();
                databaseManager.addActivities(allActivities);
                adapter.setData(activities);
                bannerAdapter.setData(bannerUrls);
            } else {
                Toast.makeText(getActivity(), "服务器错误", Toast.LENGTH_SHORT).show();
                bannerUrls.clear();
                activities.clear();
                allActivities = databaseManager.getAllActivities();
                for (int i = 0; i < allActivities.size(); i++) {
                    if (i < 5) {
                        bannerUrls.add(allActivities.get(i).getImgUrl());
                    } else {
                        activities.add(allActivities.get(i));
                    }
                }
                bannerAdapter.setData(bannerUrls);
                adapter.setData(activities);
            }
            if (dots.size() == 0) {
                initDotGroup();
                //vp.setCurrentItem(bannerUrls.size() * 7, false);
                Log.i(TAG, "=============>" + bannerUrls.size() * 7);
                vp.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        vp.setCurrentItem(bannerUrls.size() * 7, false);
                    }
                }, 100);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    HttpUtil.HttpCallBallListener getData = new HttpUtil.HttpCallBallListener() {
        @Override
        public void onStart() {
            pd = ProgressDialog.show(getActivity(), "提示", "加载中，请稍后");
        }

        @Override
        public void onFinish() {
            pd.cancel();
        }

        @Override
        public void onError() {
            Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
            bannerUrls.clear();
            activities.clear();
            /*allActivities = databaseManager.getAllActivities();
            for (int i = 0; i < allActivities.size(); i++) {
                if (i < 5) {
                    bannerUrls.add(allActivities.get(i).getImgUrl());
                } else {
                    activities.add(allActivities.get(i));
                }
            }*/
            bannerAdapter.setData(bannerUrls);
            adapter.setData(activities);
            if (dots.size() == 0) {
                initDotGroup();
                //vp.setCurrentItem(bannerUrls.size() * 7, false);
                vp.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        vp.setCurrentItem(bannerUrls.size() * 7, false);
                    }
                }, 100);
            }
        }

        @Override
        public void onSuccess(String result) {
            dealResult(result);
        }
    };

    HttpUtil.HttpCallBallListener refreshData = new HttpUtil.HttpCallBallListener() {
        @Override
        public void onStart() {
        }

        @Override
        public void onFinish() {
            refreshLayout.setRefreshing(false);
        }

        @Override
        public void onError() {
            Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
            bannerUrls.clear();
            activities.clear();
            allActivities = databaseManager.getAllActivities();

            for (int i = 0; i < allActivities.size(); i++) {
                if (i < 5) {
                    bannerUrls.add(allActivities.get(i).getImgUrl());
                } else {
                    activities.add(allActivities.get(i));
                }
            }

            bannerAdapter.setData(bannerUrls);
            adapter.setData(activities);
            if (dots.size() == 0) {
                initDotGroup();
                //vp.setCurrentItem(bannerUrls.size() * 7, false);
                vp.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        vp.setCurrentItem(bannerUrls.size() * 7 + 1, false);
                    }
                }, 100);
            }
        }

        @Override
        public void onSuccess(String result) {
            dealResult(result);
        }
    };


    public class ImageHandler extends Handler {
        /**
         * 请求更新显示的View。
         */
        public static final int MSG_UPDATE_IMAGE = 1;
        /**
         * 请求暂停轮播。
         */
        public static final int MSG_KEEP_SILENT = 2;
        /**
         * 请求恢复轮播。
         */
        public static final int MSG_BREAK_SILENT = 3;
        /**
         * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使轮播的页面出错。
         * 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页，
         * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。
         */
        public static final int MSG_PAGE_CHANGED = 4;

        // 轮播间隔时间
        public static final long MSG_DELAY = 4000;

        private FirstFragment firstFragment;
        private int currentItem = 0;

        public ImageHandler(FirstFragment fragment) {
            // TODO Auto-generated constructor stub
            this.firstFragment = fragment;
        }

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if (firstFragment == null) {
                // Fragment已经回收，无需再处理UI了
                return;
            }
            if (firstFragment.handler != null
                    && firstFragment.handler
                    .hasMessages(ImageHandler.MSG_UPDATE_IMAGE)) {
                firstFragment.handler
                        .removeMessages(ImageHandler.MSG_UPDATE_IMAGE);
            }
            switch (msg.what) {
                case MSG_UPDATE_IMAGE:
                    currentItem++;
                    firstFragment.vp.setCurrentItem(currentItem);
                    firstFragment.handler.sendEmptyMessageDelayed(
                            MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_KEEP_SILENT:
                    break;
                case MSG_BREAK_SILENT:
                    firstFragment.handler.sendEmptyMessageDelayed(
                            MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_PAGE_CHANGED:
                    currentItem = msg.arg1;
                    break;
                default:
                    break;
            }
        }
    }

    class BannerAdapter extends PagerAdapter {

        private Context context;
        private List<String> urls;
        private List<SimpleDraweeView> imageViews;

        public BannerAdapter(Context context, List<String> urls) {
            this.context = context;
            initUrls(urls);
        }

        void initUrls(List<String> urls) {
            this.urls = urls;
            imageViews = new ArrayList<SimpleDraweeView>();
            for (int i = 0; i < urls.size(); i++) {
                Uri uri = Uri.parse(urls.get(i));
                SimpleDraweeView view = new SimpleDraweeView(context, null, R.style.banner_image);
                view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                view.setImageURI(uri);
                imageViews.add(view);
            }
        }

        @Override
        public int getCount() {
            return urls.size() > 1 ? 200 : urls.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (urls.size() == 0) {
                return null;
            } else {
                position = position % urls.size();
                if (position < 0) {
                    position = imageViews.size() + position;
                }
                final int curPos = position;

                SimpleDraweeView image = imageViews.get(position);
                ViewParent vp = image.getParent();
                if (vp != null) {
                    ((ViewGroup) vp).removeView(image);
                }
                container.addView(image);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), WebActivity.class);
                        intent.putExtra("activity", allActivities.get(curPos));
                        startActivity(intent);
                    }
                });
                return image;
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }

        public void setData(List<String> urls) {
            this.urls = urls;
            if (imageViews.size() < urls.size()) {
                for (int i = imageViews.size(); i < urls.size(); i++) {
                    Uri uri = Uri.parse(urls.get(i));
                    SimpleDraweeView view = new SimpleDraweeView(context, null, R.style.banner_image);
                    view.setImageURI(uri);
                    imageViews.add(view);
                }
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeMessages(ImageHandler.MSG_BREAK_SILENT);
        handler.removeMessages(ImageHandler.MSG_KEEP_SILENT);
        handler.removeMessages(ImageHandler.MSG_PAGE_CHANGED);
        handler.removeMessages(ImageHandler.MSG_UPDATE_IMAGE);
    }
}
