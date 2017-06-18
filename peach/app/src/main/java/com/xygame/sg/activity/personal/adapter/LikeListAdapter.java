package com.xygame.sg.activity.personal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.xygame.sg.R;
import com.xygame.sg.define.draggrid.DataTools;

/**
 * Created by xy on 2015/11/17.
 */
public class LikeListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;

    public LikeListAdapter(Context context) {
        super();
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 5;
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

        convertView = inflater.inflate(R.layout.like_list_item, null);
        View divider_line = convertView.findViewById(R.id.divider_line);



        if (i == getCount() - 1) {
            LinearLayout.LayoutParams layoutParamsV = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
            divider_line.setLayoutParams(layoutParamsV);
        }

        return convertView;
    }


}