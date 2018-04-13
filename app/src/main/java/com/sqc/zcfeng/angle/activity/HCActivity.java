package com.sqc.zcfeng.angle.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.sqc.zcfeng.angle.R;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HCActivity extends AppCompatActivity {

    @BindView(R.id.direct)
    ListView direct;
    String[] directs = new String[]{"疾病查询", "饮食鉴定", "合理用药", "血压分析"};
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hc);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("疾病列表");
        direct.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, directs));
        direct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        showAlertDialog0();
                        break;
                    case 2:
                        showAlertDialog2();
                        break;
                    default:
                        ToastUtils.showLong("暂未开放");
                }
            }
        });
    }

    void showAlertDialog0() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText editText = new EditText(this);
        builder.setTitle("请输入你要查询的疾病的名称").setView(editText).setPositiveButton("查询", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(HCActivity.this, IllListActivity.class);
                intent.putExtra("key", editText.getText().toString());
                startActivity(intent);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }

    void showAlertDialog2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LinearLayout linearLayout = (LinearLayout) View.inflate(this, R.layout.form_1, null);
        final EditText names = linearLayout.findViewById(R.id.names);
        final EditText symptom = linearLayout.findViewById(R.id.symptom);
        final EditText age = linearLayout.findViewById(R.id.age);
        final Spinner sex = linearLayout.findViewById(R.id.sex);
        final Spinner ageType = linearLayout.findViewById(R.id.ageType);
        builder.setTitle("请输入药品通用名").setView(linearLayout).setPositiveButton("查询", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(HCActivity.this, IllListActivity.class);
                String[] _name = names.getText().toString().split("\\|");
                String _symptom = symptom.getText().toString();
                String _age = age.getText().toString();
                int _gender = sex.getSelectedItemPosition();
                String _ageType = ((TextView) ageType.getSelectedView()).getText().toString();
                HashMap<String, Object> objectHashMap = new HashMap<>();
                Gson gson = new Gson();
                objectHashMap.put("drugCommonNames", _name);
                objectHashMap.put("gender", _gender);
                objectHashMap.put("symptom", _symptom);
                objectHashMap.put("age", _age);
                objectHashMap.put("ageType", _ageType);
                String json = gson.toJson(objectHashMap).toString();
                LogUtils.d(json);
                intent.putExtra("json", json);
                startActivity(intent);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
