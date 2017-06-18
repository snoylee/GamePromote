package com.xygame.sg.activity.notice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.commen.bean.ShootTypeBean;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.SGApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xy on 2015/12/9.
 */
public class ShootTypeAdapter extends BaseAdapter {
    private Context mContext;
    private List<ShootTypeBean> mDataList;
    private List<Boolean> mSelectedList = new ArrayList<Boolean>();

    ImageLoader imageLoader;

    public List<ShootTypeBean> getmDataList() {
        return mDataList;
    }

    public ShootTypeAdapter(Context context) {
        super();
        mContext = context;
        imageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        mDataList = SGApplication.getInstance().getTypeList();
        if (mDataList==null){
            mDataList=new ArrayList<>();
        }
        mSelectedList.add(true);
        for (int i = 0; i < mDataList.size(); i++) {
            mSelectedList.add(false);
        }
    }

    public void setSelectedItem(int index) {
        for (int i = 0; i < mSelectedList.size(); i++) {
            if (i == index) {
                mSelectedList.set(i, true);
            } else {
                mSelectedList.set(i, false);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDataList.size() + 1;//默认的全部没从接口返回
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
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.notice_shoot_cate_item_temp, parent, false);
        }

        View vertical_line_view = BaseViewHolder.get(convertView, R.id.vertical_line_view);
        View selected_fl = BaseViewHolder.get(convertView, R.id.selected_fl);

        TextView type_name_tv = BaseViewHolder.get(convertView, R.id.type_name_tv);
        ImageView cate_iv = BaseViewHolder.get(convertView, R.id.cate_iv);


        boolean isLastSelected = mSelectedList.get(i);
        if (i > 0) {
            ShootTypeBean itemData = mDataList.get(i-1);

            type_name_tv.setText(itemData.getTypeName());
            if (isLastSelected) {
                imageLoader.loadImageNoThume(itemData.getIconSelectedUrl(), cate_iv, true);
                selected_fl.setVisibility(View.VISIBLE);
                type_name_tv.setTextColor(mContext.getResources().getColor(R.color.dark_green));
            } else {
                imageLoader.loadImageNoThume(itemData.getIconDefaultUrl(), cate_iv, true);
                selected_fl.setVisibility(View.GONE);
                type_name_tv.setTextColor(mContext.getResources().getColor(R.color.color_grey_999999));
            }

        } else {
            type_name_tv.setText("全部");

            if (isLastSelected) {
                cate_iv.setImageResource(R.drawable.all_selected);
                selected_fl.setVisibility(View.VISIBLE);
                type_name_tv.setTextColor(mContext.getResources().getColor(R.color.dark_green));
            } else {
                cate_iv.setImageResource(R.drawable.all);
                selected_fl.setVisibility(View.GONE);
                type_name_tv.setTextColor(mContext.getResources().getColor(R.color.color_grey_999999));
            }
        }

        if ((i + 1) % 3 == 0) {
            vertical_line_view.setVisibility(View.GONE);
        }


        return convertView;
    }
}
