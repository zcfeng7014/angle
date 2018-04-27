package com.sqc.zcfeng.angle.activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

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
    MediaPlayer mediaPlayer;
    Vibrator vibrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        ButterKnife.bind(this);
        Intent intent=getIntent();
        message.setText(intent.getCharSequenceExtra("info"));
        mediaPlayer=MediaPlayer.create(this,RingtoneManager.getActualDefaultRingtoneUri(this,
                RingtoneManager.TYPE_RINGTONE));
        mediaPlayer.setLooping(true);
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();

        vibrator=(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = { 1000, 1400 };
        //震动重复，从数组的0开始（-1表示不重复）
        vibrator.vibrate(pattern, 0);

    }

    @Override
    protected void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
        if (vibrator != null) {
            vibrator.cancel();
        }

        super.onDestroy();
    }
}
