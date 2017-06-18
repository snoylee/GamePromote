package com.xygame.sg.activity.notice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.nostra13.universalimageloader.core.ImageLoader;
import com.xygame.sg.R;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.activity.commen.bean.ShootTypeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xy on 2015/12/9.
 */
public class SelectShootTypeAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<ShootTypeBean> shootTypeBeans;
    private List<Boolean> mSelectedList = new ArrayList<Boolean>();
    private ImageLoader imageLoader;

    public SelectShootTypeAdapter(Context context) {
        super();
        mContext = context;
        inflater = LayoutInflater.from(context);
        imageLoader = ImageLoader.getInstance();
        shootTypeBeans = SGApplication.getInstance().getTypeList();
        if (shootTypeBeans==null){
            shootTypeBeans=new ArrayList<>();
        }
        mSelectedList.add(true);
        for (int i=0;i<shootTypeBeans.size();i++){
            mSelectedList.add(false);
        }
    }

    public void setSelectedItem(int index){
        for (int i=0;i<mSelectedList.size();i++){
            if (i == index){
                mSelectedList.set(i,true);
            } else {
                mSelectedList.set(i,false);
            }
        }
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return shootTypeBeans.size()+1;//默认的全部没从接口返回
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
    public View getView(int i, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.notice_shoot_select_item, null);
            holder = new ViewHolder();
            holder.root_rl = (RelativeLayout) convertView.findViewById(R.id.root_rl);
            holder.type_name_tv = (TextView) convertView.findViewById(R.id.type_tv);
            holder.cate_iv = (ImageView) convertView.findViewById(R.id.cate_iv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        boolean isLastSelected = mSelectedList.get(i);
        if (i>0){
            ShootTypeBean itemData = shootTypeBeans.get(i - 1);
            holder.type_name_tv.setText(itemData.getTypeName());
            imageLoader.displayImage(itemData.getIconDefaultUrl(), holder.cate_iv);

        } else {
            holder.type_name_tv.setText("全部");
            holder.cate_iv.setImageResource(R.drawable.all);
        }
        if (isLastSelected){
            holder.root_rl.setBackgroundColor(mContext.getResources().getColor(R.color.white_background));
        } else {
            holder.root_rl.setBackgroundColor(mContext.getResources().getColor(R.color.type_item_bg));
        }

        return convertView;
    }

    private class ViewHolder {
        private RelativeLayout root_rl;
        private TextView type_name_tv;
        private ImageView cate_iv;
    }
}
