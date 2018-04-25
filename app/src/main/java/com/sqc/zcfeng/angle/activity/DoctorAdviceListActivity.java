package com.sqc.zcfeng.angle.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sqc.zcfeng.angle.R;
import com.sqc.zcfeng.angle.bean.DABean;
import com.sqc.zcfeng.angle.bean.MyDB;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DoctorAdviceListActivity extends AppCompatActivity {
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAdapter.notifyDataSetChanged();
            lv.onRefreshComplete();
        }
    };
    BaseAdapter mAdapter;
    @BindView(R.id.lv)
    PullToRefreshListView lv;
    ArrayList<DABean> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new MyDB(this).getReadableDatabase();
        setContentView(R.layout.activity_doctor_advice);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       getdate();
        mAdapter=new MyAdapter();
        lv.setAdapter(mAdapter);
        lv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                getdate();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //
                Intent intent=new Intent(DoctorAdviceListActivity.this,AdviceInfoActivity.class);
                LogUtils.d(position);
                intent.putExtra("bean",list.get(position-1));
                startActivity(intent);
            }
        });
    }

    private void getdate() {
        list.clear();
        list.addAll(DABean.getList(this));
        LogUtils.d(list.size());
        mHandler.sendEmptyMessage(0);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view==null){
                view=View.inflate(getApplicationContext(),R.layout.dalist_item,null );
            }
            LinearLayout linearLayout= (LinearLayout) view;
            TextView name=linearLayout.findViewById(R.id.name);
            TextView _class=linearLayout.findViewById(R.id.classname);
            name.setTextColor(Color.BLACK);
            name.setText(list.get(i).getName());
            _class.setText("时间："+list.get(i).getTime());
            _class.setTextColor(Color.BLACK);
            return linearLayout;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== android.R.id.home)
            finish();
        if(item.getTitle().equals("新增")){
            startActivity(new Intent(this,AdviceInfoActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("新增");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        getdate();
        super.onResume();
    }
}
