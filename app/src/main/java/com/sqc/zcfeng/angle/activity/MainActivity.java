package com.sqc.zcfeng.angle.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.sqc.zcfeng.angle.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.gv)
    GridView gv;
    String title[]=new String[]{"健康中心","健康数据","电子医嘱","在线医师","天使守护","数据中心"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        gv.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 6;
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
                LinearLayout ll= (LinearLayout) View.inflate(getApplicationContext(), R.layout.item, null);
                ImageView iv = ll.findViewById(R.id.image);
                TextView tv = ll.findViewById(R.id.text);
                tv.setText(title[position]);
                Picasso.with(getApplicationContext()).load(R.drawable._new).into(iv);
                return ll;
            }
        });
    }
}
