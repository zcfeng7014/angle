package com.sqc.zcfeng.angle.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.sqc.zcfeng.angle.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlarmActivity extends AppCompatActivity {

    @BindView(R.id.message)
    TextView message;
    @BindView(R.id.ok)
    Button ok;
    @OnClick(R.id.ok)
    void Ok(){
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        ButterKnife.bind(this);
        Intent intent=getIntent();
        ToastUtils.showLong(intent.getCharSequenceExtra("info"));
        message.setText(intent.getCharSequenceExtra("info"));
        message.setTextColor(Color.BLACK);

    }

    @Override
    protected void onDestroy() {



        super.onDestroy();
    }
}
