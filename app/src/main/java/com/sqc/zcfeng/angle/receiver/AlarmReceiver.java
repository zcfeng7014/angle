package com.sqc.zcfeng.angle.receiver;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.blankj.utilcode.util.ToastUtils;
import com.sqc.zcfeng.angle.activity.AlarmActivity;

/**
 * Created by Administrator on 2018/4/24.
 */

public class AlarmReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {


        String info=intent.getStringExtra("info");
        ToastUtils.showLong(info);
        context.startActivity(new Intent(context, AlarmActivity.class));
    }
}
