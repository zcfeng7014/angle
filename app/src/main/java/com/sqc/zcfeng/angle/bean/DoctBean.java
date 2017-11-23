package com.sqc.zcfeng.angle.bean;

/**
 * Created by Administrator on 2017\11\23 0023.
 */

public class DoctBean {
    String name;

    public DoctBean(String name, String departments, String rank,String intro) {
        this.name = name;
        this.departments = departments;
        this.rank = rank;
        this.intro = intro;
    }

    String departments;
    String rank;
    String intro;
}
