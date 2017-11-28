package com.sqc.zcfeng.angle.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.sqc.zcfeng.angle.R;
import com.sqc.zcfeng.lib.Utils.ToastUtils;

public class ConversationActivity extends AppCompatActivity {
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String title = getIntent().getData().getQueryParameter("title");
        if (!TextUtils.isEmpty(title)){
            if(title.equals("zcfeng"))
                setTitle("测试医生");
            else
                setTitle(title);
        }
        bundle=getIntent().getExtras();
        if(bundle!=null&&!bundle.isEmpty()){
            ToastUtils.show(this,"有参数传递");
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_conversation, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // 动态设置ToolBar状态
        if(bundle!=null&&!bundle.isEmpty()){
           menu.findItem(R.id.a1).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.a1:
                startActivity(new Intent(this,AdviceInfoActivity.class));
                break;
            case R.id.a2:
                startActivity(new Intent(this,DoctorAdviceListActivity.class));
            default:;
        }
        return true;
    }
}
