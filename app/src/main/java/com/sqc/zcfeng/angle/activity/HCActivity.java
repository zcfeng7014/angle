package com.sqc.zcfeng.angle.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.blankj.utilcode.util.ToastUtils;
import com.sqc.zcfeng.angle.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HCActivity extends AppCompatActivity {

    @BindView(R.id.direct)
    ListView direct;
    String[] directs=new String[]{"疾病查询","饮食鉴定", "药品鉴定","血压分析"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hc);
        ButterKnife.bind(this);
        setTitle("疾病列表");
        direct.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,directs));
        direct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                      showAlertDialog0();
                      break;
                      default:
                          ToastUtils.showLong("暂未开放");
                }
            }
        });
    }
    void showAlertDialog0(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        final EditText editText=new EditText(this);
        builder.setTitle("请输入你要查询的疾病的名称").setView(editText).setPositiveButton("查询", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent=new Intent(HCActivity.this,IllListActivity.class);
                intent.putExtra("key",editText.getText().toString());
                startActivity(intent);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }
}
