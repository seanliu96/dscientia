package com.xyz.lehuo.global;

import android.os.Environment;

/**
 * Created by xyz on 15/12/23.
 */
public class Constant {
    public static final String SERVER_ADDRESS = "http://172.18.71.35:8080";
    public static final String LOGIN_API = SERVER_ADDRESS + "/api/login";
    public static final String REGISTER_API = SERVER_ADDRESS + "/api/register";
    public static final String UPDATE_USER_API = SERVER_ADDRESS + "/api/user/update";
    public static final String UPDATE_USER_LOGO_API = SERVER_ADDRESS + "/api/user/upload";
    public static final String ADD_NOTEBOOK_API = SERVER_ADDRESS + "/api/user/addnotebook";
    public static final String UPDATE_NOTEBOOK_API = SERVER_ADDRESS + "/api/notebook/update";
    public static final String UPDATE_NOTEBOOK_COVER_API = SERVER_ADDRESS + "/api/notebook/upload";
    public static final String ADD_NOTE_API = SERVER_ADDRESS + "/api/notebook/addnote";
    public static final String UPDATE_NOTE_API = SERVER_ADDRESS + "/api/note/update";
    public static final String DATABASE_NAME = "Dscientia.db";
    public static final String NOTEBOOK_TABLE_NAME = "NoteBooks";
    public static final String NOTE_TABLE_NAME = "Notes";
    public static final String APP_PATH = Environment.getExternalStorageDirectory() + "/Dscientia/";
    public static final String FILE_NAME = "config";


    /*public static final String SERVER_ADDRESS = "http://172.18.71.35:8080";
    public static final String LOGIN_API = SERVER_ADDRESS + "/api/login";
    public static final String REGISTER_API = SERVER_ADDRESS + "/api/register";
    public static final String UPDATE_USER_API = SERVER_ADDRESS + "/api/user/update";
    public static final String UPDATE_USER_LOGO_API = SERVER_ADDRESS + "/api/user/upload";*/
    public static final String GET_ALL_CLUBS = SERVER_ADDRESS + "/api/club/index";
    public static final String GET_ALL_ACTS_BY_CLUB = SERVER_ADDRESS + "/api/activity/get_all_acts_by_club";
    public static final String GET_SOME_ACTS_BY_TYPE = SERVER_ADDRESS + "/api/activity/get_some_acts_by_type";
    public static final String GET_MORE_ACTS_BY_TYPE = SERVER_ADDRESS + "/api/activity/get_more_acts_by_type";
    public static final String COLLECT = SERVER_ADDRESS + "/api/user/collect";
    public static final String UN_COLLECT = SERVER_ADDRESS + "/api/user/uncollect";
    public static final String LIKE = SERVER_ADDRESS + "/api/user/like";
    public static final String UN_LIKE = SERVER_ADDRESS + "/api/user/unlike";
    public static final String GET_RECOMMEND__ACTS = SERVER_ADDRESS + "/api/activity/get_recommend_acts";
    public static final String GET_USER_COLLECT_ACTS = SERVER_ADDRESS + "/api/activity/get_user_collect_acts";
    public static final String GET_USER_LIKE_ACTS = SERVER_ADDRESS + "/api/activity/get_user_like_acts";
    public static final String GET_ACT_BY_SCAN = SERVER_ADDRESS + "/api/activity/get_act_by_url";
    public static final String ACTIVITY_TABLE_NAME = "activity";
    public static final String CLUB_TABLE_NAME = "club";
    /*public static final String DATABASE_NAME = "lehuo.db";

    public static final String APP_PATH = Environment.getExternalStorageDirectory() + "/lehuo/";
    public static final String FILE_NAME = "config";

    public static final String [] majors = {"软件学院", "环境卫生学院", "中山医学院", "传播与设计学院", "移动信息工程学院",
                                            "管理学院", "心理学院", "信息科学与计算机学院"};
    public static final String [] grades = {"大一", "大二", "大三", "大四"};
    public static final String [] activity_types = {"展览", "讲座", "招聘", "公益", "戏剧"};

    public static String majorNum(String major) {
        for (int i = 0; i < majors.length; i++) {
            if (majors[i].equals(major)) {
                return new String("" + i);
            }
        }
        return new String("");
    }

    public static String gradeNum(String grade) {
        for (int i = 0; i < grades.length; i++) {
            if (grades[i].equals(grade)) {
                return new String("" + i);
            }
        }
        return new String("");
    }*/

}