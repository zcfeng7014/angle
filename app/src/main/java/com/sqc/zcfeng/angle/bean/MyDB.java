package com.sqc.zcfeng.angle.bean;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2018/4/22.
 */

public class MyDB extends SQLiteOpenHelper {
    
    public MyDB(Context context) {
        super(context, "mydb.db", null, 1);
    }
    String casetable="create table if not exists casetable(" +
            "id  TEXT PRIMARY KEY NOT NULL,"+
            "name  TEXT,"+
            "class  TEXT,"+
            "time  TEXT,"+
            "doctorname  TEXT,"+
            "doctor_tel  TEXT,"+
            "reslut  TEXT,"+
            "orders  TEXT,"+
            "prescription TEXT," +
            "isalarm int)";
    String alarmlist="create table if not exists alarmlist(" +
            "id  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
            "case_id  TEXT," +
            "hour INTEGER," +
            "minute INTEGER)";
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(casetable);
        sqLiteDatabase.execSQL(alarmlist);
     }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
