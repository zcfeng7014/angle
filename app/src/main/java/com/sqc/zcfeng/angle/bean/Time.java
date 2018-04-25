package com.sqc.zcfeng.angle.bean;

/**
 * Created by Administrator on 2018/4/23.
 */

public class Time {
    int hour;

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    int minute;
}
