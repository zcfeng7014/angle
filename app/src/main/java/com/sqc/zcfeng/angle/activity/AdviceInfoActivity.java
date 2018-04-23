package com.sqc.zcfeng.angle.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.blankj.utilcode.util.LogUtils;
import com.sqc.zcfeng.angle.R;
import com.sqc.zcfeng.angle.bean.DABean;
import com.sqc.zcfeng.angle.bean.Time;

import java.util.ArrayList;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdviceInfoActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.btn)
    Button btn;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.departments)
    EditText departments;
    @BindView(R.id.time)
    EditText time;
    @BindView(R.id.doctor_name)
    EditText doctorName;
    @BindView(R.id.tel)
    EditText tel;
    @BindView(R.id.result)
    EditText result;
    @BindView(R.id.advice)
    EditText advice;
    @BindView(R.id.recipel)
    EditText recipel;
    @BindView(R.id.rate)
    Spinner rate;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @OnClick(R.id.addtime)
    void addtime(){
        getTime(new TimeSelect(){

            @Override
            public void timeselected(Time time) {
                time_list.add(time);
                timeadapter.notifyDataSetChanged();
            }
        },new Time());

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adviceinfo);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        DABean info = (DABean) intent.getSerializableExtra("info");
        if (info == null) {
            setTitle("病例详情");
        } else
            initdate(info);
        iniview();
    }

    ArrayList<String> option_list = new ArrayList<>();
    ArrayList<Time> time_list = new ArrayList<>();

    private void iniview() {
        for (int i = 1; i <= 7; i++){
            option_list.add("" + i);
        }

        rate.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, option_list));
        lv.setAdapter(timeadapter);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                time_list.remove(i);
                return false;
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                     getTime(new TimeSelect(){

                         @Override
                         public void timeselected(Time time) {
                             time_list.set(i,time);
                             timeadapter.notifyDataSetChanged();
                         }
                     },time_list.get(i));
            }
        });
        lv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    private void getTime(final TimeSelect timeSelect, final Time alarmTime) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        final TimePicker timePicker=new TimePicker(this);
        int hour=0;
        final Time temp=new Time();
        temp.setHour(alarmTime.getHour());
        temp.setMinute(alarmTime.getMinute());
        timePicker.setHour(temp.getHour());
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                temp.setHour(i);
                temp.setMinute(i1);
            }
        });
        builder.setView(timePicker).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                timeSelect.timeselected(temp);
            }
        }).create().show();
    }

    private void initdate(DABean info) {
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

BaseAdapter timeadapter=new BaseAdapter() {
        @Override
        public int getCount() {
            return time_list.size();
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
            if (view == null)
                view = View.inflate(AdviceInfoActivity.this, android.R.layout.simple_list_item_1, null);
            TextView tv = (TextView) view;
            tv.setText(String.format("%02d",time_list.get(i).getHour())+":"+String.format("%02d",time_list.get(i).getMinute()));
            return view;
        }
    };
    interface  TimeSelect{
       void timeselected(Time time);
    }
}
