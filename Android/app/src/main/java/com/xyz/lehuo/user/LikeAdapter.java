package com.xyz.lehuo.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xyz.lehuo.R;
import com.xyz.lehuo.bean.Note;
import com.xyz.lehuo.bean.User;
import com.xyz.lehuo.global.Constant;
import com.xyz.lehuo.global.MyApplication;
import com.xyz.lehuo.util.HttpUtil;
import com.zy.zlistview.adapter.BaseSwipeAdapter;
import com.zy.zlistview.enums.DragEdge;
import com.zy.zlistview.enums.ShowMode;
import com.zy.zlistview.view.ZSwipeItem;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xyz on 16/1/3.
 */
public class LikeAdapter extends BaseSwipeAdapter {

    private Context context;
    private List<Note> activities;
    private ProgressDialog pd;
    private User user;

    public LikeAdapter(Context context, List<Note> activities) {
        this.context = context;
        this.activities = activities;
        user = ((MyApplication)(((android.app.Activity) context).getApplication())).getUser();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_item;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_listview_collection, parent, false);
    }

    @Override
    public void fillValues(final int position, View convertView) {
        final ZSwipeItem swipeItem = (ZSwipeItem) convertView.findViewById(R.id.swipe_item);
        LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.ll);
        swipeItem.setShowMode(ShowMode.PullOut);
        swipeItem.setDragEdge(DragEdge.Right);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("uid", user.getUid()));
                params.add(new BasicNameValuePair("aid", activities.get(position).getId()));
                new HttpUtil().create(HttpUtil.POST, Constant.UN_LIKE, params, new HttpUtil.HttpCallBallListener() {
                    @Override
                    public void onStart() {
                        pd = ProgressDialog.show(context, "提示", "加载中，请稍后");
                    }

                    @Override
                    public void onFinish() {
                        pd.cancel();
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(context, "网络出错", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                user.cancleFocus(activities.get(position).getId());
                                activities.remove(position);
                                notifyDataSetChanged();
                                swipeItem.close();
                            } else {
                                Toast.makeText(context, "取消失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        /*((TextView) convertView.findViewById(R.id.activity_title)).setText(activities.get(position).getTitle());
        ((TextView) convertView.findViewById(R.id.organizer)).setText(activities.get(position).getOrganizer());
        ((TextView) convertView.findViewById(R.id.read_num)).setText(activities.get(position).getReadNum() + "");
        ((TextView) convertView.findViewById(R.id.end_date)).setText(activities.get(position).getEndDate());*/
        Uri uri = Uri.parse(activities.get(position).getImgUrl());
        ((SimpleDraweeView) convertView.findViewById(R.id.activity_img)).setImageURI(uri);

    }

    @Override
    public int getCount() {
        return activities.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void setData(List<Note> activities) {
        this.activities = activities;
        notifyDataSetChanged();
    }

}
