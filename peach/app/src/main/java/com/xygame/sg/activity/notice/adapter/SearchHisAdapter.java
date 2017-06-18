package com.xygame.sg.activity.notice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xygame.sg.R;

import java.util.List;

/**
 * Created by xy on 2016/1/14.
 */
public class SearchHisAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private List<String> mData;

    public SearchHisAdapter(Context context, List<String> data) {
        this.context = context;
        this.mData = data;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<String> data) {
        if (mData != null){
            mData.clear();
            mData.addAll(data);
        }
    }

    @Override
    public int getCount() {
        if (mData != null){
            return mData.size();
        }
        return 0;
    }

    @Override
    public String getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.search_history_item, null);
        TextView history_text = (TextView) view.findViewById(R.id.history_text);
        history_text.setText(mData.get(i));
        return view;
    }
}
