package com.xygame.sg.activity.notice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.xygame.sg.R;
import com.xygame.sg.activity.notice.bean.NoticeReferPicVo;
import com.xygame.sg.image.ImageLoader;

import java.util.List;

/**
 * Created by xy on 2015/11/16.
 * 作品里面相册封面的adapter
 */
public class RefWorkAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;

    List<NoticeReferPicVo> referPics;

    private ImageLoader imageLoader;

    public RefWorkAdapter(Context context, List<NoticeReferPicVo> referPics) {
        super();
        this.context = context;
        imageLoader=ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        inflater = LayoutInflater.from(context);
        this.referPics = referPics;
    }
    @Override
    public int getCount() {
        if (referPics.size()>8){
            return 8;
        } else {
            return referPics.size();
        }
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
        NoticeReferPicVo itemData = referPics.get(i);
        view = inflater.inflate(R.layout.refwork_album_cover_item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.album_item_iv);
        imageLoader.loadImage(itemData.getPicUrl(),imageView,false);

        return view;
    }
}
