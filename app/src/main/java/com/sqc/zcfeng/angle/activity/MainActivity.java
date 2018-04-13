package com.sqc.zcfeng.angle.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sqc.zcfeng.angle.R;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.gv)
    GridView gv;
    String title[] = new String[]{"健康助手","健康日报","健康数据库", "自助问诊机",};
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
    private int[] logo={R.drawable._new,R.drawable._new,R.drawable._new,R.drawable._new};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        gv.setAdapter(myadapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        startActivity(new Intent(getApplicationContext(),HCActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(getApplicationContext(),NewsActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(getApplicationContext(),DoctorAdviceListActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(getApplicationContext(),ClinicActivity.class));
                        break;
                }

            }
        });

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
            imageView.setImageResource(logo[position]);
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