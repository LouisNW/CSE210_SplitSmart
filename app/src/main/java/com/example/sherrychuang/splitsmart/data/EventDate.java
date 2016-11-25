package com.example.sherrychuang.splitsmart.data;

/**
 * Created by Louis on 11/22/16.
 */

public class EventDate {
    private int month;
    private int day;

    public EventDate(int month, int day) {
        this.month = month;
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "EventDate{" +
                "month=" + month +
                ", day=" + day +
                '}';
    }
}
