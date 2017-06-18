package com.xygame.sg.activity.personal.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.utils.assort.AssortPinyinList;
import com.xygame.sg.utils.assort.HashList;
import com.xygame.sg.utils.assort.LanguageComparator_CN;

public class CountryAdapter extends BaseAdapter {
    // 字符串
    private List<Map<String, String>> strList;

    private AssortPinyinList assort = new AssortPinyinList();

    private Context context;

    private String countryName;

    public Map<String, String> ctries = new TreeMap<String, String>();
    // 中文排序
    private LanguageComparator_CN cnSort = new LanguageComparator_CN();

    private HashList<String, String> tempDatas;

    public CountryAdapter(Context context, List<Map<String, String>> strList, String countryName) {
        super();
        this.context = context;
        this.countryName = countryName;
        if (strList == null) {
            this.strList = new ArrayList<Map<String, String>>();
        } else {
            this.strList = strList;
        }

        // 排序
        sort();

        initNewSort();
    }

    public List<String> list = new ArrayList<String>();

    private void initNewSort() {
        strList.clear();
        list.clear();
        tempDatas = assort.getHashList();
        for (int i = 0; i < tempDatas.size(); i++) {
            list.addAll(tempDatas.getValueListIndex(i));
        }
    }

    private void sort() {
        // 分类
        for (int i = 0; i < strList.size() - 1; i++) {
            ctries.put(strList.get(i).get("country_name"), strList.get(i).get("country_code"));
            assort.getHashList().add(strList.get(i).get("country_name"));
        }
        assort.getHashList().sortKeyComparator(cnSort);
        for (int i = 0, length = assort.getHashList().size(); i < length; i++) {
            Collections.sort((assort.getHashList().getValueListIndex(i)),
                    cnSort);
        }

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public String getItem(int arg0) {
        // TODO Auto-generated method stub
        return ctries.get(list.get(arg0));
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    /**
     * 初始化View
     */
    private class ViewHolder {
        private View bottomLine;
        private ImageView selectIcon;
        private TextView countryText;
    }

    /**
     * 添加数据
     */
    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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
        viewHolder.countryText.setText(list.get(position));
        if (countryName == null) {
            return convertView;
        }
        if (countryName.equals(list.get(position))) {
            viewHolder.selectIcon.setVisibility(View.VISIBLE);
            viewHolder.selectIcon.setImageResource(R.drawable.sg_list_select_icon);
            viewHolder.countryText.setTextColor(context.getResources().getColor(R.color.dark_green));
        } else {
            viewHolder.selectIcon.setVisibility(View.GONE);
            viewHolder.countryText.setTextColor(context.getResources().getColor(R.color.black));
        }

        return convertView;
    }

}
