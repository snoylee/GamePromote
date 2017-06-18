package com.xygame.sg.activity.personal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.commen.bean.ShootTypeBean;
import com.xygame.sg.utils.SGApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xy on 2015/11/18.
 */
public class CategoryForPerAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<ShootTypeBean> dataList;
    private Map<String, Boolean> beanMap;


    public CategoryForPerAdapter(Context context, Map<String, Boolean> beanMap) {
        super();
        this.dataList = SGApplication.getInstance().getTypeList();
        if (this.dataList==null){
            this.dataList=new ArrayList<>();
        }
        this.beanMap = beanMap;
        inflater = LayoutInflater.from(context);
    }

    public List<ShootTypeBean> getSelectDatas(){
        List<ShootTypeBean> data=new ArrayList<>();
        for (ShootTypeBean it:dataList){
            if (beanMap.get(String.valueOf(it.getTypeId()))){
                data.add(it);
            }
        }
        return data;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public ShootTypeBean getItem(int i) {
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
        ShootTypeBean itemData = dataList.get(i);
        TextView item_name_tv = (TextView) convertView.findViewById(R.id.item_name_tv);
        ImageView selected_iv = (ImageView) convertView.findViewById(R.id.selected_iv);
        if (beanMap.get(String.valueOf(itemData.getTypeId()))){
            selected_iv.setVisibility(View.VISIBLE);
        }else{
            selected_iv.setVisibility(View.GONE);
        }
        item_name_tv.setText(itemData.getTypeName());
        convertView.setOnClickListener(new ClickItemListener(itemData));
        return convertView;
    }

    private class ClickItemListener implements View.OnClickListener{

        private ShootTypeBean itemData;

        public ClickItemListener(ShootTypeBean itemData){
            this.itemData=itemData;
        }

        @Override
        public void onClick(View v) {
            clickItem(itemData);
        }
    }

    private void clickItem(ShootTypeBean itemData) {
        beanMap.put(String.valueOf(itemData.getTypeId()),!beanMap.get(String.valueOf(itemData.getTypeId())));
        notifyDataSetChanged();
    }
}