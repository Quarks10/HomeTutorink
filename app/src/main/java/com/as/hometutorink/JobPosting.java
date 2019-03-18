package com.as.hometutorink;

class JobPosting {

    public String postID;
    public String childID;
    public String childName;
    public String eduLevel;
    public String level;
    public String subject;
    public String location;
    public String date;
    public String time;
    public String status;

    public JobPosting(){

    }

    public JobPosting(String postID, String childID, String childName, String eduLevel, String level, String subject, String location, String date, String time, String status) {
        this.postID = postID;
        this.childID = childID;
        this.childName = childName;
        this.eduLevel = eduLevel;
        this.level = level;
        this.subject = subject;
        this.location = location;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getChildID() {
        return childID;
    }

    public void setChildID(String childID) {
        this.childID = childID;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getEduLevel() {
        return eduLevel;
    }

    public void setEduLevel(String eduLevel) {
        this.eduLevel = eduLevel;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
