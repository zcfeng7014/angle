package com.sqc.zcfeng.angle.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.anton46.stepsview.StepsView;
import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.sqc.zcfeng.angle.R;
import com.sqc.zcfeng.angle.bean.Answer;
import com.sqc.zcfeng.angle.bean.Result;
import com.sqc.zcfeng.angle.constans.DoctorConfig;
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

public class ClinicActivity extends AppCompatActivity  {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    String[] message = {
            "开始智能导诊。（本系统所有步骤都是不可逆，如需修改请重新开始）",
            "请进行步骤2：选择就诊人群",
            "请进行步骤3：选择身体部位",
            "请进行步骤4：选择该身体部位主要症状",
            "请进行步骤5：选择该主要症状的起病特征",
            "请进行步骤6：选择其他伴随症状(多选)",
            "经系统判定，得到以下诊断结果，选择相应诊断点击下一步查询对应的标准科室",
            "科室列表："
    };
    String[] steplabel = {"", "", "", "", "", "", "", ""};
    @BindView(R.id.steps)
    StepsView steps;
    @BindView(R.id.question)
    TextView question;
    int step = 0;
    @BindView(R.id.pre)
    Button pre;
    @BindView(R.id.next)
    Button next;
    ArrayList<String> bsz = new ArrayList<>();
    HashMap<String, Integer> map = new HashMap<>();
    ArrayList<LinearLayout> stepviews = new ArrayList<>();
    @BindView(R.id.layout)
    LinearLayout layout;
    ArrayList<ArrayList> answer_list=new ArrayList<>();
    String sessionId=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("智能预检");
        steps.setLabels(steplabel).setProgressColorIndicator(Color.BLUE).drawView();
        stepviews.add(new LinearLayout(this));
        setStep(0);
        app= (App) getApplication();
        for (int i=0;i<8;i++)
            answer_list.add(new ArrayList<Answer>());
        HttpUtils.doPost(app.client, DoctorConfig.url, new JSONObject().toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

               if(response.code()==200){
                   String json=response.body().string();
                   try {
                       JSONObject data=new JSONObject(json).getJSONObject("data");
                       sessionId=data.getString("sessionId");
                       LogUtils.d(sessionId);
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }

               }

            }
        });
    }
    App app;
    @OnClick({R.id.pre, R.id.next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pre:

                setStep(0);
                break;
            case R.id.next:
                getList(step+1);
                break;
        }
    }

    public void setStep(int step) {
        this.step = step;
        pre.setVisibility(View.VISIBLE);
        next.setVisibility(View.VISIBLE);
        layout.removeAllViews();
        layout.addView(stepviews.get(step));
        switch (step) {
            case 0:
                stepviews.clear();
                layout.removeAllViews();
                stepviews.add(new LinearLayout(this));
                pre.setVisibility(View.INVISIBLE);
                break;
            case 7:
                next.setVisibility(View.INVISIBLE);
                break;

        }
        question.setText(message[step]);
        steps.setCompletedPosition(step).drawView();

    }

    private void getList(final int i) {
        final LinearLayout ll=stepviews.get(i-1);
        ArrayList<Answer> pre_list=answer_list.get(i-1);
        String choice="";
        if(i>1&&i!=6){
            RadioGroup rg= (RadioGroup) ll.getChildAt(0);
            for (int index=0;index<rg.getChildCount();index++){
                RadioButton rb= (RadioButton) rg.getChildAt(index);
                if(rb.isChecked()){
                    choice=pre_list.get(index).getId()+"";
                    break;
                }
            }

        }
        if(i>1&&i==6){
            ArrayList<String> choice_list=new ArrayList<>();
            for (int index=0;index<ll.getChildCount();index++){
                CheckBox rb= (CheckBox) ll.getChildAt(index);
                if(rb.isChecked()){
                    choice_list.add(pre_list.get(index).getId()+"");
                }
            }

            for (String str:choice_list){
                choice+="|"+str;
            }
            choice=choice.substring(1);
        }
        LogUtils.d(choice);
        JSONObject object=new JSONObject();

        try {
            object.put("choice",choice);
            object.put("sessionId",sessionId);
            object.put("step",i);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ArrayList<Answer>  list=answer_list.get(i);
        list.clear();
        String json_obj=object.toString();
        LogUtils.d(json_obj);
        HttpUtils.doPost(app.client, DoctorConfig.url, json_obj, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.d(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(response.code()==200){
                    String json=response.body().string();
                    LogUtils.d(json);
                    Gson gson=new Gson();
                    if(i<7)
                    try {
                        JSONArray data=new JSONObject(json).getJSONArray("data");
                       for(int index=0;index<data.length();index++){
                                list.add(gson.fromJson(data.getJSONObject(index).toString(),Answer.class));

                       }
                        ThreadUtils.RunInUI(new Runnable() {
                            @Override
                            public void run() {
                                LinearLayout ll=new LinearLayout(ClinicActivity.this);
                                if(i==5)
                                {
                                    setCheckBox(ll,list);
                                }
                                else
                                    setRadioGroup(ll,list);
                                stepviews.add(ll);
                                setStep(i);
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    else
                    {
                        JSONArray data= null;
                        final ArrayList<Result> results_list=new ArrayList<>();
                        try {
                            data = new JSONObject(json).getJSONArray("data");
                            for(int index=0;index<data.length();index++){
                                results_list.add(gson.fromJson(data.getJSONObject(index).toString(),Result.class));
                            }
                            ThreadUtils.RunInUI(new Runnable() {
                                @Override
                                public void run() {
                                    LinearLayout ll=new LinearLayout(ClinicActivity.this);
                                    setResult(ll,results_list);
                                    stepviews.add(ll);
                                    setStep(i);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
                else {
                    LogUtils.d(response.code());
                    LogUtils.d(response.message());
                }
            }
        });
    }

    private void setRadioGroup(LinearLayout layout, ArrayList<Answer> list) {
        layout.removeAllViews();
        RadioGroup rg=new RadioGroup(this);
        for (Answer answer : list) {
            RadioButton cb = new RadioButton(this);
            cb.setText(answer.getChoice());
            rg.addView(cb);

        }
        layout.addView(rg);
    }

    private void setCheckBox(LinearLayout layout, ArrayList<Answer> list) {
        layout.removeAllViews();
        layout.setOrientation(LinearLayout.VERTICAL);
        map.clear();
        for (Answer answer : list) {
            map.put(answer.getChoice(), answer.getId());
            CheckBox cb = new CheckBox(this);
            cb.setText(answer.getChoice());
            layout.addView(cb);
        }
    }
    private void setResult(LinearLayout layout, ArrayList<Result> list) {
        layout.removeAllViews();
        layout.setOrientation(LinearLayout.VERTICAL);

        for (Result answer : list) {
            LinearLayout rg= (LinearLayout) View.inflate(this,R.layout.doctor_result_item,null);
            TextView name=rg.findViewById(R.id.diseaseName);
            TextView juniorName=rg.findViewById(R.id.juniorName);
            TextView seniorName=rg.findViewById(R.id.seniorName);
            name.append(answer.getDiseaseName());
            juniorName.append(answer.getJuniorName());
            seniorName.append(answer.getSeniorName());
            layout.addView(rg);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
