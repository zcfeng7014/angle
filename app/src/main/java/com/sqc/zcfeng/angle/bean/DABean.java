package com.sqc.zcfeng.angle.bean;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.blankj.utilcode.util.LogUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017\11\24 0024.
 */

public class DABean implements Serializable{
    private static SQLiteDatabase dbr;
    private static SQLiteDatabase dbw;
    public String id;
    public String name;
    String _class;
    String time;
    String doctorname;
    String doctor_tel;
    String reslut;
    String orders;
    String prescription;
    int isalarm;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static ArrayList<DABean> getList(Context context) {
        if (dbr==null)
            dbr=new MyDB(context).getReadableDatabase();
        Cursor cursor=dbr.rawQuery("select * from casetable ",null);
        ArrayList<DABean> list=new ArrayList<>();
        while(cursor.moveToNext()){

            DABean bean=new DABean();
            bean.setId(cursor.getString(cursor.getColumnIndex("id")));
            bean.setName(cursor.getString(cursor.getColumnIndex("name")));
            bean.set_class(cursor.getString(cursor.getColumnIndex("class")));
            bean.setTime(cursor.getString(cursor.getColumnIndex("time")));
            bean.setDoctorname(cursor.getString(cursor.getColumnIndex("doctorname")));
            bean.setDoctor_tel(cursor.getString(cursor.getColumnIndex("doctor_tel")));
            bean.setOrders(cursor.getString(cursor.getColumnIndex("orders")));
            bean.setReslut(cursor.getString(cursor.getColumnIndex("reslut")));
            bean.setPrescription(cursor.getString(cursor.getColumnIndex("prescription")));
            bean.setIsalarm(cursor.getInt(cursor.getColumnIndex("isalarm")));
            list.add(bean);
            LogUtils.d(bean.getName());
        }
        return list;
    }
    public ArrayList<DABean> save(Context context){
        if (dbw==null)
            dbw=new MyDB(context).getWritableDatabase();
        String sql="replace into casetable(id,name,class,time,doctorname,doctor_tel,orders,reslut,prescription,isalarm) values (?,?,?,?,?,?,?,?,?,?)";
        dbw.execSQL(sql,new String[]{
                getId(),
                getName(),
                get_class(),
                getTime(),
                getDoctorname(),
                getDoctor_tel(),
                getOrders(),
                getReslut(),
                getPrescription(),
                getIsalarm()+""
        });
        return getList(context);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDoctorname() {
        return doctorname;
    }

    public void setDoctorname(String doctorname) {
        this.doctorname = doctorname;
    }

    public String getDoctor_tel() {
        return doctor_tel;
    }

    public void setDoctor_tel(String doctor_tel) {
        this.doctor_tel = doctor_tel;
    }

    public String getReslut() {
        return reslut;
    }

    public void setReslut(String reslut) {
        this.reslut = reslut;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public int getIsalarm() {
        return isalarm;
    }

    public void setIsalarm(int isalarm) {
        this.isalarm = isalarm;
    }



}
