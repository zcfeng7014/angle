package com.sqc.zcfeng.angle.bean;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.blankj.utilcode.util.LogUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Administrator on 2017\11\24 0024.
 */

public class AlarmBean implements Serializable{
    private static SQLiteDatabase dbr;
    private static SQLiteDatabase dbw;
    public int id;

    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String case_id;
    Time time =new Time(0,0);
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static ArrayList<AlarmBean> getListByCaseId(Context context,String id) {
        if (dbr==null)
            dbr=new MyDB(context).getReadableDatabase();
        String sql="select * from alarmlist where case_id=? ";
        Cursor cursor=dbr.rawQuery(sql,new String[]{""+id,});
        ArrayList<AlarmBean> list=new ArrayList<>();
        while(cursor.moveToNext()){

            AlarmBean bean=new AlarmBean();
            bean.setId(cursor.getInt(cursor.getColumnIndex("id")));
            bean.setTime(new Time(cursor.getInt(cursor.getColumnIndex("hour")),cursor.getInt(cursor.getColumnIndex("minute"))));
            bean.setCase_id(cursor.getString(cursor.getColumnIndex("case_id")));
            list.add(bean);
        }
        return list;
    }
    public  ArrayList<AlarmBean> add(Context context){
        if (dbw==null){
            dbw=new MyDB(context).getWritableDatabase();
        }
        String sql="insert into alarmlist(case_id,hour,minute) values(?,?,?)";
        dbw.execSQL(sql,new String[]{getCase_id()+"",getTime().getHour()+"",getTime().getMinute()+""});
        return getListByCaseId(context,getCase_id());
    }

    public ArrayList<AlarmBean> update(Context applicationContext) {
        if (dbw==null){
            dbw=new MyDB(applicationContext).getWritableDatabase();
        }
        String sql="update alarmlist set case_id=?,hour=?,minute=? where id=?";
        dbw.execSQL(sql,new String[]{getCase_id()+"",getTime().getHour()+"",getTime().getMinute()+"",getId()+""});
        return getListByCaseId(applicationContext,getCase_id());
    }

    public Collection<? extends AlarmBean> remove(Context applicationContext) {
        if (dbw==null){
            dbw=new MyDB(applicationContext).getWritableDatabase();
        }
        String sql="delete from alarmlist where id=?";
        dbw.execSQL(sql,new String[]{getId()+""});
        return getListByCaseId(applicationContext,getCase_id());
    }
}
