package com.xygame.sg.activity.personal.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.personal.PersonalPhotoesActivity;
import com.xygame.sg.activity.personal.PersonalPhotoesBrowsersActivity;
import com.xygame.sg.activity.personal.bean.QueryModelGalleryView;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.widget.NestedGridView;

import java.util.List;
import java.util.Map;

/**
 * Created by xy on 2015/11/16.
 * 资料页面的adapter
 */
public class CMWorkAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;

    private List<QueryModelGalleryView> dataList;
    private static final int TYPE_DEFAULT = 0;
    private static final int TYPE_EMPTY = 1;

    /**
     * 标志是否从查看某个模特的入口进入
     */
    private boolean isQuery = false;

    /**
     * 作品集的用户id
     */
    private String userId;

    public CMWorkAdapter(Context context, List<QueryModelGalleryView> dataList, String userId, boolean isQuery) {
        super();
        this.context = context;
        this.userId=userId;
        if (context!=null){
            inflater = LayoutInflater.from(context);
        }else{
            inflater = LayoutInflater.from(SGApplication.getInstance().getApplicationContext());
        }
        this.dataList = dataList;
        this.isQuery = isQuery;
    }

    public void setDataList(List<QueryModelGalleryView> dataList) {
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return 1;
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
    public int getItemViewType(int position) {
        if (dataList.isEmpty()) {
            return TYPE_EMPTY;
        } else {
            return TYPE_DEFAULT;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        EmptyViewHolder emptyViewHolder = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case TYPE_DEFAULT:
                    convertView = inflater.inflate(R.layout.cm_person_work_item, null);
                    holder = new ViewHolder();
                    holder.album_gv = (NestedGridView) convertView.findViewById(R.id.photoList);
                    holder.album_gv.setSelector(new ColorDrawable(Color.TRANSPARENT));

                    convertView.setTag(holder);
                    break;
                case TYPE_EMPTY:
                    convertView = inflater.inflate(R.layout.common_empty_view, null);
                    convertView.setVisibility(View.VISIBLE);
                    TextView primary_tip_tv = (TextView) convertView.findViewById(R.id.primary_tip_tv);
                    primary_tip_tv.setText("还没有作品集！");
                    break;
            }

        } else {
            switch (type) {
                case TYPE_DEFAULT:
                    holder = (ViewHolder) convertView.getTag();
                    break;
                case TYPE_EMPTY:
                    emptyViewHolder = (EmptyViewHolder) convertView.getTag();
                    break;
            }
        }
        switch (type) {
            case TYPE_DEFAULT:


                holder.album_gv.setAdapter(new AlbumCoverAdapter(context, dataList));

                holder.album_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        final QueryModelGalleryView itemData = dataList.get(arg2);
                        if (isQuery) {
                            Intent intent = new Intent(context, PersonalPhotoesBrowsersActivity.class);
                            intent.putExtra("oral", itemData.getGalDesc());
                            intent.putExtra("id", itemData.getGalId()+"");
                            intent.putExtra("imageCount", itemData.getCount() + "");
                            intent.putExtra("priseCount", itemData.getPraiseCount() + "");
                            intent.putExtra("seeUserId", userId);
                            context.startActivity(intent);
                        } else {
                            Intent intent = new Intent(context, PersonalPhotoesActivity.class);
                            intent.putExtra("oral", itemData.getGalDesc());
                            intent.putExtra("id", itemData.getGalId()+"");
                            intent.putExtra("imageCount", itemData.getCount() + "");
                            intent.putExtra("priseCount", itemData.getPraiseCount() + "");
                            context.startActivity(intent);
                        }
                    }
                });
                break;
            case TYPE_EMPTY:

                break;
        }

        return convertView;
    }

    private class ViewHolder {

        private NestedGridView album_gv;// 相册封面

    }

    private class EmptyViewHolder {
        private TextView empty_tv;// 相册简介
    }
}
