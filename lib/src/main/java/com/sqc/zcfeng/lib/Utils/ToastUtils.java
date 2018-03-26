package com.sqc.zcfeng.lib.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Administrator on 2017\10\26 0026.
 */

public class ToastUtils {
    public  interface Action{
        void action();
    }
    public static void show(Context context,String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
    }


}
