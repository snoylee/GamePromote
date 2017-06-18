package com.xygame.sg.activity.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.xygame.sg.R;
import com.xygame.sg.activity.model.bean.BannerBean;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.StringUtils;

import java.util.List;

/**
 * Created by xy on 2016/1/17.
 */
public class BannerAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private List<BannerBean> banerList;
    private ImageLoader imageLoader;


    public BannerAdapter(Context context, List<BannerBean> banerList) {
        super();
        this.context = context;
        this.banerList = banerList;

        inflater = LayoutInflater.from(context);
        imageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
    }

    @Override
    public int getCount() {
        if (banerList != null) {
            return banerList.size();
        }
        return 0;
    }

    @Override
    public BannerBean getItem(int i) {
        return banerList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.model_banner_item, null);
            holder = new ViewHolder();
            holder.banner_iv = (ImageView) convertView.findViewById(R.id.banner_iv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BannerBean bannerBean = banerList.get(i);
        String picUrl = bannerBean.getPicUrl();
        if (!StringUtils.isEmpty(picUrl)) {
            imageLoader.loadImage(picUrl, holder.banner_iv, true);
        }
        return convertView;
    }

    private class ViewHolder {
        private ImageView banner_iv;
    }
}
