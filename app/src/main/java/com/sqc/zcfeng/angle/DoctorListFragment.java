package com.sqc.zcfeng.angle;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sqc.zcfeng.angle.bean.DoctBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.rong.imkit.RongIM;

public class DoctorListFragment extends Fragment {

    @BindView(R.id.lv)
    PullToRefreshListView lv;
    Unbinder unbinder;
    ArrayList<DoctBean> list=new ArrayList<>();
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAdapter.notifyDataSetChanged();
            lv.onRefreshComplete();
        }
    };
    BaseAdapter mAdapter;;
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
        for (int i=0;i<20;i++)
        {
            list.add(new DoctBean("医生","急诊科","主治医师","暂无"));
        }

        mAdapter=new BaseAdapter(){

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder viewHolder;
                if(convertView==null){
                    convertView=View.inflate(getContext(),R.layout.newitem,null);
                    viewHolder=new ViewHolder(convertView);
                }
                else
                    viewHolder= (ViewHolder) convertView.getTag();
                DoctBean doctBean=list.get(position);
               Picasso.with(getContext()).load(R.drawable.doctor).into( viewHolder.iv);
                viewHolder.infoview.setText(doctBean.name+(position+1)+" "+doctBean.departments + "  "+doctBean.rank);
                viewHolder.introview.setText("简介:"+doctBean.intro);
                return convertView;
            }
            class ViewHolder{
                ImageView iv;
                TextView infoview;
                TextView introview;
                ViewHolder(View ll){
                    iv=ll.findViewById(R.id.pic);
                    infoview=ll.findViewById(R.id.name);
                    introview=ll.findViewById(R.id.intro);
                    ll.setTag(this);
                }
            }
        };
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
                RongIM.getInstance().startPrivateChat(getContext(),"测试医生","测试医生");
            }
        });
    }

    private void pullUpToRefresh() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    for (int i = 0; i < 20; i++) {
                        list.add(new DoctBean("医生" ,"急诊科","主治医师","暂无"));
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
                    for (int i = 0; i < 20; i++) {
                        list.add(new DoctBean("医生","急诊科","主治医师","暂无"));
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
