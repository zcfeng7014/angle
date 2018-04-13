package com.sqc.zcfeng.angle.bean;

/**
 * Created by Administrator on 2018/4/11.
 */

public class Answer {

    /**
     * id : 3
     * choice : 儿童
     */

    private int id;
    private String choice;

    public Answer(int id, String choice) {
        this.id = id;
        this.choice = choice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }
}
