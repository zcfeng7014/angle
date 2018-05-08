package com.sqc.zcfeng.angle.activity;

import android.content.DialogInterface;
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

import com.sqc.zcfeng.angle.R;
import com.sqc.zcfeng.angle.constans.DishConfig;
import com.sqc.zcfeng.angle.request.HttpUtils;
import com.sqc.zcfeng.lib.Utils.ThreadUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DishCheckActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.adddrug)
    TextView adddrug;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.check)
    Button check;
    App app;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> list2 = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    ArrayAdapter arrayAdapter2;
    @BindView(R.id.res_list)
    ListView resList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_check);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle("合理饮食");
        app = (App) getApplication();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lv.setAdapter(arrayAdapter);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                new AlertDialog.Builder(DishCheckActivity.this).setMessage("确定删除" + list.get(i) + "？").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int index) {
                        list.remove(i);
                        arrayAdapter.notifyDataSetChanged();

                    }
                }).setNegativeButton("取消", null).create().show();
                return true;
            }
        });
        arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list2);
        resList.setAdapter(arrayAdapter2);

    }

    @OnClick(R.id.adddrug)
    public void onViewClicked() {
        final EditText editText = new EditText(this);
        new AlertDialog.Builder(DishCheckActivity.this).setTitle("食材名称").setView(editText).setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int index) {
                list.add(editText.getText().toString());
                arrayAdapter.notifyDataSetChanged();

            }
        }).setNegativeButton("取消", null).create().show();
    }

    @OnClick(R.id.check)
    public void Check() {
        JSONArray jsonArray = new JSONArray();

        for (String str : list
                ) {
            jsonArray.put(str);
        }
        HashMap<String, String> form = new HashMap<>();
        form.put("key", jsonArray.toString());
        HttpUtils.Post(app.client, DishConfig.check, form, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                list2.clear();
                try {
                    JSONArray array=new JSONArray(json);
                    for (int i=0;i<array.length();i++){
                        JSONObject object=array.getJSONObject(i);
                        list2.add(object.get("material_1")+"和"+object.get("material_2")+ "不建议一起食用 \n原因"+object.get("reason"));
                    }
                    ThreadUtils.RunInUI(new Runnable() {
                        @Override
                        public void run() {
                            arrayAdapter2.notifyDataSetChanged();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}
