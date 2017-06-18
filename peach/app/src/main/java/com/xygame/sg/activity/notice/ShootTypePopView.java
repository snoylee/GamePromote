package com.xygame.sg.activity.notice;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.xygame.sg.R;
import com.xygame.sg.activity.notice.adapter.SelectShootTypeAdapter;
import com.xygame.sg.activity.notice.adapter.SubShootTypeAdapter;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.activity.commen.bean.ShootSubTypeBean;
import com.xygame.sg.activity.commen.bean.ShootTypeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xy on 2016/2/16.
 */
public class ShootTypePopView extends LinearLayout {
    private Context mContext;
    private ListView shootTypeLv;
    private ListView subShootTypeLv;
    private SelectShootTypeAdapter shootTypeAdapter;
    private SubShootTypeAdapter subShootTypeAdapter;
    private List<ShootTypeBean> shootTypeBeans;
    private List<ShootSubTypeBean> subShootTypeBeans;

    private OnSelectListener mOnSelectListener;
    private int currentCityPos;
    private int position = -1;
    private int subPosition = -1;


    public ShootTypePopView(Context context) {
        super(context);
        init(context);
    }

    public ShootTypePopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(final Context context) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.shoot_type_pop_view, this, true);
        shootTypeLv = (ListView) findViewById(R.id.shoot_type_lv);
        subShootTypeLv = (ListView) findViewById(R.id.sub_shoot_type_lv);
        shootTypeBeans = SGApplication.getInstance().getTypeList();
        if (shootTypeBeans==null){
            shootTypeBeans=new ArrayList<>();
        }
        shootTypeAdapter = new SelectShootTypeAdapter(mContext);
        shootTypeLv.setAdapter(shootTypeAdapter);

        subShootTypeBeans = new ArrayList<ShootSubTypeBean>();
        subShootTypeAdapter = new SubShootTypeAdapter(context,subShootTypeBeans);
        subShootTypeLv.setAdapter(subShootTypeAdapter);

        shootTypeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                position = i-1;
                if (i == 0) {
                    subShootTypeBeans = new ArrayList<ShootSubTypeBean>();
                } else {
                    subShootTypeBeans = shootTypeBeans.get(i - 1).getSubTypes();
                }
                subShootTypeAdapter = new SubShootTypeAdapter(mContext, subShootTypeBeans);
                subShootTypeLv.setAdapter(subShootTypeAdapter);
                if (i==0){
                    ShootTypeBean typeBean = new ShootTypeBean();
                    typeBean.setTypeId(-1);
                    typeBean.setTypeName("全部");

                    ShootSubTypeBean subTypeBean = new ShootSubTypeBean();
                    mOnSelectListener.setSelectedType(typeBean, subTypeBean);
                }
                shootTypeAdapter.setSelectedItem(i);
            }

        });

        subShootTypeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mOnSelectListener != null) {
                    ShootTypeBean typeBean = new ShootTypeBean();
                    if (position == -1){
                        typeBean.setTypeId(-1);
                        typeBean.setTypeName("全部");
                    } else {
                        typeBean = shootTypeBeans.get(position);
                    }
                    subShootTypeAdapter.setSelectedItem(i);
                    ShootSubTypeBean subTypeBean = subShootTypeBeans.get(i);
                    mOnSelectListener.setSelectedType(typeBean, subTypeBean);
                }

            }
        });
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        mOnSelectListener = onSelectListener;
    }

    public interface OnSelectListener {

        void setSelectedType(ShootTypeBean typeBean, ShootSubTypeBean subTypeBean);
    }
}
