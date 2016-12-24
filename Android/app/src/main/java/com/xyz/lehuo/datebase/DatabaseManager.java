package com.xyz.lehuo.datebase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xyz.lehuo.bean.Note;
import com.xyz.lehuo.bean.Club;
import com.xyz.lehuo.global.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xyz on 16/1/1.
 */
public class DatabaseManager {

    private DatabaseHelper mHelper;
    private SQLiteDatabase mDatabase;

    public DatabaseManager(Context context) {
        mHelper = new DatabaseHelper(context);
        mDatabase = mHelper.getWritableDatabase();
    }

    /*public void addActivity(Note activity) {
        ContentValues cv = new ContentValues();
        cv.put("activityId", activity.getId());
        cv.put("title", activity.getTitle());
        cv.put("readnum", activity.getReadNum());
        cv.put("endDate", activity.getEndDate());
        cv.put("startDate", activity.getStartDate());
        cv.put("endTime", activity.getEndTime());
        cv.put("startTime", activity.getStartTime());
        cv.put("organizer", activity.getOrganizer());
        cv.put("imgUrl", activity.getImgUrl());
        cv.put("detailUrl", activity.getDetailUrl());
        cv.put("type", activity.getType());
        mDatabase.insert(Constant.ACTIVITY_TABLE_NAME, null, cv);
    }*/

    public void addActivity(Note note) {
        ContentValues cv = new ContentValues();
        cv.put("note_id", note.getId());
        cv.put("note_title", note.getNoteName());
        cv.put("note_week", note.getWeek());
        cv.put("note_date", note.getDate());
        cv.put("note_content", note.getContent());
        cv.put("imgUrl", note.getImgUrl());
        cv.put("detailUrl", note.getDetailUrl());
        mDatabase.insert(Constant.NOTE_TABLE_NAME, null, cv);
    }

    public void addActivities(List<Note> activities) {
        mDatabase.beginTransaction();
        try {
            for (Note activity : activities) {
                addActivity(activity);
            }
            mDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mDatabase.endTransaction();
        }
    }

    /*public List<Note> getAllActivities() {
        List<Note> activities = new ArrayList<Note>();
        Cursor c = mDatabase.rawQuery("SELECT * FROM " + Constant.ACTIVITY_TABLE_NAME, null);
        while (c.moveToNext()) {
            Note activity = new Note();
            activity.setId(c.getString(c.getColumnIndex("activityId")));
            activity.setTitle(c.getString(c.getColumnIndex("title")));
            activity.setReadNum(c.getInt(c.getColumnIndex("readnum")));
            activity.setEndDate(c.getString(c.getColumnIndex("endDate")));
            activity.setOrganizer(c.getString(c.getColumnIndex("organizer")));
            activity.setImgUrl(c.getString(c.getColumnIndex("imgUrl")));
            activity.setDetailUrl(c.getString(c.getColumnIndex("detailUrl")));
            activity.setEndTime(c.getString(c.getColumnIndex("endTime")));
            activity.setStartDate(c.getString(c.getColumnIndex("startDate")));
            activity.setStartTime(c.getString(c.getColumnIndex("startTime")));
            activity.setType(c.getString(c.getColumnIndex("type")));
            activities.add(activity);
        }
        c.close();
        return activities;
    }*/

    public List<Note> getAllActivities() {
        List<Note> activities = new ArrayList<Note>();
        Cursor c = mDatabase.rawQuery("SELECT * FROM " + Constant.NOTE_TABLE_NAME, null);
        while (c.moveToNext()) {
            Note activity = new Note();

            activity.setId(c.getString(c.getColumnIndex("note_nid")));
            activity.setNoteName(c.getString(c.getColumnIndex("note_name")));
            activity.setWeek(c.getString(c.getColumnIndex("note_week")));
            activity.setDate(c.getString(c.getColumnIndex("note_date")));
            activity.setContent(c.getString(c.getColumnIndex("note_content")));
            activity.setImgUrl(c.getString(c.getColumnIndex("imgUrl")));
            activity.setDetailUrl(c.getString(c.getColumnIndex("detailUrl")));
            activities.add(activity);
        }
        c.close();
        return activities;
    }

    public void clearActivityTable() {
        mDatabase.execSQL("DELETE FROM " + Constant.NOTE_TABLE_NAME);
    }
}