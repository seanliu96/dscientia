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
    private int nbsNum;
    private int colsNum;
    private int focusNum;
    private List<String> cols;
    private List<String> focus;
    private List<String> notebooks;

    public User() {
        cols = new ArrayList<String>();
        focus = new ArrayList<String>();
        notebooks = new ArrayList<String>();
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public int getNbsNum() {
        return nbsNum;
    }

    public void setNbsNum(int nbsNum) {
        this.nbsNum = nbsNum;
    }

    public List<String> getNotebooks() {
        return notebooks;
    }

    public void setNotebooks(List<String> notebooks) {
        this.notebooks = notebooks;
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

    public void addNoteBooks(String noteBook) {
        notebooks.add(noteBook);
        colsNum++;
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
        SPUtil.put(context, "name", user.getName());
        SPUtil.put(context, "avatar_url", user.getAvatar());
        SPUtil.put(context, "uid", user.getUid());
        SPUtil.put(context, "nbsNum", user.getNbsNum());
        SPUtil.put(context, "colsNum", user.getColsNum());
        SPUtil.put(context, "focusNum", user.getFocusNum());
        for (int i = 0; i < user.getCols().size(); i++) {
            SPUtil.put(context, "col" + i, user.getCols().get(i));
        }
        for (int i = 0; i < user.getFocus().size(); i++) {
            SPUtil.put(context, "focus" + i, user.getFocus().get(i));
        }
        for (int i = 0; i < user.getNotebooks().size(); i++) {
            SPUtil.put(context, "noteBooks" + i, user.getNotebooks().get(i));
        }
    }

    public static User load(Context context) {
        User user = new User();
        user.setName((String) SPUtil.get(context, "name", ""));
        user.setAvatar((String) SPUtil.get(context, "avatar_url", ""));
        user.setUid((String) SPUtil.get(context, "uid", ""));
        user.setColsNum((Integer) SPUtil.get(context, "colsNum", 0));
        user.setFocusNum((Integer) SPUtil.get(context, "focusNum", 0));
        user.setFocusNum((Integer) SPUtil.get(context, "nbsNum", 0));
        List<String> cols = new ArrayList<String>();
        List<String> focus = new ArrayList<String>();
        List<String> noteBooks = new ArrayList<String>();
        for (int i = 0; i < user.getColsNum(); i++) {
            cols.add((String) SPUtil.get(context, "col" + i, ""));
        }
        user.setCols(cols);
        for (int i = 0; i < user.getFocusNum(); i++) {
            focus.add((String) SPUtil.get(context, "focus" + i, ""));
        }
        user.setFocus(focus);

        for (int i = 0; i < user.getNotebooks().size(); i++) {
            SPUtil.put(context, "noteBooks" + i, user.getNotebooks().get(i));
        }

        user.setNotebooks(noteBooks);
        return user;
    }

    public static void clear(Context context) {
        SPUtil.remove(context, "name");
        SPUtil.remove(context, "avatar_url");
        SPUtil.remove(context, "uid");
        SPUtil.remove(context, "nbid");
        int colsNum = (int) SPUtil.get(context, "colsNum", 0);
        int focusNum = (int) SPUtil.get(context, "focusNum", 0);
        int nbsNum = (int) SPUtil.get(context, "nbsNum", 0);
        SPUtil.remove(context, "colsNum");
        SPUtil.remove(context, "focusNum");
        SPUtil.remove(context, "nbsNum");
        for (int i = 0; i < colsNum; i++) {
            SPUtil.remove(context, "col" + i);
        }
        for (int i = 0; i < focusNum; i++) {
            SPUtil.remove(context, "focus" + i);
        }
        for (int i = 0; i < nbsNum; i++) {
            SPUtil.remove(context, "noteBooks" + i);
        }
    }

}
