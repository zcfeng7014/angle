package com.sqc.zcfeng.lib.Utils;

import com.blankj.utilcode.util.LogUtils;

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by cfeng on 2018/2/26.
 */

public class HttpUtils {
    public static void Post(OkHttpClient client, String url, HashMap<String,String> map, Callback callback){
       FormBody.Builder builder= new FormBody.Builder();
       if(map!=null)
        for (String key:map.keySet()) {
            builder.add(key,map.get(key));
            LogUtils.i(key+"-"+map.get(key));
        }
        RequestBody form=builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(form)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);

    }

    public static void doGetOnHead(OkHttpClient client, String url, HashMap<String,String> head ,Callback callback){
        Request.Builder request_builder = new Request.Builder()
                .url(url);
        if(head!=null)
        for (String key:head.keySet()) {
            request_builder.addHeader(key,head.get(key));
            LogUtils.i(key+"-"+head.get(key));
        }
        Request request=request_builder.build();
        Call call = client.newCall(request);
        call.enqueue(callback);

    }
}
