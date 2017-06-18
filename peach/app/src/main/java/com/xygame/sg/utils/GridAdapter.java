/**
 *
 */
package com.xygame.sg.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.xygame.sg.R;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import base.adapter.Logs;

/**
 * @author huiming
 */
public class GridAdapter extends BaseAdapter {
    private LayoutInflater inflater; // 视图容器
    private boolean shape;
    private Context context;
    public List<String> cnt = new ArrayList<String>();
    private List<String> showcnt =  new ArrayList<String>();
    {
        showcnt.add("end");
    }
    public boolean isShape() {
        return shape;
    }

    public void setShape(boolean shape) {
        this.shape = shape;
    }

    public GridAdapter(Context context, Handler handler) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
    int max = 20;

    public void setMax(int max) {
        this.max = max;
    }

    public void setContent(List<String> paramcnt) {
        update();
        List cpcnt = this.cnt;

        this.cnt = new ArrayList<String>(paramcnt);

        if(cnt.get(cnt.size()-1).equals("end")){
            cnt.remove(cnt.size()-1);
        }
        cpcnt.addAll(cnt);
        this.cnt = cpcnt;
        showcnt = new ArrayList<String>(this.cnt);
        if(paramcnt.get(paramcnt.size()-1).equals("end")&&!showcnt.contains("end")){
            showcnt.add("end");

        }
    }

    public void update() {
        List<String> strs = new ArrayList<String>();
        for(int i = 0;i<cnt.size();i++){
            if(!new File(cnt.get(i)).exists()){
                strs.add(cnt.get(i));
            }

        }
        cnt.removeAll(strs);
        showcnt.removeAll(strs);
        if(showcnt.size()<max&&!showcnt.contains("end")){
            showcnt.add("end");
        }
        notifyDataSetChanged();
    }


    public int getCount() {
        return showcnt.size();
    }

    public Object getItem(int arg0) {

        return showcnt.get(arg0);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    /**
     * ListView Item设置
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView image;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_published_grida, null);
        }
        image = (ImageView) convertView.findViewById(R.id.item_grida_image);
        if (getItem(position).toString().equals("end")) {
            convertView = inflater.inflate(R.layout.item_published_grida, null);
            image = (ImageView) convertView.findViewById(R.id.item_grida_image);
            image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.addpicture_gray));
        } else {
            Logs.i("cnt position ----> "+cnt.get(position));
            ImageLocalLoader.getInstance(3, ImageLocalLoader.Type.LIFO).loadImage(showcnt.get(position), image);
        }

        return convertView;
    }

}
