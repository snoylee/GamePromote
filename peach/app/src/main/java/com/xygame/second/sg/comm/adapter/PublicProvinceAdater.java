package com.xygame.second.sg.comm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.bean.comm.ProvinceBean;

import java.util.ArrayList;
import java.util.List;

public class PublicProvinceAdater extends BaseAdapter {

    private Context context;
    private List<ProvinceBean> datas;

    public PublicProvinceAdater(Context context, List<ProvinceBean> datas) {
        this.context = context;
        if (null == datas) {
            this.datas = new ArrayList<>();
        } else {
            this.datas = datas;
        }
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public ProvinceBean getItem(int position) {
        ProvinceBean item = datas.get(position);
        item.get();
        return item;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // TODO Auto-generated method stub
        HoldView holdView;
        if (view == null) {
            holdView = new HoldView();
            view = LayoutInflater.from(context).inflate(
                    R.layout.public_province_item, null);
            holdView.areaName = (TextView) view.findViewById(R.id.typeName);
            view.setTag(holdView);
        } else {
            holdView = (HoldView) view.getTag();
        }

        ProvinceBean item = datas.get(position);
        item.get();
        holdView.areaName.setText(item.getName());
        return view;
    }

    public void updateDatas(List<ProvinceBean> vector) {
        this.datas = vector;
        notifyDataSetChanged();
    }

    class HoldView {
        TextView areaName;
    }

}
