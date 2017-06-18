package com.xygame.sg.activity.personal.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.commen.bean.ShootSubTypeBean;

import java.util.List;
import java.util.Map;

/**
 * Created by xy on 2015/11/18.
 */
public class SelectSecondCategoryListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;

    private List<ShootSubTypeBean> dataList;

    private String selectedId;//已选项的id

    public SelectSecondCategoryListAdapter(Context context, List<ShootSubTypeBean> dataList,String selectedId) {
        super();
        this.context = context;
        this.dataList = dataList;
        this.selectedId = selectedId;
        inflater = LayoutInflater.from(context);
    }

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
    }



    @Override
    public int getCount() {
        if (dataList.size()==1 ){
            try {
                Map item = (Map) dataList.get(0);
                return dataList.size();
            } catch (ClassCastException e){
                e.printStackTrace();
                return 0;

            }
        }else {
            return dataList.size();
        }

    }

    @Override
    public ShootSubTypeBean getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        convertView = inflater.inflate(R.layout.common_category_item, null);

        if (i == getCount() - 1) {
            View bottom_divider = convertView.findViewById(R.id.bottom_divider);
            LinearLayout.LayoutParams layoutParamsV = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
            bottom_divider.setLayoutParams(layoutParamsV);
        }
        ShootSubTypeBean itemData = dataList.get(i);
        TextView item_name_tv = (TextView) convertView.findViewById(R.id.item_name_tv);
        ImageView selected_iv = (ImageView) convertView.findViewById(R.id.selected_iv);
        if ((itemData.getTypeId()+"").equals(selectedId)){
            selected_iv.setVisibility(View.VISIBLE);
        } else {
            selected_iv.setVisibility(View.GONE);
        }

        item_name_tv.setText(itemData.getTypeName());
        return convertView;
    }
}
