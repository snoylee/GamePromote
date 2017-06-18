package com.xygame.sg.activity.notice.adapter;

import java.util.ArrayList;
import java.util.List;

import com.xygame.sg.R;
import com.xygame.sg.activity.notice.bean.SignedBean;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.CalendarUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by xy on 2015/11/16.
 */
public class SignedAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<SignedBean> datas;
    private ImageLoader imageLoader;
    public SignedAdapter(Context context, List<SignedBean> datas) {
        super();
        inflater = LayoutInflater.from(context);
        imageLoader =ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        if(datas==null){
        	this.datas=new ArrayList<SignedBean>();
        }else{
        	this.datas=datas;
        }
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public SignedBean getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    
    public void addDatas(List<SignedBean> tempDatas){
    	datas.addAll(tempDatas);
    	notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.signed_item, null);
            holder = new ViewHolder();
            holder.userImage = (ImageView) convertView.findViewById(R.id.userImage);
            holder.userName = (TextView) convertView.findViewById(R.id.userName);
            holder.signTime = (TextView) convertView.findViewById(R.id.signTime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SignedBean item = datas.get(i);
        holder.userName.setText(item.getUsernick());
        holder.signTime.setText("签到时间：".concat(CalendarUtils.getHenGongDateDis(Long.parseLong(item.getSignTime()))));
        imageLoader.loadImage(item.getUserIcon(), holder.userImage, true);
        return convertView;
    }

    private class ViewHolder {
        private ImageView userImage;
        private TextView userName,signTime;
    }
}
