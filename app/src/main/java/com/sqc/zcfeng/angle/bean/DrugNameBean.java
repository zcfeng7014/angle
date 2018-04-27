package com.sqc.zcfeng.angle.bean;

/**
 * Created by Administrator on 2018/4/26.
 */

public class DrugNameBean {


    /**
     * id : 4
     * normal_name : 肝安
     * offical_name : 15AA
     * created_at : null
     * updated_at : null
     */

    private int id;
    private String normal_name;
    private String offical_name;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNormal_name() {
        return normal_name;
    }

    public void setNormal_name(String normal_name) {
        this.normal_name = normal_name;
    }

    public String getOffical_name() {
        return offical_name;
    }

    public void setOffical_name(String offical_name) {
        this.offical_name = offical_name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
