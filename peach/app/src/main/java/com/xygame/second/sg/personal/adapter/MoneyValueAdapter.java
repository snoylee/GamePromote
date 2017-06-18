package com.xygame.second.sg.personal.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xygame.second.sg.personal.bean.MoneyValueBean;
import com.xygame.sg.R;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2016/10/27.
 */
public class MoneyValueAdapter extends BaseAdapter {
    private Context context;
    private List<MoneyValueBean> vector;
    public MoneyValueAdapter(Context context, List<MoneyValueBean> vector) {
        this.context = context;
        if(vector!=null){
            this.vector = vector;
        }else{
            this.vector=new ArrayList<>();
        }
    }

    @Override
    public int getCount() {
        return vector.size();
    }

    @Override
    public MoneyValueBean getItem(int position) {
        MoneyValueBean item=vector.get(position);
        return item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder = null;
        if (null == convertView)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.cub_jp_small_type_item, parent, false);

            viewHolder.typeName = (TextView) convertView.findViewById(R.id.typeName);
            viewHolder.gouView = convertView
                    .findViewById(R.id.gouView);
            viewHolder.backgroundColor =convertView
                    .findViewById(R.id.backgroundColor);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MoneyValueBean item=vector.get(position);
        viewHolder.typeName.setText(ConstTaskTag.getIntPrice(item.getMoneyValue()).concat("元"));
        if (item.isSelect()){
            viewHolder.gouView.setVisibility(View.GONE);
            viewHolder.typeName.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.backgroundColor.setBackgroundResource(R.drawable.shape_rect_dark_green10);
        }else{
            viewHolder.gouView.setVisibility(View.GONE);
            viewHolder.typeName.setTextColor(context.getResources().getColor(R.color.dark_green));
            viewHolder.backgroundColor.setBackgroundResource(R.drawable.shape_rect_white);
        }
        return convertView;
    }

    public void updateAreaDatas(List<MoneyValueBean> areaBeans) {
        this.vector=areaBeans;
        notifyDataSetChanged();
    }

    public MoneyValueBean getMoneyValueBean(){
        MoneyValueBean item=null;
        for (MoneyValueBean it:vector){
            if (it.isSelect()){
                item=it;
                break;
            }
        }
        return item;
    }

    private static class ViewHolder
    {
        TextView typeName;
        View backgroundColor,gouView;
    }

    public void updateSelect(MoneyValueBean item) {
        ThreadPool.getInstance().excuseThread(new JugmentUse(item));
    }

    private class JugmentUse implements Runnable {
        private MoneyValueBean item;
        public JugmentUse(MoneyValueBean item){
            this.item=item;
        }
        @Override
        public void run() {
            for (int i=0;i<vector.size();i++){
                if (vector.get(i).getId()==item.getId()){
                    vector.get(i).setIsSelect(true);
                }else {
                    vector.get(i).setIsSelect(false);
                }
            }
            mHandler.sendEmptyMessage(1);
        }
    }

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);
        }
    };
}