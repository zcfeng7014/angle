package com.sqc.zcfeng.angle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sqc.zcfeng.angle.R;
import com.sqc.zcfeng.angle.constans.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @OnClick(R.id.login)
    public void login(){
        RongIM.connect("JdBjdAHi4ix5J7QXoNsaEsPxALb3YnK63jtE5MY3p8FULY37k46b/OCJorIiqo20GyZRe0XqdG5yWaamN9EX9A==", new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
            ToastUtils.showShort("onTokenIncorrect");
            }

            @Override
            public void onSuccess(String s) {
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                ToastUtils.showShort("onError");
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }
}
