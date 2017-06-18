package com.xygame.sg.activity.notice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.sg.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xy on 2015/12/9.
 */
public class SortListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Map> mDataList;
    private List<Boolean> mSelectedList = new ArrayList<Boolean>();
    public SortListAdapter(Context context, List<Map> dataList) {
        super();
        mContext = context;
        mDataList = dataList;
        for (int i=0;i<mDataList.size();i++){
            if (i == 0){
                mSelectedList.add(true);
            } else {
                mSelectedList.add(false);
            }

        }
    }


    public void setSelectedItem(int index){
        for (int i=0;i<mSelectedList.size();i++){
            if (i == index){
                mSelectedList.set(i,true);
            } else {
                mSelectedList.set(i,false);
            }
        }
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mDataList.size();
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
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.notice_sort_item, parent, false);
        }


        TextView item_name_tv = BaseViewHolder.get(convertView, R.id.item_name_tv);
        ImageView selected_iv = BaseViewHolder.get(convertView, R.id.selected_iv);

        boolean isLastSelected = mSelectedList.get(i);
        Map itemData = mDataList.get(i);

        item_name_tv.setText((String) itemData.get("sortTypeName"));
        if (isLastSelected){
            selected_iv.setVisibility(View.VISIBLE);
            item_name_tv.setTextColor(mContext.getResources().getColor(R.color.dark_green));
        } else {
            selected_iv.setVisibility(View.GONE);
            item_name_tv.setTextColor(mContext.getResources().getColor(R.color.black));
        }
        return convertView;
    }
}
