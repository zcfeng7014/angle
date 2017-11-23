package com.sqc.zcfeng.angle.activity;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2017\11\21 0021.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        RongIM.init(this,"sfci50a7s4u8i");
    }

}
