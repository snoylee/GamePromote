package com.xygame.sg.activity.notice;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.xygame.sg.R;
import com.xygame.sg.activity.notice.adapter.SortListAdapter;

/**
 * Created by xy on 2015/12/9.
 */
public class SortPopWindow extends PopupWindow implements AdapterView.OnItemClickListener {
    private Context mContext;
    private LinearLayout notice_sort_root_ll;
    private SortListAdapter mAdapter;
    private ListView sort_lv;
    private IOnSortItemSelectListener mItemSelectListener;
    public SortPopWindow(Context context) {
        super(context);
        mContext = context;
        init();
    }
    public void setItemSelectListener(IOnSortItemSelectListener listener) {
        mItemSelectListener = listener;
    }

    public void setAdatper(SortListAdapter adapter) {
        mAdapter = adapter;
        sort_lv.setAdapter(mAdapter);
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notice_sort, null);
        setContentView(view);
        setFocusable(true);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);

        notice_sort_root_ll = (LinearLayout) view.findViewById(R.id.notice_sort_root_ll);

        sort_lv = (ListView) view.findViewById(R.id.sort_lv);
        sort_lv.setOnItemClickListener(this);
        notice_sort_root_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        dismiss();
        mAdapter.setSelectedItem(i);
        if (mItemSelectListener != null) {
            mItemSelectListener.onSortItemClick(i);
        }
    }

    public  interface IOnSortItemSelectListener{
         void onSortItemClick(int pos);
    }
}
