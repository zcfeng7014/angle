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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.sqc.zcfeng.angle.R;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DrugSearchActivity extends AppCompatActivity {

    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.symptom)
    EditText symptom;
    ArrayList<String> list = new ArrayList<>();
    @BindView(R.id.adddrug)
    TextView adddrug;
    ArrayAdapter<String> arrayAdapter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.check)
    Button check;
    App app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_search);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        app= (App) getApplication();
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        lv.setAdapter(arrayAdapter);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                new AlertDialog.Builder(DrugSearchActivity.this).setMessage("确定添加"+list.get(i)+"到列表？").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int index) {
                        list.remove(i);
                        arrayAdapter.notifyDataSetChanged();

                    }
                }).setNegativeButton("取消", null).create().show();
                return true;
            }
        });
    }

    @OnClick(R.id.adddrug)
    public void onViewClicked() {
        startActivityForResult(new Intent(this,SelectDrugActivity.class),PICK_CONTACT_REQUEST);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @OnClick(R.id.check)
    public void check() {
        Intent intent = new Intent(this, DrugActivity.class);
        String _symptom = symptom.getText().toString();
        HashMap<String, Object> objectHashMap = new HashMap<>();
        Gson gson = new Gson();
        objectHashMap.put("drugCommonNames", list.toArray());
        objectHashMap.put("symptom", _symptom);
        String json = gson.toJson(objectHashMap).toString();
        LogUtils.d(json);
        intent.putExtra("json", json);
        startActivity(intent);
    }
    static final int PICK_CONTACT_REQUEST = 0;

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
                list.add(data.getStringExtra("drugName"));
                arrayAdapter.notifyDataSetChanged();
            }
        }
    }
}
