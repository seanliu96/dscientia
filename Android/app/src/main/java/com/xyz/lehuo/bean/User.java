package com.xyz.lehuo.bean;

import android.content.Context;

import com.xyz.lehuo.util.SPUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xyz on 15/12/18.
 */
public class User {

    private String uid;
    private String name;
    private String avatar;
    /*private String major;
    private String grade;
    private String sex;*/
    private int colsNum;
    private int focusNum;
    private List<String> cols;
    private List<String> focus;

    public User() {
        cols = new ArrayList<String>();
        focus = new ArrayList<String>();
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /*public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }*/

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<String> getCols() {
        return cols;
    }

    public void setCols(List<String> cols) {
        this.cols = cols;
    }

    public List<String> getFocus() {
        return focus;
    }

    public void setFocus(List<String> focus) {
        this.focus = focus;
    }

    public int getColsNum() {
        return colsNum;
    }

    public void setColsNum(int colsNum) {
        this.colsNum = colsNum;
    }

    public int getFocusNum() {
        return focusNum;
    }

    public void setFocusNum(int focusNum) {
        this.focusNum = focusNum;
    }

    public boolean isActivityCollected(Note note) {
        for (String s : cols) {
            if (s.equals(note.getId())) {
                return true;
            }
        }
        return false;
    }

    public boolean isActivityFocused(Note note) {
        for (String s: focus) {
            if (s.equals(note.getId())) {
                return true;
            }
        }
        return false;
    }

    public void addCollection(String aid) {
        cols.add(aid);
        colsNum++;
    }

    public void addFocus(String aid) {
        focus.add(aid);
        focusNum++;
    }

    public void cancleCol(String aid) {
        for (String s: cols) {
            if (s.equals(aid)) {
                cols.remove(s);
                colsNum--;
                return;
            }
        }
    }

    public void cancleFocus(String aid) {
        for (String s : focus) {
            if (s.equals(aid)) {
                focus.remove(s);
                focusNum--;
                return;
            }
        }
    }

    public static void save(Context context, User user) {
        //SPUtil.put(context, "major", user.getMajor());
        SPUtil.put(context, "name", user.getName());
        //SPUtil.put(context, "grade", user.getGrade());
        //SPUtil.put(context, "sex", user.getSex());
        SPUtil.put(context, "avatar_url", user.getAvatar());
        SPUtil.put(context, "uid", user.getUid());
        SPUtil.put(context, "colsNum", user.getColsNum());
        SPUtil.put(context, "focusNum", user.getFocusNum());
        for (int i = 0; i < user.getCols().size(); i++) {
            SPUtil.put(context, "col" + i, user.getCols().get(i));
        }
        for (int i = 0; i < user.getFocus().size(); i++) {
            SPUtil.put(context, "focus" + i, user.getFocus().get(i));
        }
    }

    public static User load(Context context) {
        User user = new User();
        user.setName((String) SPUtil.get(context, "name", ""));
        //user.setMajor((String) SPUtil.get(context, "major", ""));
        //user.setGrade((String) SPUtil.get(context, "grade", ""));
        user.setAvatar((String) SPUtil.get(context, "avatar_url", ""));
        //user.setSex((String) SPUtil.get(context, "sex", ""));
        user.setUid((String) SPUtil.get(context, "uid", ""));
        user.setColsNum((Integer) SPUtil.get(context, "colsNum", 0));
        user.setFocusNum((Integer) SPUtil.get(context, "focusNum", 0));
        List<String> cols = new ArrayList<String>();
        List<String> focus = new ArrayList<String>();
        for (int i = 0; i < user.getColsNum(); i++) {
            cols.add((String) SPUtil.get(context, "col" + i, ""));
        }
        user.setCols(cols);
        for (int i = 0; i < user.getFocusNum(); i++) {
            focus.add((String) SPUtil.get(context, "focus" + i, ""));
        }
        user.setFocus(focus);
        return user;
    }

    public static void clear(Context context) {
        SPUtil.remove(context, "name");
        //SPUtil.remove(context, "major");
        //SPUtil.remove(context, "grade");
        //SPUtil.remove(context, "sex");
        SPUtil.remove(context, "avatar_url");
        SPUtil.remove(context, "uid");
        int colsNum = (int) SPUtil.get(context, "colsNum", 0);
        int focusNum = (int) SPUtil.get(context, "focusNum", 0);
        SPUtil.remove(context, "colsNum");
        SPUtil.remove(context, "focusNum");
        for (int i = 0; i < colsNum; i++) {
            SPUtil.remove(context, "col" + i);
        }
        for (int i = 0; i < focusNum; i++) {
            SPUtil.remove(context, "focus" + i);
        }
    }

}
