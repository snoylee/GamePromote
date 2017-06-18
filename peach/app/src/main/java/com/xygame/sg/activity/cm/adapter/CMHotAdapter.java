package com.xygame.sg.activity.cm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.cm.bean.HotPhotographerView;
import com.xygame.sg.activity.cm.bean.HotPhotographerVo;
import com.xygame.sg.activity.model.bean.ModelBean;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.StringUtils;

import java.util.List;

/**
 * Created by xy on 2016/1/17.
 */
public class CMHotAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private HotPhotographerView hotPhotographers = new HotPhotographerView();
    private ImageLoader imageLoader;

    public CMHotAdapter(Context context, HotPhotographerView hotPhotographers) {
        super();
        this.context = context;
        this.hotPhotographers = hotPhotographers;

        inflater = LayoutInflater.from(context);
        imageLoader =ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
    }

    @Override
    public int getCount() {
        if (hotPhotographers!=null && hotPhotographers.getPhotops() != null && hotPhotographers.getPhotops().size() > 0 ) {
            return hotPhotographers.getPhotops().size();
        }
        return 0;
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.cm_hot_item, null);
            holder = new ViewHolder();
            holder.user_icon_iv = (ImageView) convertView.findViewById(R.id.user_icon_iv);
            holder.user_nick_tv = (TextView) convertView.findViewById(R.id.user_nick_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HotPhotographerVo photographerVo = hotPhotographers.getPhotops().get(i);
        String picUrl = photographerVo.getUserIcon();
        if (!StringUtils.isEmpty(picUrl)) {
            imageLoader.loadImage(picUrl, holder.user_icon_iv, true);
        }
        String userNick = photographerVo.getUsernick();
        if (!StringUtils.isEmpty(userNick)) {
            holder.user_nick_tv.setText(userNick);
        }
        return convertView;
    }

    private class ViewHolder {
        private ImageView user_icon_iv;
        private TextView user_nick_tv;
    }
}
