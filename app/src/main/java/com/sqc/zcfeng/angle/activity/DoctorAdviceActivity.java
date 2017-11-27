package com.sqc.zcfeng.angle.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sqc.zcfeng.angle.R;
import com.sqc.zcfeng.angle.bean.DABean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;

public class DoctorAdviceActivity extends AppCompatActivity {
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAdapter.notifyDataSetChanged();
            lv.onRefreshComplete();
        }
    };
    ArrayAdapter mAdapter;
    @BindView(R.id.lv)
    PullToRefreshListView lv;
    ArrayList<String> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_advice);
        ButterKnife.bind(this);
        setTitle("医嘱列表");
        for (int i=0;i<20;i++){
            list.add(" 测试医生"+(list.size()+1)+"  2017-1-1 14:30:00");
        }
        mAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        lv.setAdapter(mAdapter);
        lv.setMode(PullToRefreshBase.Mode.BOTH);
        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                pullDownToRefresh();
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                pullUpToRefresh();
            }

        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RongIM.getInstance().startPrivateChat(getApplicationContext(),"测试医生","测试医生");
            }
        });
    }

    private void pullUpToRefresh() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i=0;i<20;i++){
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    list.add(" 测试医生"+(list.size()+1)+"  2017-1-1 14:30:00");
                }
                mHandler.sendEmptyMessage(0);
            }
        }).start();


    }

    private void pullDownToRefresh() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                list.clear();
                for (int i=0;i<20;i++){
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    list.add(" 测试医生"+(list.size()+1)+"  2017-1-1 14:30:00");
                }
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }
}
