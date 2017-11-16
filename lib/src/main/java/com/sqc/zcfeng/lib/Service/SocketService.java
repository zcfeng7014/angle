package com.sqc.zcfeng.lib.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.sqc.zcfeng.lib.Service.Impl.MessageListener;
import com.sqc.zcfeng.lib.Utils.ToastUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public abstract class SocketService extends Service implements Runnable{
    private String ip="0.0.0.0";
    private int port=0;
    private Handler handler=new Handler();
    private long lastSendTime;
    private Socket client;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private MessageListener observer;

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return  new MyBind();
    }

    public void onStart(Intent intent, int startId) {
        new Thread(this).start();
        new Thread(new NetWatchDog()).start();
    }

     void sendMessage(String obj) throws IOException {
        if(bufferedWriter==null) return;
        bufferedWriter.write(getMessage(obj)+"\n");
        bufferedWriter.flush();
        lastSendTime=System.currentTimeMillis();
    }
    void sendBoom() throws IOException {
        if(bufferedWriter==null) return;
        bufferedWriter.write(getBoom()+"\n");
        bufferedWriter.flush();
        lastSendTime=System.currentTimeMillis();
    }

    public abstract String getBoom() ;

    public abstract String getMessage(String obj);

    @Override
    public void run() {
        try {
            client=new Socket(ip,port);
            bufferedWriter=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            bufferedReader=new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (Exception e) {
            resetSocket();
        }
        while (true){
            try {
                String msg=bufferedReader.readLine();
                if(observer!=null)
                    observer.OnMessage(msg);
            } catch (IOException e) {
                resetSocket();
            }
        }
    }

    public class MyBind extends Binder {
        public void sendMessage(final String msg) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        SocketService.this.sendMessage(msg);
                    } catch (IOException e) {

                        resetSocket();
                    }
                }
            }).start();
        }
        public void setObserver(MessageListener ob){
            observer=ob;
        }
    }

    class NetWatchDog implements Runnable {
        int checkDelay = 10;
        long keepAliveDelay = 5000;

        @Override
        public void run() {
            while (true) {
                if (System.currentTimeMillis() - lastSendTime > keepAliveDelay) {
                    try {
                        sendBoom();
                    } catch (IOException e) {
                        resetSocket();
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Thread.sleep(checkDelay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
        public  synchronized void resetSocket(){
            while (isServerClose(client)) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    client = new Socket(ip, port);
                    bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                } catch (UnknownHostException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Host解析错误",Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"正在重连",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"重连成功",Toast.LENGTH_SHORT).show();
                }
            });
        }
        /**
         * 判断是否断开连接，断开返回true,没有返回false
         * @param socket
         * @return
         */
        public  Boolean isServerClose(Socket socket){
            try{
                sendBoom();
                return false;
            }catch(Exception se){
                return true;
            }
        }

}
