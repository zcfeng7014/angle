package com.sqc.zcfeng.angle;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DoctorListFragment extends Fragment {

    @BindView(R.id.lv)
    PullToRefreshListView lv;
    Unbinder unbinder;
    ArrayList<String> list=new ArrayList<>();
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAdapter.notifyDataSetChanged();
            lv.onRefreshComplete();
        }
    };
    ArrayAdapter<String> mAdapter;;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        mAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list);
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

    }

    private void pullUpToRefresh() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Random random=new Random();
                try {
                    Thread.sleep(1000);
                    for (int i = 0; i < 5; i++) {
                        list.add("上拉加载后的数据" + random.nextInt()*1000 + "\t号");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    private void pullDownToRefresh() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    list.clear();
                    for (int i = 0; i < 5; i++) {
                        list.add("下拉刷新后的数据" + i + "\t号");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
