package com.xygame.second.sg.xiadan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.sg.R;
import com.xygame.sg.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tony on 2017/2/6.
 */
public class GodQiangTypeAdapter extends BaseAdapter {
    private Context context;
    private List<JinPaiBigTypeBean> vector;
    private Map<String, String> markMap = new HashMap<>();

    public GodQiangTypeAdapter(Context context, List<JinPaiBigTypeBean> vector) {
        this.context = context;
        if (vector != null) {
            this.vector = vector;
            String id = this.vector.get(0).getId();
            markMap.put("markMap", id);
        } else {
            this.vector = new ArrayList<>();
        }
    }

    @Override
    public int getCount() {
        return vector.size();
    }

    @Override
    public JinPaiBigTypeBean getItem(int position) {
        return vector.get(position);
    }

    public JinPaiBigTypeBean getSelectedItem() {
        JinPaiBigTypeBean it = null;
        for (JinPaiBigTypeBean item : vector) {
            if (item.getId().equals(markMap.get("markMap"))) {
                it = item;
                break;
            }
        }
        return it;
    }

    public void updateJinPaiDatas(List<JinPaiBigTypeBean> vector){
        for (int i=0;i<vector.size();i++){
            if (!Constants.DEFINE_LOL_ID.equals(vector.get(i).getId())){
                this.vector.add(vector.get(i));
            }
        }
        if (this.vector.size()>0){
            String id = this.vector.get(0).getId();
            markMap.put("markMap", id);
            notifyDataSetChanged();
        }
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
                    R.layout.god_qiang_type_item, parent, false);

            viewHolder.typeName = (TextView) convertView.findViewById(R.id.typeName);
            viewHolder.backgroundColor = convertView
                    .findViewById(R.id.backgroundColor);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        JinPaiBigTypeBean item = vector.get(position);

        viewHolder.typeName.setText(item.getName());

        String id = markMap.get("markMap");
        if (item.getId().equals(id)) {
            viewHolder.typeName.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.backgroundColor.setBackgroundResource(R.drawable.shape_rect_dark_green10);
        } else {
            viewHolder.typeName.setTextColor(context.getResources().getColor(R.color.dark_green));
            viewHolder.backgroundColor.setBackgroundDrawable(null);//.setBackgroundResource(R.drawable.shape_rect_white);
        }
        convertView.setOnClickListener(new TypeClickListener(item));
        return convertView;
    }

    private static class ViewHolder {
        TextView typeName;
        View backgroundColor;
    }

    private class TypeClickListener implements View.OnClickListener {
        private JinPaiBigTypeBean item;

        public TypeClickListener(JinPaiBigTypeBean item) {
            this.item = item;
        }

        @Override
        public void onClick(View v) {
            updateSelect(item);
        }
    }

    private void updateSelect(JinPaiBigTypeBean item) {
        markMap.put("markMap", item.getId());
        notifyDataSetChanged();
    }
}