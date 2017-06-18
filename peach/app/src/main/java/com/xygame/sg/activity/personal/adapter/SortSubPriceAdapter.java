package com.xygame.sg.activity.personal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haarman.listviewanimations.ArrayAdapter;
import com.xygame.sg.R;
import com.xygame.sg.activity.personal.bean.PriceBean;

import java.util.List;

/**
 * Created by xy on 2015/11/25.
 */
public class SortSubPriceAdapter extends ArrayAdapter<PriceBean> {
    private LayoutInflater inflater;
    private Context context;


    public SortSubPriceAdapter(Context context, List<PriceBean> dataList) {
        super(dataList);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public boolean hasStableIds() {
        return true;
    }
    public List<PriceBean> getDatas(){
        return getAll();
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        convertView = inflater.inflate(R.layout.category_item, null);

        if (i == getCount() - 1) {
            View bottom_divider = convertView.findViewById(R.id.divider_line);
            LinearLayout.LayoutParams layoutParamsV = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
            bottom_divider.setLayoutParams(layoutParamsV);
        }

        PriceBean priceBean = getItem(i);
        TextView second_category_tv = (TextView) convertView.findViewById(R.id.second_category_tv);
        TextView price_tv = (TextView) convertView.findViewById(R.id.price_tv);
        TextView unit_tv = (TextView) convertView.findViewById(R.id.unit_tv);
        TextView note_tv = (TextView) convertView.findViewById(R.id.note_tv);
        ImageView action_iv = (ImageView) convertView.findViewById(R.id.action_iv);
        action_iv.setImageResource(R.drawable.sg_moving_icon);


        second_category_tv.setText(priceBean.getItemName());
        price_tv.setText("￥"+priceBean.getPrice());
        unit_tv.setText("/" + priceBean.getPriceUnit());
        if (priceBean.getLimitParter().equals("")){
            note_tv.setVisibility(View.GONE);
        } else {
            note_tv.setText("每次拍摄人数不得超过：" + priceBean.getLimitParter() + "人");
        }


        return convertView;
    }


}
