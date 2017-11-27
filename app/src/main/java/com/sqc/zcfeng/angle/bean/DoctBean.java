package com.sqc.zcfeng.angle.bean;

/**
 * Created by Administrator on 2017\11\23 0023.
 */

public class DoctBean {
    public String name;

    public DoctBean(String name, String departments, String rank,String intro) {
        this.name = name;
        this.departments = departments;
        this.rank = rank;
        this.intro = intro;
    }

    public String departments;
    public String rank;
    public String intro;
}
