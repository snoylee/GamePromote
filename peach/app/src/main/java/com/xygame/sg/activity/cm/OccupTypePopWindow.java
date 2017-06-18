package com.xygame.sg.activity.cm;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.xygame.sg.R;
import com.xygame.sg.activity.cm.adapter.OccupTypeAdapter;
import com.xygame.sg.activity.notice.adapter.ShootTypeAdapter;

/**
 * Created by xy on 2015/12/9.
 */
public class OccupTypePopWindow extends PopupWindow implements AdapterView.OnItemClickListener {
    private Context mContext;
    private LinearLayout notice_shoot_cate_root_ll;
    private OccupTypeAdapter mAdapter;
    private GridView shoot_cate_gv;
    private IOnOccupTypeItemSelectListener mItemSelectListener;
    public OccupTypePopWindow(Context context) {
        super(context);
        mContext = context;
        init();
    }
    public void setItemSelectListener(IOnOccupTypeItemSelectListener listener) {
        mItemSelectListener = listener;
    }

    public void setAdatper(OccupTypeAdapter adapter) {
        mAdapter = adapter;
        shoot_cate_gv.setAdapter(mAdapter);
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notice_shoot_cate, null);
        setContentView(view);
        setFocusable(true);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);

        notice_shoot_cate_root_ll = (LinearLayout) view.findViewById(R.id.notice_shoot_cate_root_ll);

        shoot_cate_gv = (GridView) view.findViewById(R.id.shoot_cate_gv);
        shoot_cate_gv.setOnItemClickListener(this);
        notice_shoot_cate_root_ll.setOnClickListener(new View.OnClickListener() {
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
            mItemSelectListener.onOccupTypeItemClick(i);
        }
    }

    public  interface IOnOccupTypeItemSelectListener{
         void onOccupTypeItemClick(int pos);
    }
}
