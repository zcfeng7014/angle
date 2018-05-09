package com.sqc.zcfeng.angle.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sqc.zcfeng.angle.R;
import com.sqc.zcfeng.angle.bean.Action;
import com.sqc.zcfeng.angle.bean.NewBean;
import com.sqc.zcfeng.angle.constans.Constants;
import com.sqc.zcfeng.angle.constans.NewsConfig;
import com.sqc.zcfeng.angle.request.HttpUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Random;

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


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.gv)
    GridView gv;
    String title[] = new String[]{"疾病查询", "个人病例", "智能预检", "合理饮食", "合理用药"};
    MyAdapter myadapter = new MyAdapter();
    int page = 0;
    private ArrayList<NewBean> datalist = new ArrayList<>();
    /**
     * 监听对话框里面的button点击事件
     */
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
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
    @BindView(R.id.lv)
    PullToRefreshListView pullToRefreshListView;
    private int[] logo = {R.drawable.book, R.drawable.manage, R.drawable.test, R.drawable.food, R.drawable.drug};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        datalist.add(new NewBean());
        initgv();

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
                        ArrayList<NewBean> list=new ArrayList<>();
                        Random random=new Random();
                        while (datalist.size()>0){
                            list.remove(random.nextInt(datalist.size()));
                        }
                        list.addAll(datalist);
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

    private void initgv() {
        gv.setAdapter(myadapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        showAlertDialog0();
                        break;
                    case 1:
                        startActivity(new Intent(getApplicationContext(), DoctorAdviceListActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(getApplicationContext(), ClinicActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(getApplicationContext(), DishCheckActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(getApplicationContext(), DrugSearchActivity.class));
                        break;
                }

            }
        });
    }

    class MyAdapter extends BaseAdapter {

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
            LinearLayout ll = (LinearLayout) View.inflate(MainActivity.this, R.layout.item, null);
            ImageView imageView = ll.findViewById(R.id.image);
            TextView textView = ll.findViewById(R.id.text);
            imageView.setImageResource(logo[position]);
            textView.setText(title[position]);
            return ll;
        }
    }

    void showAlertDialog0() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText editText = new EditText(this);
        builder.setTitle("请输入你要查询的疾病的名称").setView(editText).setPositiveButton("查询", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(MainActivity.this, IllListActivity.class);
                intent.putExtra("key", editText.getText().toString());
                startActivity(intent);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog isExit = new AlertDialog.Builder(this).create();
        // 设置对话框标题
        isExit.setTitle("系统提示");
        // 设置对话框消息
        isExit.setMessage("确定要退出吗");
        // 添加选择按钮并注册监听
        isExit.setButton(AlertDialog.BUTTON_POSITIVE, "确定", listener);
        isExit.setButton(AlertDialog.BUTTON_NEGATIVE, "取消", listener);
        // 显示对话框
        isExit.show();
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
                Picasso.with(MainActivity.this).load(R.drawable.sina).fit().into(iv);
            } else
                Picasso.with(MainActivity.this).load(datalist.get(position).getPic()).fit().into(iv);
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
    int time;
    void getdata(String url, final ObservableEmitter<NewBean> e) {
        ArrayList<String> list=Action.getKeys(this);
        ArrayList<String> query_list=new ArrayList<>();
        if(list.size()<=3)
        {
            query_list.addAll(list);
        }
        else
        {
            Random random=new Random();
            for (int i=0;i<3;i++)
                query_list.add(list.remove(random.nextInt(list.size())));
        }

        time=query_list.size()+1;
        App app = (App) getApplication();
        HashMap<String, String> head = new HashMap<>();
        head.put("Authorization", "APPCODE " + Constants.AppCode);

        HttpUtils.doGetOnHead(app.client, url, head, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                LogUtils.d(json);
                if (response.code() == 200) {
                    try {
                        JSONObject object = new JSONObject(json);
                        JSONArray jsonArray = object.getJSONObject("result").getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Gson gson = new Gson();
                            NewBean bean = gson.fromJson(jsonArray.get(i).toString(), NewBean.class);
                            e.onNext(bean);
                        }
                        time--;
                        if(time==0){
                            e.onComplete();
                        }
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

        for (String query : query_list) {
            String queryurl = "";
            try {
                url = NewsConfig.getnewbykey + "?keyword="+ URLEncoder.encode("健康", "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            HttpUtils.doGetOnHead(app.client, url, head, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json = response.body().string();
                    LogUtils.d(json);
                    if (response.code() == 200) {
                        ArrayList<NewBean> list1=new ArrayList<>();
                        try {
                            JSONObject object = new JSONObject(json);
                            JSONArray jsonArray = object.getJSONObject("result").getJSONArray("list");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Gson gson = new Gson();
                                NewBean bean = gson.fromJson(jsonArray.get(i).toString(), NewBean.class);
                                list1.add(bean);
                            }
                            Random random=new Random();
                            for(int i=0;i<5;i++){
                                if(list1.size()>0)
                                e.onNext(list1.remove(random.nextInt(list1.size())));

                            }
                            time--;
                            LogUtils.d(time);
                            if(time==0){
                                e.onComplete();
                            }
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
    }
}