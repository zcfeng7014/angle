package com.sqc.zcfeng.angle.bean;

/**
 * Created by Administrator on 2017\11\24 0024.
 */

public class DABean {
    public int id;

    public DABean(int id, String dateTime, String doctorName) {
        this.id = id;
        DateTime = dateTime;
        DoctorName = doctorName;
    }

    public String DateTime;
    public String DoctorName;
}
