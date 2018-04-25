package com.sqc.zcfeng.angle.activity;


import android.app.AlarmManager;
import android.app.PendingIntent;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.blankj.utilcode.util.LogUtils;
import com.sqc.zcfeng.angle.R;
import com.sqc.zcfeng.angle.bean.AlarmBean;
import com.sqc.zcfeng.angle.bean.DABean;
import com.sqc.zcfeng.angle.bean.Time;
import com.sqc.zcfeng.angle.receiver.AlarmReceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

import static android.app.AlarmManager.INTERVAL_DAY;
import static android.app.AlarmManager.INTERVAL_HALF_DAY;

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
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.addtime)
    TextView addtime;
    @BindView(R.id.isalarm)
    Switch isalarm;
    private DABean info;
    @OnClick(R.id.time)
    void changetime(){
;       AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final DatePicker timePicker = new DatePicker(this);
        builder.setView(timePicker).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               time.setText(timePicker.getYear()+"-"+timePicker.getMonth()+"-"+timePicker.getDayOfMonth());
            }
        }).create().show();
    }
    @OnClick(R.id.addtime)
    void addtime() {
        getTime(new TimeSelect() {

            @Override
            public void timeselected(AlarmBean bean) {
                bean.setCase_id(info.getId());
                bean.add(getApplicationContext());
                time_list.add(bean);
                timeadapter.notifyDataSetChanged();
            }
        }, new AlarmBean());

    }

    @OnClick(R.id.btn)
    void save() {
        info.set_class(departments.getText().toString());
        info.setIsalarm(isalarm.isChecked()?1:0);
        info.setName(name.getText().toString());
        info.setTime(time.getText().toString());
        info.setPrescription(recipel.getText().toString());
        info.setOrders(advice.getText().toString());
        info.setReslut(result.getText().toString());
        info.setDoctorname(doctorName.getText().toString());
        info.setDoctor_tel(tel.getText().toString());
        info.save(this);
        if(isalarm.isChecked()){
            createAlarm();
        }
        else
            cancelAlarm();
        finish();
    }

    private void createAlarm() {

        AlarmManager am=(AlarmManager)getSystemService(ALARM_SERVICE);
        for (AlarmBean bean :time_list) {
            Intent intent = new Intent(AdviceInfoActivity.this, AlarmReceiver.class);
            intent.putExtra("info",info.getPrescription());
            Calendar calendar=Calendar.getInstance();
            calendar.set(Calendar.HOUR,bean.getTime().getHour());
            calendar.set(Calendar.MINUTE,bean.getTime().getMinute());
            Date date = new Date(calendar.getTimeInMillis());

            java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            LogUtils.d(format.format(date));
            PendingIntent pi = PendingIntent.getBroadcast(AdviceInfoActivity.this, bean.getId(), intent, 0);
            am.setRepeating(AlarmManager.RTC_WAKEUP,INTERVAL_DAY, calendar.getTimeInMillis(), pi);
        }
    }
    private void cancelAlarm() {

        AlarmManager am=(AlarmManager)getSystemService(ALARM_SERVICE);
        for (AlarmBean bean :time_list) {
            Intent intent = new Intent(AdviceInfoActivity.this, AlarmReceiver.class);

            Calendar calendar=Calendar.getInstance();
            calendar.set(Calendar.HOUR,bean.getTime().getHour());
            calendar.set(Calendar.MINUTE,bean.getTime().getMinute());
            PendingIntent pi = PendingIntent.getBroadcast(AdviceInfoActivity.this, bean.getId(), intent, 0);
            am.cancel( pi);
        }
    }
    private void cancelAlarm(AlarmBean bean) {

        AlarmManager am=(AlarmManager)getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(AdviceInfoActivity.this, AlarmReceiver.class);
            Calendar calendar=Calendar.getInstance();
            calendar.set(Calendar.HOUR,bean.getTime().getHour());
            calendar.set(Calendar.MINUTE,bean.getTime().getMinute());
            PendingIntent pi = PendingIntent.getBroadcast(AdviceInfoActivity.this, bean.getId(), intent, 0);
            am.cancel( pi);
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
        info = (DABean) intent.getSerializableExtra("bean");
        if (info == null) {
            info = new DABean();
            info.setId(UUID.randomUUID().toString());
        }
        initdate(info);
        iniview();
    }

    ArrayList<String> option_list = new ArrayList<>();
    ArrayList<AlarmBean> time_list = new ArrayList<>();

    private void iniview() {
        for (int i = 1; i <= 7; i++) {
            option_list.add("" + i);
        }
        lv.setAdapter(timeadapter);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                delete(i);
                return true;
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                getTime(new TimeSelect() {

                    @Override
                    public void timeselected(AlarmBean bean) {


                        time_list.clear();
                        time_list.addAll(bean.update(getApplicationContext()));
                        timeadapter.notifyDataSetChanged();
                    }
                }, time_list.get(i));
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

    private void delete(final int index) {
        new AlertDialog.Builder(this).setMessage("确认删除？").setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AlarmBean bean = time_list.remove(index);
                cancelAlarm(bean);
                time_list.clear();
                time_list.addAll(bean.remove(getApplicationContext()));
                timeadapter.notifyDataSetChanged();
            }
        }).create().show();
    }

    private void getTime(final TimeSelect timeSelect, final AlarmBean alarmTime) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final TimePicker timePicker = new TimePicker(this);
        int hour = 0;
        final Time temp = new Time(0, 0);
        temp.setHour(alarmTime.getTime().getHour());
        temp.setMinute(alarmTime.getTime().getMinute());
        timePicker.setIs24HourView(true);
        timePicker.setHour(temp.getHour());
        timePicker.setMinute(temp.getMinute());
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
                alarmTime.setTime(temp);
                timeSelect.timeselected(alarmTime);

            }
        }).create().show();
    }

    private void initdate(DABean info) {
        time_list.clear();
        time_list.addAll(AlarmBean.getListByCaseId(this, info.getId()));
        name.setText(info.getName());
        result.setText(info.getReslut());
        time.setText(info.getTime());
        doctorName.setText(info.getDoctorname());
        tel.setText(info.getDoctor_tel());
        departments.setText(info.get_class());
        advice.setText(info.getOrders());
        recipel.setText(info.getPrescription());
        isalarm.setChecked(info.getIsalarm()==1);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    BaseAdapter timeadapter = new BaseAdapter() {
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
            tv.setText(String.format("%02d", time_list.get(i).getTime().getHour()) + ":" + String.format("%02d", time_list.get(i).getTime().getMinute()));
            return view;
        }
    };

    interface TimeSelect {
        void timeselected(AlarmBean bean);
    }
}
