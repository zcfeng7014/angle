package com.sqc.zcfeng.angle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sqc.zcfeng.angle.R;
import com.sqc.zcfeng.angle.bean.NewBean;
import com.sqc.zcfeng.angle.constans.NewsConfig;
import com.sqc.zcfeng.lib.Utils.HttpUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

public class NewsActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    private ArrayList<NewBean> datalist = new ArrayList<>();
    @BindView(R.id.lv)
    PullToRefreshListView pullToRefreshListView;
    int page = 0;

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
        datalist.add(new NewBean());
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
                Observable.create(new ObservableOnSubscribe<NewBean>() {

                    @Override
                    public void subscribe(ObservableEmitter<NewBean> o) throws Exception {
                        update_data(o);
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Observer<NewBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        datalist.clear();
                    }

                    @Override
                    public void onNext(NewBean value) {
                        datalist.add(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(e.getMessage());
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
                Observable.create(new ObservableOnSubscribe<NewBean>() {

                    @Override
                    public void subscribe(ObservableEmitter<NewBean> e) throws Exception {
                        loadmore(e);
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Observer<NewBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(NewBean value) {
                        datalist.add(value);
                    }

                    @Override
                    public void onError(Throwable e) {

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
                Intent intent = new Intent(getApplicationContext(), ShowNewActivity.class);
                intent.putExtra("url", datalist.get(position - 1).getUrl());
                intent.putExtra("title", datalist.get(position - 1).getTitle());
                startActivity(intent);
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
                convertView = getLayoutInflater().inflate(R.layout.newitem, null);
            }
            TextView tv = (TextView) convertView.findViewById(R.id.name);
            TextView bv = (TextView) convertView.findViewById(R.id.intro);
            ImageView iv = (ImageView) convertView.findViewById(R.id.pic);
            tv.setText(datalist.get(position).getTitle());
            bv.setText(datalist.get(position).getTime() + "  " + datalist.get(position).getSrc());
            if (datalist.get(position).getPic().isEmpty()) {
                Picasso.with(NewsActivity.this).load(R.drawable.sina).fit().into(iv);
            } else
                Picasso.with(NewsActivity.this).load(datalist.get(position).getPic()).fit().into(iv);
            return convertView;
        }
    };

    public void loadmore(final ObservableEmitter<NewBean> e) {
        page++;
        String url = "";
        try {
            url = NewsConfig.getnew + "?channel=" + URLEncoder.encode("健康", "utf-8") + "&num=" + 20 + "&start=" + 20 * page;
            getdata(url, e);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
    }

    public void update_data(final ObservableEmitter<NewBean> e) {
        String url = "";
        try {
            url = NewsConfig.getnew + "?channel=" + URLEncoder.encode("健康", "utf-8") + "&num=" + 20 + "&start=" + 0;
            getdata(url, e);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }


    }

    void getdata(String url, final ObservableEmitter<NewBean> e) {
        App app = (App) getApplication();
        HashMap<String, String> head = new HashMap<>();
        head.put("Authorization", "APPCODE " + NewsConfig.AppCode);
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
                        JSONArray jsonArray = object.getJSONObject("result").getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Gson gson = new Gson();
                            NewBean bean = gson.fromJson(jsonArray.get(i).toString(), NewBean.class);
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
                        ToastUtils.showLong("数据获取异常");
                        finish();
                    }
                } else {
                    ToastUtils.showLong("数据获取失败");
                    e.onComplete();
                    finish();
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
