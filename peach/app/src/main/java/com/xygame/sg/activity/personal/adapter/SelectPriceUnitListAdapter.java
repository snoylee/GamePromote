package com.xygame.sg.activity.personal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xygame.sg.R;

import java.util.Map;

/**
 * Created by xy on 2015/11/18.
 */
public class SelectPriceUnitListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private String[] priceUnitArr;




    private int selectedId;//已选项的id


    public SelectPriceUnitListAdapter(Context context,String[] priceUnitArr,int selectedId) {
        super();
        this.context = context;
        this.priceUnitArr= priceUnitArr;
        this.selectedId =selectedId;
        inflater = LayoutInflater.from(context);
    }

    public void setSelectedId(int selectedId) {
        this.selectedId = selectedId;
    }

    @Override
    public int getCount() {
        return priceUnitArr.length;
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

        convertView = inflater.inflate(R.layout.common_category_item, null);

        if (i == getCount() - 1) {
            View bottom_divider = convertView.findViewById(R.id.bottom_divider);
            LinearLayout.LayoutParams layoutParamsV = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
            bottom_divider.setLayoutParams(layoutParamsV);
        }

        TextView item_name_tv = (TextView) convertView.findViewById(R.id.item_name_tv);
        ImageView selected_iv = (ImageView) convertView.findViewById(R.id.selected_iv);
        if (selectedId == i){
            selected_iv.setVisibility(View.VISIBLE);
        } else {
            selected_iv.setVisibility(View.GONE);
        }

        item_name_tv.setText(priceUnitArr[i]);

        return convertView;
    }
}
