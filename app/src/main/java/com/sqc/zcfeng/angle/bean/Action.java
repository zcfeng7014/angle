package com.sqc.zcfeng.angle.bean;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/5/8.
 */

public class Action {
    int id;
    int option;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
    private static SQLiteDatabase dbr;
    private static SQLiteDatabase dbw;
    String key;
    float weight;
    public void add(Context context){
        if (dbw==null)
            dbw=new MyDB(context).getWritableDatabase();
        String sql="insert into action(option,key,weight) values (?,?,?)";

        dbw.execSQL(sql,new String[]{option+"",key,weight+""});
    }
    public static ArrayList<String> getKeys(Context context){
        if (dbr==null)
            dbr=new MyDB(context).getReadableDatabase();
        Cursor cursor=dbr.rawQuery("select `key`, sum(weight) as weight from `action` group by `key`  order by weight limit 10 ",null);
        ArrayList<String> list=new ArrayList<>();
        while (cursor.moveToNext()){
            list.add(cursor.getString(0));
        }
        return list;
    }
}
