package com.xyz.lehuo.bean;

import java.io.Serializable;

/**
 * Created by xyz on 15/12/25.
 */
/*public class Note implements Serializable {

    private String id;
    private String title;
    private int readNum;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private String openTime;
    private String sponsor;
    private String organizer;
    private String imgUrl;
    private String detailUrl;
    private int fee;
    private String location;
    private String joinWay;
    private String type;

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getJoinWay() {
        return joinWay;
    }

    public void setJoinWay(String joinWay) {
        this.joinWay = joinWay;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public int getReadNum() {
        return readNum;
    }

    public void setReadNum(int readNum) {
        this.readNum = readNum;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}*/

public class Note implements Serializable {
    private static final long serialVersionUID = 1L;

    private String mDate;
    private String mWeek;
    private String mContent;
    private String mNoteName;
    private String imgUrl;
    private String detailUrl;
    private String nid;

    public String toString() {
        return mNoteName + " Time:" + mDate + " Week:" + mWeek + " Content:" + mContent;
    }

    public Note() { }

    public Note(String date,String week ,String noteName, String content) {
        this.mDate = date;
        this.mWeek = week;
        this.mContent = content;
        this.mNoteName = noteName;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String Date) { this.mDate = Date; }

    public String getWeek(){
        return mWeek;
    }

    public void setWeek(String Week) { this.mWeek = Week; }

    public String getNoteName() {return mNoteName; }

    public void setNoteName(String noteName) {
        this.mNoteName = noteName;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String Content) { this.mContent = Content; }

    public void setId(String nid){this.nid=nid;}

    public String getId(){return nid;}

}


