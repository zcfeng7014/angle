package com.sqc.zcfeng.angle.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.sqc.zcfeng.angle.R;
import com.sqc.zcfeng.angle.bean.DrugResult;
import com.sqc.zcfeng.angle.constans.DrugConfig;
import com.sqc.zcfeng.angle.request.HttpUtils;
import com.sqc.zcfeng.lib.Utils.ThreadUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DrugActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.interactionList)
    LinearLayout interactionList;
    App app;
    @BindView(R.id.safeStatus)
    TextView safeStatus;
    String[] grade = {"禁用", "不适用", "不推荐", "不宜应用", "忌用", "避免使用", "不建议", "慎用", "权衡利弊",};
    String[] color={"#FF0000","#FFB5C5","#FFCC66","#CCFFFF","#CC0000","#00CCFF","#FFCCCC","#0099FF","#CFCFCF"};

    @BindView(R.id.tabooList)
    LinearLayout tabooList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grug);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        final String json = intent.getStringExtra("json");
        app = (App) getApplication();
        HttpUtils.doPost(app.client, DrugConfig.url, json, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String response_json = response.body().string();
                LogUtils.d(response_json);
                Gson gson = new Gson();
                try {
                    JSONObject jsonObject = new JSONObject(response_json);
                    final DrugResult result = gson.fromJson(jsonObject.get("body").toString(), DrugResult.class);
                    ThreadUtils.RunInUI(new Runnable() {
                        @Override
                        public void run() {
                            updateView(result);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    private void updateView(DrugResult result) {
        switch (result.getSafeStatus()) {
            case 1:
                safeStatus.setText("危险");
                safeStatus.setTextColor(Color.RED);
                break;
            case 2:
                safeStatus.setText("慎用");
                safeStatus.setTextColor(Color.parseColor("#FFA500"));
                break;
            case 3:
                safeStatus.setText("安全");
                safeStatus.setTextColor(Color.parseColor("#66CD00"));
        }
        List<DrugResult.Interaction> Interactionlist = result.getInteractionList();
        if (Interactionlist.size() > 0) {
            interactionList.removeAllViews();
        }
        for (int i = 0; i < Interactionlist.size(); i++) {
            DrugResult.Interaction interactionbeam = Interactionlist.get(i);
            LinearLayout ll = (LinearLayout) View.inflate(this, R.layout.interaction_item, null);
            interactionList.addView(ll);
            TextView _names = ll.findViewById(R.id.names);
            List<DrugResult.TabooListBean.DrugBean> rblist = interactionbeam.getDrugList();
            for (DrugResult.TabooListBean.DrugBean db : rblist
                    ) {
                _names.append(" " + db.getDrugCommonName());
            }
            TextView _grade = ll.findViewById(R.id.grade);
            _grade.setText(grade[interactionbeam.getGrade() - 1]);
            _grade.setTextColor(Color.parseColor(color[interactionbeam.getGrade() - 1]));
            TextView _descript = ll.findViewById(R.id.description);
            _descript.setText("  " + interactionbeam.getDescription());
        }
        List<DrugResult.TabooListBean> _tabooList = result.getTabooList();
        if(_tabooList.size()>0)
            tabooList.removeAllViews();
        for (DrugResult.TabooListBean bean:_tabooList){
            LinearLayout ll= (LinearLayout) View.inflate(this,R.layout.taboo_item,null);
            tabooList.addView(ll);
            TextView name=ll.findViewById(R.id.name);
            name.setText(bean.getDrug().getDrugCommonName());
            TextView _grade = ll.findViewById(R.id.grade);
            _grade.setText(grade[bean.getGrade() - 1]);
            _grade.setTextColor(Color.parseColor(color[bean.getGrade() - 1]));
            TextView _descript = ll.findViewById(R.id.description);
            _descript.setText("  " + bean.getDescription());
            TextView crowd=ll.findViewById(R.id.crowd);
            crowd.setText(bean.getCrowd());
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
