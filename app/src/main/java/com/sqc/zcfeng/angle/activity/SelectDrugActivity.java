package com.sqc.zcfeng.angle.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.sqc.zcfeng.angle.R;
import com.sqc.zcfeng.angle.bean.DrugNameBean;
import com.sqc.zcfeng.angle.constans.DrugConfig;
import com.sqc.zcfeng.angle.request.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SelectDrugActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    App app;
    @BindView(R.id.lis)
    ListView lis;
    ArrayList<DrugNameBean> list=new ArrayList();
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            baseAdapter.notifyDataSetChanged();
            super.handleMessage(msg);
        }
    };
    private SearchView searchView;
    private SearchView.SearchAutoComplete et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_drug);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle("");
        app = (App) getApplication();
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lis.setAdapter(baseAdapter);
        lis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                et.setText(list.get(i).getOffical_name());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        //设置搜索栏的默认提示
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint("请输入药品名称"); //默认刚进去就打开搜索栏
        searchView.setIconified(true);
        //设置输入文本的EditText
        et = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        //设置搜索栏的默认提示，作用和setQueryHint相同 et.setHint("输入商品名或首字母"); //设置提示文本的颜色
        et.setHintTextColor(Color.WHITE); //设置输入文本的颜色
        et.setTextColor(Color.WHITE); //设置提交按钮是否可见
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                new AlertDialog.Builder(SelectDrugActivity.this).setMessage("确定添加"+query+"到列表？").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent=getIntent();
                            intent.putExtra("drugName",query);
                        SelectDrugActivity.this.setResult(RESULT_OK, intent);
                        // 结束SelectCityActivity
                        SelectDrugActivity.this.finish();
                    }
                }).setNegativeButton("取消", null).create().show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                HashMap<String, String> map = new HashMap<>();
                map.put("key", newText);
                HttpUtils.Post(app.client, DrugConfig.getNameUrl, map, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        LogUtils.d(e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String json = response.body().string();
                        list.clear();
                        Gson gson=new Gson();
                        LogUtils.d(json);
                        try {
                            JSONArray jsonArray=new JSONArray(json);

                            for (int i=0;i<jsonArray.length();i++){
                                DrugNameBean bean=gson.fromJson(jsonArray.getJSONObject(i).toString(),DrugNameBean.class);
                                list.add(bean);

                            }

                            handler.sendEmptyMessage(1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                return false;
            }
        });
        return true;
    }
    BaseAdapter baseAdapter=new BaseAdapter() {
        @Override
        public int getCount() {
            return list.size();
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
            if(view==null)
                view=View.inflate(getApplicationContext(),android.R.layout.simple_list_item_1,null);
            TextView tv= (TextView) view;
            DrugNameBean bean=list.get(i);
            tv.setText(bean.getOffical_name()+"(常用名："+bean.getNormal_name()+")");
            return tv;
        }
    };
}
