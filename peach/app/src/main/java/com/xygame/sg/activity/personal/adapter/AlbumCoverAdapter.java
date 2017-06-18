package com.xygame.sg.activity.personal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.personal.bean.QueryModelGalleryView;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by xy on 2015/11/16.
 * 作品里面相册封面的adapter
 */
public class AlbumCoverAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;

    private List<QueryModelGalleryView> dataList;

    private ImageLoader imageLoader;

    public AlbumCoverAdapter(Context context, List<QueryModelGalleryView> dataList) {
        super();
        this.context = context;
        imageLoader=ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        inflater = LayoutInflater.from(context);
        this.dataList = dataList;
    }
    @Override
    public int getCount() {
//        if (dataList.size()>3){
//            return 3;
//        } else {
//            return dataList.size();
//        }
        return dataList.size();
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;

        if (view == null) {
            view = inflater.inflate(R.layout.item_photoe_list_imgs, null);
            holder = new ViewHolder();
            holder.id_img = (ImageView) view.findViewById(R.id.id_img);
            holder.photoName = (TextView) view.findViewById(R.id.photoName);
            holder.photoesNums = (TextView) view.findViewById(R.id.photoesNums);
            holder.priseNums = (TextView) view.findViewById(R.id.priseNums);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        QueryModelGalleryView itemData = dataList.get(i);
        if (itemData.getCover()!=null && !StringUtils.isEmpty(itemData.getCover().getResUrl())){
            imageLoader.loadImage(itemData.getCover().getResUrl(), holder.id_img, false);
        } else {
            holder.id_img.setImageResource(R.drawable.moren_icon);
        }


        String num = itemData.getCount()+"";
        holder.photoesNums.setText(num);
        String praiseCount = itemData.getPraiseCount()+"";
        holder.priseNums.setText(praiseCount);
        return view;
    }

    private class ViewHolder {
        private ImageView id_img;
        private TextView photoName;
        private TextView photoesNums;
        private TextView priseNums;
    }
}
