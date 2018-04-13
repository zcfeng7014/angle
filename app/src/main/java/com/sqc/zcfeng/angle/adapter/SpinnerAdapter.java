package com.sqc.zcfeng.angle.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sqc.zcfeng.angle.bean.Answer;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/11.
 */

public class SpinnerAdapter extends BaseAdapter{
    ArrayList<Answer> data;
    Context context;
    public SpinnerAdapter(Context context, ArrayList<Answer> data) {
        this.data = data;
        this.context = context;
    }


    @Override
    public int getCount() {
        return data.size();
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
        TextView tv= (TextView) View.inflate(context,android.R.layout.simple_spinner_dropdown_item,null);
        tv.setText(data.get(i).getChoice());
        return tv;
    }
}
