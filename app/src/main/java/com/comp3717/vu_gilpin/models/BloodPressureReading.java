package com.comp3717.vu_gilpin.models;

import com.google.firebase.database.Exclude;

import java.util.Date;

public class BloodPressureReading {
    @Exclude
    private String readingKey;
    private Date readingDate;
    private Integer systolicReading;
    private Integer diastolicReading;
    public String getReadingKey() {
        return readingKey;
    }
    public void setReadingKey(String readingKey) {
        this.readingKey = readingKey;
    }
    public Date getReadingDate() {
        return readingDate;
    }
    public void setReadingDate(Date readingDate) {
        this.readingDate = readingDate;
    }
    public Integer getSystolicReading() {
        return systolicReading;
    }
    public void setSystolicReading(Integer systolicReading) {
        this.systolicReading = systolicReading;
    }
    public Integer getDiastolicReading() {
        return diastolicReading;
    }
    public void setDiastolicReading(Integer diastolicReading) {
        this.diastolicReading = diastolicReading;
    }
    public Condition getCondition() {
        if (systolicReading > 180 || diastolicReading > 120) {
            return Condition.HYPERTENSIVE;
        } else if (systolicReading >= 140 || diastolicReading >= 90) {
            return Condition.STAGE2;
        } else if (systolicReading >= 130 || diastolicReading >= 80) {
            return Condition.STAGE1;
        } else if (systolicReading >= 120) {
            return Condition.ELEVATED;
        } else {
            return Condition.NORMAL;
        }
    }
}
