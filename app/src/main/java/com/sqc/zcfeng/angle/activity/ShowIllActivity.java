package com.sqc.zcfeng.angle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.sqc.zcfeng.angle.R;
import com.sqc.zcfeng.angle.bean.illDetail;
import com.sqc.zcfeng.angle.constans.Constants;
import com.sqc.zcfeng.angle.constans.IllConfig;
import com.sqc.zcfeng.angle.constans.NewsConfig;
import com.sqc.zcfeng.angle.request.HttpUtils;

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

public class ShowIllActivity extends AppCompatActivity {
    WebView wv;
    ArrayList<illDetail> datalist = new ArrayList();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.name)
    TextView name;
    String ill_name;
    @BindView(R.id.typeName)
    TextView typeName;
    String ill_typeName;
    @BindView(R.id.subTypeName)
    TextView subTypeName;
    String ill_subTypeName;
    @BindView(R.id.tag)
    LinearLayout tag;
    @BindView(R.id.summary)
    TextView summary;
    Spanned ill_summary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ill_info);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        getSupportActionBar().setTitle(title);
        String id = intent.getStringExtra("id");
        getdata(id);
//        wv = (WebView) findViewById(R.id.webview);
//        wv.loadDataWithBaseURL(null, "<p>介绍：</p>"+content, "text/html" , "utf-8", null);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    void getdata(final String id) {
        Observable.create(new ObservableOnSubscribe<illDetail>() {

            @Override
            public void subscribe(ObservableEmitter<illDetail> e) throws Exception {
                Request(IllConfig.get_detail + "?id=" + id, e);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Observer<illDetail>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(illDetail value) {
                datalist.add(value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                name.setText(ill_name);
                typeName.setText(ill_typeName);
                subTypeName.setText(ill_subTypeName);
                summary.setText(ill_summary);
                LayoutInflater lif = getLayoutInflater();
                for (illDetail tag_bean : datalist
                        ) {
                    LinearLayout ll = (LinearLayout) lif.inflate(R.layout.ill_tag, null);
                    TextView title = ll.findViewById(R.id.title);
                    TextView content = ll.findViewById(R.id.content);

                    title.setText(tag_bean.getName());
                    content.setText(Html.fromHtml(tag_bean.getContent(), null, null));
                    tag.addView(ll);

                }
            }
        });
    }

    void Request(String url, final ObservableEmitter<illDetail> e) {
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
                if (response.code() == 200) {
                    try {
                        JSONObject object = new JSONObject(json);
                        LogUtils.i(object.toString());
                        JSONObject body = object.getJSONObject("showapi_res_body").getJSONObject("item");
                        ill_name=body.getString("name");
                        ill_typeName=body.getString("typeName");
                        ill_subTypeName=body.getString("subTypeName");
                        ill_summary=Html.fromHtml(body.getString("summary"),null,null);
                        JSONArray jsonArray = body.getJSONArray("tagList");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Gson gson = new Gson();
                            illDetail bean = gson.fromJson(jsonArray.get(i).toString(), illDetail.class);
                            e.onNext(bean);
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
}
