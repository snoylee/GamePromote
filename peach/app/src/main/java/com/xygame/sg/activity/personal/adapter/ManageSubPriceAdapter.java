package com.xygame.sg.activity.personal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.personal.bean.PriceBean;

import java.util.List;

/**
 * Created by xy on 2015/11/25.
 */
public class ManageSubPriceAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;

    private List<PriceBean> dataList;

    public ManageSubPriceAdapter(Context context, List<PriceBean> dataList) {
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

    public void setData(List<PriceBean> priceBeanList){
        dataList = priceBeanList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        convertView = inflater.inflate(R.layout.category_item, null);

        PriceBean priceBean = dataList.get(i);
        TextView second_category_tv = (TextView) convertView.findViewById(R.id.second_category_tv);
        TextView price_tv = (TextView) convertView.findViewById(R.id.price_tv);
        TextView unit_tv = (TextView) convertView.findViewById(R.id.unit_tv);
        TextView note_tv = (TextView) convertView.findViewById(R.id.note_tv);
        ImageView action_iv = (ImageView) convertView.findViewById(R.id.action_iv);

        second_category_tv.setText(priceBean.getItemName());
        price_tv.setText("￥"+priceBean.getPrice());
        unit_tv.setText("/"+priceBean.getPriceUnit());
        if (priceBean.getLimitParter().equals("")){
            note_tv.setVisibility(View.GONE);
        } else {
            note_tv.setText("每次拍摄人数不得超过：" + priceBean.getLimitParter() + "人");
        }
        return convertView;
    }
}
