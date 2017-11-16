package com.sqc.zcfeng.lib.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017\10\26 0026.
 */

public class ToastUtils {
    public static void show(Context context,String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
    }
}
