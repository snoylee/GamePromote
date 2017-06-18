package com.xygame.sg.activity.personal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.xygame.sg.R;

/**
 * Created by xy on 2015/11/18.
 */
public class SelectJobTypeListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;

    public SelectJobTypeListAdapter(Context context) {
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

        convertView = inflater.inflate(R.layout.common_category_item, null);

        if (i == getCount() - 1) {
            View bottom_divider = convertView.findViewById(R.id.bottom_divider);
            LinearLayout.LayoutParams layoutParamsV = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
            bottom_divider.setLayoutParams(layoutParamsV);
        }

        return convertView;
    }
}
