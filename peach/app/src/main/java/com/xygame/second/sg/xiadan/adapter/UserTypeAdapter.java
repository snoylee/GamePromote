package com.xygame.second.sg.xiadan.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.xygame.second.sg.biggod.activity.GodDetailActivity;
import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.second.sg.xiadan.bean.GodTypeBean;
import com.xygame.second.sg.xiadan.bean.GodUserBean;
import com.xygame.sg.R;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.ConstTaskTag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2016/12/22.
 */
public class UserTypeAdapter extends BaseAdapter {
    private Context context;
    private List<GodTypeBean> vector;
    private ImageLoader mImageLoader;
    /**
     * 搜索Adapter初始化
     */
    public UserTypeAdapter(Context context, List<GodTypeBean> vector) {
        this.context = context;
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        if (vector != null) {
            this.vector = vector;
        } else {
            this.vector = new ArrayList<GodTypeBean>();
        }
    }

    @Override
    public int getCount() {
        return vector.size();
    }

    @Override
    public GodTypeBean getItem(int position) {
        return vector.get(position);
    }

    public List<GodTypeBean> getDatas() {
        return vector;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addPhotoes(List<GodTypeBean> datas) {
       this.vector=datas;
        notifyDataSetChanged();
    }

    /**
     * 初始化View
     */
    private class ViewHolder {
        private GridView photoList;
        private CircularImage godAppIcon;
        private TextView godAppName;
        private LeiXinAdapter gridAdapter;
    }

    /**
     * 添加数据
     */
    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.leixin_item,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.godAppName = (TextView) convertView
                    .findViewById(R.id.godAppName);
            viewHolder.photoList = (GridView) convertView
                    .findViewById(R.id.photoList);
            viewHolder.godAppIcon = (CircularImage) convertView.findViewById(R.id.godAppIcon);
            viewHolder.gridAdapter = new LeiXinAdapter(context, null);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.photoList.setAdapter(viewHolder.gridAdapter);

        final GodTypeBean item=vector.get(position);
        List<JinPaiBigTypeBean> typeDatas= CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
        for (JinPaiBigTypeBean it:typeDatas) {
            if (it.getId().equals(item.getSkillCode())) {
                viewHolder.godAppName.setText(it.getName());
                viewHolder.gridAdapter.addTypeBean(it);
                mImageLoader.loadImage(it.getUrl(), viewHolder.godAppIcon, false);
                break;
            }
        }
        viewHolder.gridAdapter.updatePhotoes(item.getUserBeans());
        viewHolder.gridAdapter.notifyDataSetChanged();
        viewHolder.photoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                GodUserBean it= viewHolder.gridAdapter.getItem(arg2);
                Intent intent=new Intent(context,GodDetailActivity.class);
                intent.putExtra("userId",it.getUserId());
                intent.putExtra("userName",it.getUsernick());
                intent.putExtra("skillCode",item.getSkillCode());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (observer != null) {
            super.unregisterDataSetObserver(observer);
        }
    }
}