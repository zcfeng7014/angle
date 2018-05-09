package com.sqc.zcfeng.angle.activity;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017\11\21 0021.
 */

public class App extends Application {
    public OkHttpClient client;

    @Override
    public void onCreate() {
        super.onCreate();
        client=new OkHttpClient();
        Utils.init(this);
    }

}
