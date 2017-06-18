package com.xygame.sg.activity.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.model.bean.ModelBean;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.StringUtils;

import java.util.List;

/**
 * Created by xy on 2016/1/17.
 */
public class ModelHotAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private List<ModelBean> models;
    private ImageLoader imageLoader;

    public ModelHotAdapter(Context context, List<ModelBean> models) {
        super();
        this.context = context;
        this.models = models;

        inflater = LayoutInflater.from(context);
        imageLoader =ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
    }

    @Override
    public int getCount() {
        if (models != null) {
            return models.size();
        }
        return 0;
    }

    @Override
    public ModelBean getItem(int i) {
        return models.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.model_hot_item, null);
            holder = new ViewHolder();
            holder.user_icon_iv = (ImageView) convertView.findViewById(R.id.user_icon_iv);
            holder.user_nick_tv = (TextView) convertView.findViewById(R.id.user_nick_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ModelBean modelBean = models.get(i);
        String picUrl = modelBean.getUserIcon();
        if (!StringUtils.isEmpty(picUrl)) {
            imageLoader.loadImage(picUrl, holder.user_icon_iv, true);
        }
        String userNick = modelBean.getUserNick();
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
