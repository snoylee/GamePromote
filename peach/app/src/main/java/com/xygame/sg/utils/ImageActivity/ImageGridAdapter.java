package com.xygame.sg.utils.ImageActivity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.sg.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by minhua on 2015/11/9.
 */
public class ImageGridAdapter extends BaseAdapter {

    private TextCallback textcallback = null;
    final String TAG = getClass().getSimpleName();
    Activity act;
    List<ImageItem> dataList;
    //public Map<String, String> map = new HashMap<String, String>();

    public Map<String, String> map = Collections.synchronizedMap(new LinkedHashMap<String, String>());
    BitmapCacheActivity cache;
    private Handler mHandler;
    private int selectTotal = 0;
    private int total = Bimp.max_photo;
    BitmapCacheActivity.ImageCallback callback = new BitmapCacheActivity.ImageCallback() {
        @Override
        public void imageLoad(ImageView imageView, Bitmap bitmap,
                              Object... params) {
            if (imageView != null && bitmap != null) {
                String url = (String) params[0];
                if (url != null && url.equals(imageView.getTag())) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    Log.e(TAG, "callback, bmp not match");
                }
            } else {
                Log.e(TAG, "callback, bmp null");
            }
        }
    };

    public interface TextCallback {
        void onListen(int count);
    }

    public void setTextCallback(TextCallback listener) {
        textcallback = listener;
    }
    int presize = 0;
    public ImageGridAdapter(Activity act, List<ImageItem> list, Handler mHandler, int total) {
        this.act = act;
        dataList = list;
        cache = new BitmapCacheActivity();
        this.mHandler = mHandler;
        this.total = total;
        presize = 0;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (dataList != null) {
            count = dataList.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    public List<String> list = new ArrayList<String>();
    class Holder {
        private ImageView iv;
        private ImageView selected;
        private TextView text;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;

        if (convertView == null) {
            holder = new Holder();
            convertView = View.inflate(act, R.layout.item_image_grid, null);
            holder.iv = (ImageView) convertView.findViewById(R.id.image);
            holder.selected = (ImageView) convertView
                    .findViewById(R.id.isselected);
            holder.text = (TextView) convertView
                    .findViewById(R.id.item_image_grid_text);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final ImageItem item = dataList.get(position);
//        if(Bimp.drr.contains(item.imagePath)){
//            item.isSelected = true;
//        }else{
//            item.isSelected = false;
//        }
        holder.iv.setTag(item.imagePath);
        cache.displayBmp(holder.iv, item.thumbnailPath, item.imagePath,
                callback);
        if (item.isSelected) {
            holder.selected.setImageResource(R.drawable.icon_data_select);
            holder.text.setBackgroundResource(R.drawable.bgd_relatly_line);

        } else {
            holder.selected.setImageResource(-1);
            holder.text.setBackgroundColor(0x00000000);

        }
        holder.iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String path = dataList.get(position).imagePath;

                if ((presize + selectTotal) < total) {

                    if (item.isSelected) {

                        holder.selected.setImageResource(-1);
                        holder.text.setBackgroundColor(0x00000000);
                        selectTotal--;
                        if (textcallback != null)
                            textcallback.onListen(selectTotal);

                        list.remove(item.imagePath);
                    } else if (!item.isSelected) {
                        holder.selected
                                .setImageResource(R.drawable.icon_data_select);
                        holder.text.setBackgroundResource(R.drawable.bgd_relatly_line);
                        selectTotal++;
                        if (textcallback != null)
                            textcallback.onListen(selectTotal);

                        list.add(item.imagePath);
                    }
                    item.isSelected = !item.isSelected;
                } else if ((presize + selectTotal) == total) {
                    if (!item.isSelected) {

                        Message message = Message.obtain(mHandler, 0);
                        message.sendToTarget();
                    } else {

                        holder.selected.setImageResource(-1);
                        holder.text.setBackgroundColor(0x00000000);
                        selectTotal--;
                        if (textcallback != null)
                            textcallback.onListen(selectTotal);

                        list.remove(item.imagePath);
                        item.isSelected = !item.isSelected;

                    }
                }
                if(total==1&&item.isSelected){
                    Message message = Message.obtain(mHandler, 1);
                    message.sendToTarget();
                }
            }

        });

        return convertView;
    }
}