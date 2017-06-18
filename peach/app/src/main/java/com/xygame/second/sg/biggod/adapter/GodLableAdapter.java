package com.xygame.second.sg.biggod.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xygame.second.sg.biggod.bean.GodLableBean;
import com.xygame.sg.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2016/12/19.
 */
public class GodLableAdapter extends BaseAdapter {
    private Context context;
    private List<GodLableBean> vector;

    public GodLableAdapter(Context context, List<GodLableBean> vector) {
        this.context = context;
        if (vector != null) {
            this.vector = vector;
        } else {
            this.vector = new ArrayList<>();
        }
    }

    public List<GodLableBean> getDatas() {
        return vector;
    }

    @Override
    public int getCount() {
        return vector.size();
    }

    @Override
    public GodLableBean getItem(int position) {
        return vector.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.god_lable_item, parent, false);
            viewHolder.godLableName = (TextView) convertView.findViewById(R.id.godLableName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        GodLableBean item = vector.get(position);
       viewHolder.godLableName.setText(item.getTitleName());
        return convertView;
    }

    private static class ViewHolder {
        TextView godLableName;
    }
}