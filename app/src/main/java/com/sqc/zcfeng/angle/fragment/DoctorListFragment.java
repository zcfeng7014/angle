package com.sqc.zcfeng.angle.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sqc.zcfeng.angle.R;
import com.sqc.zcfeng.angle.bean.DoctBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.rong.imkit.RongIM;

public class DoctorListFragment extends Fragment {

    @BindView(R.id.lv)
    PullToRefreshExpandableListView lv;
    Unbinder unbinder;
    ArrayList<ArrayList<DoctBean>> list=new ArrayList<>();
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAdapter.notifyDataSetChanged();
            lv.onRefreshComplete();
        }
    };
    String [] title={"急诊科","外科","内科","体检科"};

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
        for (String s:title
                ) {
            list.add(new ArrayList<DoctBean>());
        }
        for (int i=0;i<20;i++)
        {
            list.get(0).add(new DoctBean("医生","急诊科","主治医师","暂无"));
        }

        lv.getRefreshableView().setAdapter(mAdapter);
        lv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ExpandableListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
                pullDownToRefresh();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RongIM.getInstance().startPrivateChat(getContext(),"测试医生","测试医生");
            }
        });
    }
    private void pullDownToRefresh() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    list.clear();
                    for (String s:title
                         ) {
                        list.add(new ArrayList<DoctBean>());
                    }
                    for (int i = 0; i < 20; i++) {
                        list.get(0).add(new DoctBean("医生","急诊科","主治医师","暂无"));
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
    public BaseExpandableListAdapter mAdapter=new BaseExpandableListAdapter() {
        @Override
        public int getGroupCount() {
            return title.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return list.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            LinearLayout ll= (LinearLayout) View.inflate(getContext(),R.layout.groupview_doctor_list_fragment,null);
            TextView tv=ll.findViewById(R.id.text1);
            tv.setText("     "+title[groupPosition]);
            return ll;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView==null){
                convertView=View.inflate(getContext(),R.layout.newitem,null);
                viewHolder=new ViewHolder(convertView);
            }
            else
                viewHolder= (ViewHolder) convertView.getTag();
            DoctBean doctBean=list.get(groupPosition).get(childPosition);
            Picasso.with(getContext()).load(R.drawable.doctor).into( viewHolder.iv);
            viewHolder.infoview.setText(doctBean.name+(childPosition+1)+" "+doctBean.departments + "  "+doctBean.rank);
            viewHolder.introview.setText("简介:"+doctBean.intro);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
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

}
