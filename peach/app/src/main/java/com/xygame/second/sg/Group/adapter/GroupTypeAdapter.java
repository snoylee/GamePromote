package com.xygame.second.sg.Group.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.second.sg.Group.bean.GoupNoticeBean;
import com.xygame.sg.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2016/9/1.
 */
public class GroupTypeAdapter extends BaseAdapter {
    private Context context;
    private List<GoupNoticeBean> vector;
    private Integer[] drawbleSource;
    public GroupTypeAdapter(Context context, List<GoupNoticeBean> vector) {
        this.context = context;
        drawbleSource=new Integer[3];
        drawbleSource[0]=R.drawable.group1;
        drawbleSource[1]=R.drawable.group2;
        drawbleSource[2]=R.drawable.group3;
        if (vector != null) {
            this.vector = vector;
        } else {
            this.vector = new ArrayList<>();
        }
    }

    public List<GoupNoticeBean> getDatas(){
        return vector;
    }

    @Override
    public int getCount() {
        return vector.size();
    }

    @Override
    public GoupNoticeBean getItem(int position) {
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
        GoupNoticeBean item = vector.get(position);
        String[] arryName=item.getGroupName().split("-");
        viewHolder.groupName.setText(arryName[1]);
        viewHolder.groupTip.setText("已有".concat(String.valueOf(item.getNoticeCount())).concat("份通告"));
        int index=Integer.parseInt(item.getGroupType());
        index=index-1;
        int finalIndex=0;
        if (index>=3){
            finalIndex=index%3;
        }else{
            finalIndex=index;
        }
        viewHolder.groupIamge.setImageResource(drawbleSource[finalIndex]);
        return convertView;
    }

    public void clearDatas() {
        this.vector.clear();
        notifyDataSetChanged();
    }

    public void updateItemNums(GoupNoticeBean goupNoticeBean) {
        for (int i=0;i<vector.size();i++){
           if (goupNoticeBean.getGroupId().equals(vector.get(i).getGroupId())) {
               vector.get(i).setNoticeCount(goupNoticeBean.getNoticeCount());
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