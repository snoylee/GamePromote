package com.xygame.sg.adapter.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.bean.comm.FanErBean;
import com.xygame.sg.image.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class FanErAdapter extends BaseAdapter {
    private List<FanErBean> strList;
    private Context context;
    private ImageLoader imageLoader;
    private boolean isModel;

    public FanErAdapter(Context context, List<FanErBean> strList, boolean isModel) {
        super();
        this.context = context;
        this.isModel = isModel;
        if (strList == null) {
            this.strList = new ArrayList<FanErBean>();
        } else {
            this.strList = strList;
        }
        imageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return strList.size();
    }

    @Override
    public FanErBean getItem(int arg0) {
        // TODO Auto-generated method stub
        return strList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    public void addDatas(List<FanErBean> datas, int mCurrentPage) {
        if (mCurrentPage == 1) {
            this.strList = datas;
        } else {
            this.strList.addAll(datas);
        }
        notifyDataSetChanged();
    }

    public void clearDatas() {
        this.strList.clear();
    }

    /**
     * 初始化View
     */
    private class ViewHolder {
        private ImageView id_img, topTip;
        private TextView nameText;
    }

    /**
     * 添加数据
     */
    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.faner_item,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.nameText = (TextView) convertView.findViewById(R.id.nameText);
            viewHolder.id_img = (ImageView) convertView.findViewById(R.id.id_img);
            viewHolder.topTip = (ImageView) convertView.findViewById(R.id.topTip);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.topTip.setVisibility(View.VISIBLE);
        FanErBean item = strList.get(position);
        if (isModel) {
            switch (position) {
                case 0:
                    viewHolder.topTip.setImageResource(R.drawable.mode_index1);
                    break;
                case 1:
                    viewHolder.topTip.setImageResource(R.drawable.mode_index2);
                    break;
                case 2:
                    viewHolder.topTip.setImageResource(R.drawable.mode_index3);
                    break;
                default:
                    viewHolder.topTip.setVisibility(View.GONE);
                    break;
            }
        } else {
            viewHolder.topTip.setVisibility(View.GONE);
        }
        viewHolder.nameText.setText(item.getUsernick());
        viewHolder.id_img.setImageResource(R.drawable.moren_icon);
        imageLoader.loadImage(item.getUserIcon(), viewHolder.id_img, true);
        return convertView;
    }
}