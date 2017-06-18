package com.xygame.sg.activity.notice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.commen.bean.ShootSubTypeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xy on 2016/2/16.
 */
public class SubShootTypeAdapter extends BaseAdapter {
    private Context mContext;
    private List<ShootSubTypeBean> mSubShootTypeBeans;
    private List<Boolean> mSelectedList = new ArrayList<Boolean>();


    public SubShootTypeAdapter(Context context, List<ShootSubTypeBean> subShootTypeBeans) {
        super();
        mContext = context;
        mSubShootTypeBeans = subShootTypeBeans;
        for (int i = 0; i < mSubShootTypeBeans.size(); i++) {
            mSelectedList.add(false);
        }
    }

    public void setSelectedItem(int index) {
        for (int i = 0; i < mSelectedList.size(); i++) {
            if (i == index) {
                mSelectedList.set(i, true);
            } else {
                mSelectedList.set(i, false);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mSubShootTypeBeans.size();
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
    public View getView(int i, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.notice_shoot_select_sub_item, null);

        RelativeLayout root_rl = (RelativeLayout) convertView.findViewById(R.id.root_rl);
        TextView type_name_tv = (TextView) convertView.findViewById(R.id.type_tv);
        View divider_line = convertView.findViewById(R.id.divider_line);

        boolean isLastSelected = mSelectedList.get(i);
        ShootSubTypeBean itemData = mSubShootTypeBeans.get(i);

        type_name_tv.setText(itemData.getTypeName());


        if (isLastSelected){
            type_name_tv.setTextColor(mContext.getResources().getColor(R.color.dark_green));
            divider_line.setBackgroundColor(mContext.getResources().getColor(R.color.dark_green));
        }else{
            type_name_tv.setTextColor(mContext.getResources().getColor(R.color.black));
        }

        return convertView;
    }
}
