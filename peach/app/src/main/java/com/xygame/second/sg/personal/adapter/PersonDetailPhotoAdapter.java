package com.xygame.second.sg.personal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.xygame.second.sg.personal.bean.PhotoBean;
import com.xygame.sg.R;
import com.xygame.sg.image.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2016/8/31.
 */
public class PersonDetailPhotoAdapter extends BaseAdapter {
    private Context context;
    private List<PhotoBean> vector;
    private ImageLoader mImageLoader;

    public PersonDetailPhotoAdapter(Context context, List<PhotoBean> vector) {
        this.context = context;
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        if (vector != null) {
            this.vector = vector;
        } else {
            this.vector = new ArrayList<PhotoBean>();
        }
    }

    @Override
    public int getCount() {
        return vector.size();
    }

    @Override
    public PhotoBean getItem(int position) {
        return vector.get(position);
    }

    public void addDatas(List<PhotoBean> datas) {
        vector.clear();
        vector.addAll(datas);
    }

    public List<PhotoBean> getDatas() {
        return vector;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_fragment_list_imgs, parent, false);
        }
        ImageView imageview = (ImageView) convertView
                .findViewById(R.id.id_img);
        mImageLoader.loadImage(getItem(position).getResUrl(), imageview, true);
        return convertView;
    }
}