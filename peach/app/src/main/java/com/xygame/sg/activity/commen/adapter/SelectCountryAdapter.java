package com.xygame.sg.activity.commen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.commen.bean.CountryBean;

import java.util.List;

/**
 * Created by xy on 2016/2/2.
 */
public class SelectCountryAdapter extends BaseAdapter {
    private List<CountryBean> strList;
    private Context context;

    public SelectCountryAdapter(Context context, List<CountryBean> strList) {
        super();
        this.context = context;
        this.strList = strList;
    }

    @Override
    public int getCount() {
        return strList.size();
    }

    @Override
    public CountryBean getItem(int i) {
        return strList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sg_country_item,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.countryText = (TextView) convertView
                    .findViewById(R.id.countryText);
            viewHolder.bottomLine = convertView
                    .findViewById(R.id.bottomLine);
            viewHolder.selectIcon = (ImageView) convertView.findViewById(R.id.selectIcon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position == strList.size() - 1) {
            viewHolder.bottomLine.setVisibility(View.GONE);
        } else {
            viewHolder.bottomLine.setVisibility(View.VISIBLE);
        }
        CountryBean item = strList.get(position);
        viewHolder.countryText.setText(item.getcName());


        return convertView;
    }

    private class ViewHolder {
        private View bottomLine;
        private ImageView selectIcon;
        private TextView countryText;
    }
}
