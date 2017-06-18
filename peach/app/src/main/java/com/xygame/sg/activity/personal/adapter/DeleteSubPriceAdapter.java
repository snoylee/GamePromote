package com.xygame.sg.activity.personal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.personal.bean.PriceBean;
import com.xygame.sg.activity.personal.bean.RsumeBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by xy on 2015/11/25.
 */
public class DeleteSubPriceAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;

    private List<PriceBean> dataList = new ArrayList<PriceBean>();

    public DeleteSubPriceAdapter(Context context, List<PriceBean> dataList) {
        super();
        this.context = context;
        this.dataList = dataList;
        inflater = LayoutInflater.from(context);
    }

    public void addData(PriceBean priceBean){
        if (dataList!= null){
            dataList.add(priceBean);
        }
    }

    public void setData(List<PriceBean> dataList){
        if (this.dataList!= null){
            this.dataList.clear();
            this.dataList = dataList;
        }
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public PriceBean getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        convertView = inflater.inflate(R.layout.category_item, null);

        if (i == getCount() - 1) {
            View bottom_divider = convertView.findViewById(R.id.divider_line);
            LinearLayout.LayoutParams layoutParamsV = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
            bottom_divider.setLayoutParams(layoutParamsV);
        }

        PriceBean priceBean = dataList.get(i);
        TextView second_category_tv = (TextView) convertView.findViewById(R.id.second_category_tv);
        TextView price_tv = (TextView) convertView.findViewById(R.id.price_tv);
        TextView unit_tv = (TextView) convertView.findViewById(R.id.unit_tv);
        TextView note_tv = (TextView) convertView.findViewById(R.id.note_tv);
        ImageView action_iv = (ImageView) convertView.findViewById(R.id.action_iv);
        ImageView select_iv = (ImageView) convertView.findViewById(R.id.select_iv);
        select_iv.setVisibility(View.VISIBLE);

        if (priceBean.isSelected()){
            select_iv.setImageResource(R.drawable.gou);
        } else {
            select_iv.setImageResource(R.drawable.gou_null);
        }


        second_category_tv.setText(priceBean.getItemName());
        price_tv.setText("￥"+priceBean.getPrice());
        unit_tv.setText("/"+priceBean.getPriceUnit());
        if (priceBean.getLimitParter().equals("")){
            note_tv.setVisibility(View.GONE);
        } else {
            note_tv.setText("每次拍摄人数不得超过：" + priceBean.getLimitParter() + "人");
        }

        action_iv.setVisibility(View.GONE);
        return convertView;
    }

    public void setSelectedItem(int index){
        dataList.get(index).setIsSelected(!dataList.get(index).isSelected());
        notifyDataSetChanged();
    }

    public List<PriceBean> getSelectDatas(){
        List<PriceBean> tempDatas=new ArrayList<PriceBean>();
        for(PriceBean it:dataList){
            if(it.isSelected()){
                tempDatas.add(it);
            }
        }
        return tempDatas;
    }

    public List<PriceBean> getLeaveDatas(){

        Iterator<PriceBean> it = dataList.iterator();
        while(it.hasNext()){
            PriceBean priceBean = it.next();
            if(priceBean.isSelected()){
                it.remove();
            }
        }
        return dataList;
    }
}
