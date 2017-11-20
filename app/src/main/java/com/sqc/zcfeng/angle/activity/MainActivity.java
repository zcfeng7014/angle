package com.sqc.zcfeng.angle.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.sqc.zcfeng.angle.R;
import com.sqc.zcfeng.angle.constans.Constants;
import com.squareup.picasso.Picasso;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.gv)
    GridView gv;
    String title[] = new String[]{"健康中心", "健康数据", "电子医嘱", "在线医师", "天使守护", "数据中心"};
    MyAdapter myadapter=new MyAdapter();
    /**监听对话框里面的button点击事件*/
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    finish();
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!SPUtils.getInstance().getBoolean(Constants.ISLOGGED)){
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }
        ButterKnife.bind(this);
        gv.setAdapter(myadapter);
    }
    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return title.length;
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
            LinearLayout ll= (LinearLayout) View.inflate(MainActivity.this,R.layout.item,null);
            ImageView imageView=ll.findViewById(R.id.image);
            TextView textView=ll.findViewById(R.id.text);
            imageView.setImageResource(R.drawable._new);
            textView.setText(title[position]);
            return ll;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog isExit = new AlertDialog.Builder(this).create();
        // 设置对话框标题
        isExit.setTitle("系统提示");
        // 设置对话框消息
        isExit.setMessage("确定要退出吗");
        // 添加选择按钮并注册监听
        isExit.setButton(AlertDialog.BUTTON_POSITIVE,"确定", listener);
        isExit.setButton(AlertDialog.BUTTON_NEGATIVE,"取消", listener);
        // 显示对话框
        isExit.show();
    }
}
