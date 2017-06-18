package com.xygame.second.sg.Group.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.second.sg.Group.bean.CityGroups;
import com.xygame.sg.R;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.task.utils.AssetDataBaseManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2016/9/1.
 */
public class GroupNoticeAdapter extends BaseAdapter {
    private Context context;
    private List<CityGroups> vector;
    private ImageLoader mImageLoader;

    public GroupNoticeAdapter(Context context, List<CityGroups> vector) {
        this.context = context;
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
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
    public CityGroups getItem(int position) {
        return vector.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.goup_notice_item, parent, false);

            viewHolder.groupIamge = (ImageView) convertView
                    .findViewById(R.id.groupIamge);
            viewHolder.groupName=(TextView)convertView.findViewById(R.id.groupName);
            viewHolder.groupTip = (TextView) convertView.findViewById(R.id.groupTip);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CityGroups item = vector.get(position);
        AssetDataBaseManager.CityBean provinceBean = AssetDataBaseManager.getManager().queryCityById(Integer.parseInt(item.getProvinceId()));
        viewHolder.groupName.setText(provinceBean.getName());
        viewHolder.groupTip.setText("已有".concat(String.valueOf(item.getNoticeCout())).concat("份通告"));
        mImageLoader.loadImage(item.getGroupLogo(), viewHolder.groupIamge, true);
        return convertView;
    }

    public void clearDatas() {
        this.vector.clear();
        notifyDataSetChanged();
    }

    public void addDatas(List<CityGroups> datas) {
        this.vector = datas;
        notifyDataSetChanged();
    }

    public void updateItemNums(CityGroups goupNoticeBean) {
        for (int i=0;i<vector.size();i++){
           if (goupNoticeBean.getProvinceId().equals(vector.get(i).getProvinceId())){
               vector.get(i).setNoticeCout(goupNoticeBean.getNoticeCout());
               vector.get(i).setGoupNoticeBeans(goupNoticeBean.getGoupNoticeBeans());
               break;
           }
        }
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView groupName,groupTip;
        ImageView groupIamge;
    }
}