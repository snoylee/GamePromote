package com.xygame.second.sg.personal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.xygame.second.sg.personal.bean.PhotoBean;
import com.xygame.second.sg.personal.bean.PhotoListBean;
import com.xygame.sg.R;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2016/9/1.
 */
public class UserPhotoesAdapter extends BaseAdapter {
    private Context context;
    private List<PhotoListBean> vector;

    public UserPhotoesAdapter(Context context, List<PhotoListBean> vector) {
        this.context = context;
        if (vector != null) {
            this.vector = vector;
        } else {
            this.vector = new ArrayList<>();
        }
    }

    @Override
    public int getCount() {
        return vector.size();
    }

    @Override
    public PhotoListBean getItem(int position) {
        return vector.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.user_photoes_item, parent, false);

            viewHolder.gridview = (GridView) convertView.findViewById(R.id.gridview);
            viewHolder.timerText = (TextView) convertView.findViewById(R.id.timerText);
            viewHolder.gridAdapter = new UserPhotoGridAdapter(context, null);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PhotoListBean item = vector.get(position);
        viewHolder.timerText.setText(item.getTimerDesc());
        viewHolder.gridview.setAdapter(viewHolder.gridAdapter);
        final List<PhotoBean> photoes = item.getPhotoes();
        viewHolder.gridAdapter.addDatas(photoes);
        viewHolder.gridAdapter.notifyDataSetChanged();
        viewHolder.gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                String[] urls = new String[photoes.size()];
                for (int j = 0; j < photoes.size(); j++) {
                    urls[j] = photoes.get(j).getResUrl();
                }
                Constants.imageBrower(context, arg2, urls, false);
            }
        });
        return convertView;
    }

    public void clearDatas() {
        this.vector.clear();
        notifyDataSetChanged();
    }

    public void addDatas(List<PhotoBean> datas, int mCurrentPage) {
        if (mCurrentPage == 1) {
            List<PhotoListBean> finalDatas = new ArrayList<>();
            for (PhotoBean item : datas) {
                if (finalDatas.size() == 0) {
                    PhotoListBean photoListBean1 = new PhotoListBean();
                    photoListBean1.setTimerDesc(CalendarUtils.getHenGongYearMonthDay(Long.parseLong(item.getCreateTime())));
                    List<PhotoBean> tempDatas1 = new ArrayList<>();
                    tempDatas1.add(item);
                    photoListBean1.setPhotoes(tempDatas1);
                    finalDatas.add(photoListBean1);
                } else {
                    PhotoListBean photoListBean = finalDatas.get(finalDatas.size() - 1);
                    if (photoListBean.getTimerDesc().equals(CalendarUtils.getHenGongYearMonthDay(Long.parseLong(item.getCreateTime())))) {
                        List<PhotoBean> tempDatas = photoListBean.getPhotoes();
                        tempDatas.add(item);
                        finalDatas.get(finalDatas.size() - 1).setPhotoes(tempDatas);
                    } else {
                        PhotoListBean photoListBean1 = new PhotoListBean();
                        photoListBean1.setTimerDesc(CalendarUtils.getHenGongYearMonthDay(Long.parseLong(item.getCreateTime())));
                        List<PhotoBean> tempDatas1 = new ArrayList<>();
                        tempDatas1.add(item);
                        photoListBean1.setPhotoes(tempDatas1);
                        finalDatas.add(photoListBean1);
                    }
                }
            }
            this.vector = finalDatas;
        } else {
            for (PhotoBean item : datas) {
                PhotoListBean photoListBean = vector.get(vector.size() - 1);
                if (photoListBean.getTimerDesc().equals(CalendarUtils.getHenGongYearMonthDay(Long.parseLong(item.getCreateTime())))) {
                    List<PhotoBean> tempDatas = photoListBean.getPhotoes();
                    tempDatas.add(item);
                    vector.get(vector.size() - 1).setPhotoes(tempDatas);
                } else {
                    PhotoListBean photoListBean1 = new PhotoListBean();
                    photoListBean1.setTimerDesc(CalendarUtils.getHenGongYearMonthDay(Long.parseLong(item.getCreateTime())));
                    List<PhotoBean> tempDatas1 = new ArrayList<>();
                    tempDatas1.add(item);
                    photoListBean1.setPhotoes(tempDatas1);
                    vector.add(photoListBean1);
                }
            }
        }
    }

    private static class ViewHolder {
        TextView timerText;
        GridView gridview;
        UserPhotoGridAdapter gridAdapter;
    }
}