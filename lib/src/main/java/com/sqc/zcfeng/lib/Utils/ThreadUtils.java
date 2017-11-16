package com.sqc.zcfeng.lib.Utils;

import android.os.Handler;

/**
 * Created by Administrator on 2017\10\26 0026.
 */

public class ThreadUtils {
    public static Handler handler=new Handler();
    public static void RunInUI(Runnable runnable){
        handler.post(runnable);
    }
    public static Thread RunInIO(Runnable runnable){
        Thread thread=new Thread(runnable);
        thread.start();
       return thread;
    }
}
