package com.sqc.zcfeng.angle.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.anton46.stepsview.StepsView;
import com.sqc.zcfeng.angle.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClinicActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    String[] message = {
            "开始智能导诊",
            "选择就诊人群",
            "选择身体部位",
            "选择该身体部位主要症状",
            "选择该主要症状的起病特征",
            "选择其他伴随症状(多选)",
            "选择需要查询科室的疾病",
            "经系统判定，得到以下结论"
    };
    String[] steplabel = {"开始", "步骤1", "步骤2", "步骤3", "步骤4", "步骤5", "步骤6", "结果"};
    @BindView(R.id.steps)
    StepsView steps;
    @BindView(R.id.question)
    TextView question;
    int step = 0;
    @BindView(R.id.pre)
    Button pre;
    @BindView(R.id.next)
    Button next;
    @BindView(R.id.step1)
    Spinner step1;
    @BindView(R.id.step2)
    Spinner step2;
    @BindView(R.id.step3)
    Spinner step3;
    @BindView(R.id.step4)
    Spinner step4;
    @BindView(R.id.step5)
    LinearLayout step5;
    @BindView(R.id.step6)
    Spinner step6;
    @BindView(R.id.reslut)
    TextView reslut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("就医预检");
        steps.setLabels(steplabel).setProgressColorIndicator(Color.BLUE).drawView();
        setStep(0);

    }

    @OnClick({R.id.pre, R.id.next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pre:
                setStep(step - 1);
                break;
            case R.id.next:
                setStep(step + 1);
                break;
        }
    }

    public void setStep(int step) {
        this.step = step;
        pre.setVisibility(View.VISIBLE);
        next.setVisibility(View.VISIBLE);
        step1.setVisibility(View.GONE);
        step2.setVisibility(View.GONE);
        step3.setVisibility(View.GONE);
        step4.setVisibility(View.GONE);
        step5.setVisibility(View.GONE);
        step6.setVisibility(View.GONE);
        reslut.setVisibility(View.GONE);
        switch (step){
            case 0:
                pre.setVisibility(View.INVISIBLE);
                break;
            case 1:

                step1.setVisibility(View.VISIBLE);
                break;
            case 2:
                step2.setVisibility(View.VISIBLE);
                break;
            case 3:
                step3.setVisibility(View.VISIBLE);
                break;
            case 4:
                step4.setVisibility(View.VISIBLE);
                break;
            case 5:
                step5.setVisibility(View.VISIBLE);
                break;
            case 6:
                step6.setVisibility(View.VISIBLE);
                break;
            case 7:
                next.setVisibility(View.INVISIBLE);
                reslut.setVisibility(View.VISIBLE);
                break;
        }
        question.setText(message[step]);
        steps.setCompletedPosition(step).drawView();
    }
}
