package com.xygame.second.sg.biggod.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xygame.second.sg.biggod.bean.PriceBean;
import com.xygame.second.sg.comm.inteface.PriceListener;
import com.xygame.sg.R;
import com.xygame.sg.utils.ConstTaskTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tony on 2016/12/20.
 */
public class SetPriceAdapter extends BaseAdapter {
    private Context context;
    private List<PriceBean> vector;
    private Map<String,String> markMap=new HashMap<>();
//    private String priceRate;
    private PriceListener priceListener;

    public SetPriceAdapter(Context context, List<PriceBean> vector) {
//        this.priceRate=priceRate;
        this.context = context;
        if(vector!=null){
            this.vector = vector;
        }else{
            this.vector=new ArrayList<>();
        }
        String id=this.vector.get(0).getId();
        markMap.put("markMap",id);
    }

    public void addPriceListener( PriceListener priceListener){
        this.priceListener=priceListener;
    }

    @Override
    public int getCount() {
        return vector.size();
    }

    @Override
    public PriceBean getItem(int position) {
        return vector.get(position);
    }

    public PriceBean getSelectedItem(){
        PriceBean it=null;
        for (PriceBean item:vector){
            if (item.getId().equals(markMap.get("markMap"))){
                it=item;
                break;
            }
        }
        return it;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder = null;
        if (null == convertView)
        {
            viewHolder = new ViewHolder();
            convertView =LayoutInflater.from(context).inflate(
                    R.layout.cub_jp_small_type_item, parent, false);

            viewHolder.typeName = (TextView) convertView.findViewById(R.id.typeName);
            viewHolder.gouView = convertView
                    .findViewById(R.id.gouView);
            viewHolder.backgroundColor =convertView
                    .findViewById(R.id.backgroundColor);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PriceBean item=vector.get(position);
        viewHolder.typeName.setText(item.getPrice().concat("元"));

        String id=markMap.get("markMap");
        if (item.getId().equals(id)){
            viewHolder.gouView.setVisibility(View.GONE);
            viewHolder.typeName.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.backgroundColor.setBackgroundResource(R.drawable.shape_rect_dark_green10);
        }else{
            viewHolder.gouView.setVisibility(View.GONE);
            viewHolder.typeName.setTextColor(context.getResources().getColor(R.color.dark_green));
            viewHolder.backgroundColor.setBackgroundResource(R.drawable.shape_rect_white);
        }
        convertView.setOnClickListener(new TypeClickListener(item));
        return convertView;
    }

    private static class ViewHolder
    {
        TextView typeName;
        View backgroundColor,gouView;
    }

    private class TypeClickListener implements View.OnClickListener{
        private PriceBean item;
        public TypeClickListener(PriceBean item){
            this.item=item;
        }

        @Override
        public void onClick(View v) {
            updateSelect(item);
        }
    }

    private void updateSelect(PriceBean item) {
        markMap.put("markMap",item.getId());
        notifyDataSetChanged();
        priceListener.priceListener();
    }

    public List<PriceBean> getSmallTypes(){
        List<PriceBean> datas=new ArrayList<>();
        for (PriceBean it:vector){
            if (it.getId().equals(markMap.get("markMap"))){
                datas.add(it);
            }
        }
        return datas;
    }
}