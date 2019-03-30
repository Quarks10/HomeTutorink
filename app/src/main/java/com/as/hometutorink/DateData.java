package com.as.hometutorink;

import java.util.Date;

class DateData {

    Date date;
    String sdate;
    String day;

    public DateData() {

    }

    public DateData(Date date, String sdate, String day) {
        this.date = date;
        this.sdate = sdate;
        this.day = day;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSdate() {
        return sdate;
    }

    public void setSdate(String sdate) {
        this.sdate = sdate;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
