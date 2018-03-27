package com.sqc.zcfeng.angle.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sqc.zcfeng.angle.R;
import com.sqc.zcfeng.angle.bean.Illness;
import com.sqc.zcfeng.angle.constans.IllConfig;
import com.sqc.zcfeng.lib.Utils.HttpUtils;
import com.sqc.zcfeng.lib.Utils.ThreadUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class IllListActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    private ArrayList<Illness> datalist = new ArrayList<>();
    @BindView(R.id.lv)
    PullToRefreshListView pullToRefreshListView;
    int page = 0;
    int allpage=0;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("健康资讯");
        final Intent intent=getIntent();
        key=intent.getStringExtra("key");
        datalist.add(new Illness());
        pullToRefreshListView.setAdapter(ba);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                //设置下拉时显示的日期和时间
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                // 更新显示的label
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                // 开始执行异步任务，传入适配器来进行数据改变
                Observable.create(new ObservableOnSubscribe<Illness>() {

                    @Override
                    public void subscribe(ObservableEmitter<Illness> o) throws Exception {
                        update_data(o);
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Observer<Illness>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        datalist.clear();
                    }

                    @Override
                    public void onNext(Illness value) {
                        datalist.add(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        finish();
                    }

                    @Override
                    public void onComplete() {
                        ba.notifyDataSetChanged();
                        pullToRefreshListView.onRefreshComplete();
                    }
                });
            }
        });
        // 添加滑动到底部的监听器
        pullToRefreshListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {
                Observable.create(new ObservableOnSubscribe<Illness>() {

                    @Override
                    public void subscribe(ObservableEmitter<Illness> e) throws Exception {
                        loadmore(e);
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Observer<Illness>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Illness value) {
                        datalist.add(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                            finish();
                    }

                    @Override
                    public void onComplete() {
                        ba.notifyDataSetChanged();
                    }
                });
            }
        });
        pullToRefreshListView.setRefreshing(true);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent1=new Intent(IllListActivity.this,ShowIllActivity.class);
                    Illness illness=datalist.get(position-1);
                    intent1.putExtra("title",illness.getName());
                    intent1.putExtra("id",illness.getId());
                    startActivity(intent1);
            }
        });
    }

    BaseAdapter ba = new BaseAdapter() {
        @Override
        public int getCount() {
            return datalist.size();
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
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.ill_list_item, null);
            }
            TextView name=convertView.findViewById(R.id.name);
            name.setText(datalist.get(position).getName());
            TextView typename=convertView.findViewById(R.id.typeName);
            typename.setText(datalist.get(position).getTypeName());
            TextView subTypeName=convertView.findViewById(R.id.subTypeName);
            subTypeName.setText(datalist.get(position).getSubTypeName());
            return convertView;
        }
    };

    public void loadmore(final ObservableEmitter<Illness> e) {
        page++;
        if (page>allpage){
            ToastUtils.showLong("已到最后一条记录");
            return;
        }
        String url = IllConfig.search_by_key + "?key=" +key+ "&page=" +  page;
        getdata(url, e);

    }

    public void update_data(final ObservableEmitter<Illness> e) {
        page=1;
        String url = IllConfig.search_by_key + "?key=" +key+ "&page=" +  1;
        getdata(url, e);


    }

    void getdata(String url, final ObservableEmitter<Illness> e) {
        App app = (App) getApplication();
        HashMap<String, String> head = new HashMap<>();
        head.put("Authorization", "APPCODE " + IllConfig.AppCode);
        HttpUtils.doGetOnHead(app.client, url, head, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                if (response.code() == 200) {
                    try {
                        JSONObject object = new JSONObject(json);
                        JSONObject body = object.getJSONObject("showapi_res_body").getJSONObject("pagebean");
                        allpage=body.getInt("allPages");
                        int count =body.getInt("allNum");
                        if(count<=0){
                            ThreadUtils.RunInUI(new Runnable() {
                                @Override
                                public void run() {
                                    confirm("没有查到该病种");
                                }
                            });
                        }
                        JSONArray jsonArray=body.getJSONArray("contentlist");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Gson gson = new Gson();
                            Illness bean = gson.fromJson(jsonArray.get(i).toString(), Illness.class);
                            e.onNext(bean);
                        }
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        e.onComplete();

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                        e.onComplete();
                        ThreadUtils.RunInUI(new Runnable() {
                            @Override
                            public void run() {
                                confirm("数据异常");
                            }
                        });
                    }
                } else {
                    e.onComplete();
                    ThreadUtils.RunInUI(new Runnable() {
                        @Override
                        public void run() {
                            confirm("数据获取异常");
                        }
                    });
                }
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
    public  void confirm( final String content ){

                AlertDialog.Builder builder=new AlertDialog.Builder(IllListActivity.this);
                builder.setTitle("提示").setMessage(content).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       finish();
                    }
                }).show();
    }


}
